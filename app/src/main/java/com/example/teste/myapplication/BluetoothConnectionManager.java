package com.example.teste.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import com.example.teste.myapplication.R;

public final class BluetoothConnectionManager extends BroadcastReceiver implements Runnable, View.OnClickListener, AlertDialog.OnClickListener, AlertDialog.OnCancelListener, AlertDialog.OnDismissListener, AdapterView.OnItemClickListener {
	public static final int REQUEST_ENABLE = 0x1000;

	public static final int OK = 0;
	public static final int NOT_ENABLED = 1;
	public static final int ERROR = -1;
	public static final int ERROR_NOT_ENABLED = -2;
	public static final int ERROR_DISCOVERY = -3;
	public static final int ERROR_NOTHING_PAIRED = -4;
	public static final int ERROR_STILL_PAIRING = -5;
	public static final int ERROR_CONNECTION = -6;

	private static ColorStateList defaultTextColors, highlightTextColors;

	public static interface BluetoothObserver {
		public void onBluetoothPairingStarted(BluetoothConnectionManager manager, String description, String address);
		public void onBluetoothPairingFinished(BluetoothConnectionManager manager, String description, String address, boolean success);
		public void onBluetoothCancelled(BluetoothConnectionManager manager);
		public void onBluetoothConnected(BluetoothConnectionManager manager);
		public void onBluetoothError(BluetoothConnectionManager manager, int error);
	}

	private static final class DeviceItem extends BaseItem {
		public final String description, address;
		public final boolean paired, recentlyUsed;

		public DeviceItem(String description, String address, boolean paired, boolean recentlyUsed) {
			this.description = description;
			this.address = address;
			this.paired = paired;
			this.recentlyUsed = recentlyUsed;
		}

		@Override
		public String toString() {
			return description;
		}
	}

	private static final class DeviceList extends BaseList<DeviceItem> {
		private Context context;

		public DeviceList(Context context) {
			super(DeviceItem.class);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView txt = (TextView)convertView;
			if (txt == null)
				txt = new TextView(context);
			txt.setTextColor(items[position].recentlyUsed ? highlightTextColors : defaultTextColors);
			txt.setText(items[position].description);
			return txt;
		}
	}

	private BluetoothObserver observer;
	private Activity activity;
	private BluetoothAdapter btAdapter;
	private BluetoothSocket btSocket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private DeviceList deviceList;
	private Button btnOK;
	private TextView lblTitle;
	private ProgressBar barWait;
	private ListView listView;
	private AlertDialog dialog;
	private boolean closed, connecting, finished;
	private int lastError;
	private volatile DeviceItem deviceItem;
	private volatile boolean cancelled;

	public BluetoothConnectionManager(Activity activity, BluetoothObserver observer) {
		MainHandler.initialize();
		this.activity = activity;
		this.observer = observer;
		try {
			btAdapter = BluetoothAdapter.getDefaultAdapter();
		} catch (Throwable ex) {
		}
		activity.registerReceiver(this, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		activity.registerReceiver(this, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void destroyUI() {
		if (btAdapter != null) {
			try {
				if (btAdapter.isDiscovering())
					btAdapter.cancelDiscovery();
			} catch (Throwable ex) {
			}
			btAdapter = null;
		}
		if (listView != null) {
			listView.setAdapter(null);
			listView = null;
		}
		if (activity != null) {
			activity.unregisterReceiver(this);
			activity = null;
		}
		deviceList = null;
		btnOK = null;
		lblTitle = null;
		barWait = null;
		dialog = null;
		deviceItem = null;
	}

	public void destroy() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Throwable ex) {
			}
			inputStream = null;
		}
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (Throwable ex) {
			}
			outputStream = null;
		}
		if (btSocket != null) {
			try {
				btSocket.close();
			} catch (Throwable ex) {
			}
			btSocket = null;
		}
		destroyUI();
		observer = null;
	}

	@Override
	public void run() {
		final DeviceItem deviceItem = this.deviceItem;
		if (deviceItem == null)
			return;
		if (MainHandler.isOnMainThread()) {
			final boolean success = (inputStream != null && outputStream != null && lastError == OK);
			final boolean callObserver = !finished;
			finished = true;
			if (dialog != null)
				dialog.dismiss();
			destroyUI();
			connecting = false;
			if (observer != null && callObserver) {
				observer.onBluetoothPairingFinished(this, deviceItem.description, deviceItem.address, success);
				if (cancelled)
					observer.onBluetoothCancelled(this);
				else if (lastError == OK)
					observer.onBluetoothConnected(this);
				else
					observer.onBluetoothError(this, lastError);
			}
			return;
		}
		try {
			BluetoothDevice device = btAdapter.getRemoteDevice(deviceItem.address);
			if (device == null) {
				lastError = ERROR_NOTHING_PAIRED;
				MainHandler.postToMainThread(this);
				return;
			}
			final UUID SERIAL_PORT_SERVICE_CLASS_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
			btSocket = device.createRfcommSocketToServiceRecord(SERIAL_PORT_SERVICE_CLASS_UUID);
			try {
				btSocket.connect();
			} catch (Throwable ex) {
				btSocket = null;
				if (!deviceItem.paired) {
					lastError = ERROR_STILL_PAIRING;
					MainHandler.postToMainThread(this);
					return;
				}
				// try another method for connection, this should work on the HTC desire, credits to Michael Biermann
				try {
					Method mMethod = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
					btSocket = (BluetoothSocket)mMethod.invoke(device, Integer.valueOf(1));
					btSocket.connect();
				} catch (Throwable e1){
					btSocket = null;
					lastError = ERROR_CONNECTION;
					MainHandler.postToMainThread(this);
					return;
				}
			}
			inputStream = btSocket.getInputStream();
			outputStream = btSocket.getOutputStream();
		} catch (Throwable ex) {
			lastError = (!deviceItem.paired ? ERROR_STILL_PAIRING : ERROR_CONNECTION);
			MainHandler.postToMainThread(this);
			return;
		}
		lastError = OK;
		MainHandler.postToMainThread(this);
	}

	public int showDialogAndScan() {
		if (btAdapter == null || activity == null)
			return ERROR;
		try {
			if (!btAdapter.isEnabled()) {
				activity.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE);
				return NOT_ENABLED;
			}
		} catch (Throwable ex) {
			return ERROR_NOT_ENABLED;
		}
		LinearLayout l = new LinearLayout(activity);

		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lblTitle = new TextView(activity);
		lblTitle.setText(R.string.scanning);
		lblTitle.setLayoutParams(p);

		p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		p.topMargin = 5;
		barWait = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
		barWait.setIndeterminate(true);
		barWait.setLayoutParams(p);

		p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		p.topMargin = 5;
		listView = new ListView(activity);
		listView.setLayoutParams(p);

		l.addView(lblTitle);
		l.addView(barWait);
		l.addView(listView);

		deviceList = new DeviceList(activity);
		listView.setAdapter(deviceList);
		listView.setOnItemClickListener(this);
		defaultTextColors = lblTitle.getTextColors();
		highlightTextColors = ColorStateList.valueOf(0xff33b5e5);

		final Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices)
				deviceList.add(new DeviceItem(device.getName() + " - " + device.getAddress(), device.getAddress(), true, false), -1);
		}
		try {
			if (btAdapter.isDiscovering())
				btAdapter.cancelDiscovery();
		} catch (Throwable ex) {
		}
		try {
			btAdapter.startDiscovery();
		} catch (Throwable ex) {
			return ERROR_DISCOVERY;
		}

		dialog = (new AlertDialog.Builder(activity))
			.setTitle(activity.getText(R.string.devices))
			.setView(l)
			.setPositiveButton(R.string.refresh_list, this)
			.setNegativeButton(R.string.cancel, this)
			.create();
		dialog.setOnCancelListener(this);
		dialog.setOnDismissListener(this);
		dialog.show();
		btnOK = dialog.getButton(Dialog.BUTTON_POSITIVE);
		if (btnOK != null) {
			btnOK.setEnabled(false);
			btnOK.setOnClickListener(this);
		}

		return OK;
	}

	private void stopDialogDiscovery() {
		try {
			if (btAdapter != null && btAdapter.isDiscovering())
				btAdapter.cancelDiscovery();
		} catch (Throwable ex) {
		}
		if (lblTitle != null)
			lblTitle.setVisibility(View.GONE);
		if (barWait != null)
			barWait.setVisibility(View.GONE);
		if (btnOK != null)
			btnOK.setEnabled(true);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (activity == null || deviceList == null)
			return;
		final String action = intent.getAction();
		if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			final String address = device.getAddress();
			boolean paired = false;
			for (int i = deviceList.getCount() - 1; i >= 0; i--) {
				if (deviceList.getItemT(i).address.equals(address)) {
					paired = deviceList.getItemT(i).paired;
					deviceList.setSelection(i, true);
					deviceList.removeSelection();
					break;
				}
			}
			final String name = device.getName();
			deviceList.add(new DeviceItem(((name == null || name.length() == 0) ? activity.getText(R.string.null_device_name).toString() : name) + " - " + address, address, paired, false), -1);
			deviceList.setSelection(-1, true);
		} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			if (!connecting) {
				stopDialogDiscovery();
				if (deviceList.getCount() == 0)
					deviceList.add(new DeviceItem(activity.getText(R.string.none_found).toString(), null, false, false), -1);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (dialog != null && deviceList != null && position >= 0 && position < deviceList.getCount()) {
			final DeviceItem item = deviceList.getItemT(position);
			if (item != null && item.address != null) {
				stopDialogDiscovery();
				deviceItem = item;
				if (lblTitle != null) {
					lblTitle.setText(R.string.connecting_please_wait);
					lblTitle.setVisibility(View.VISIBLE);
				}
				if (barWait != null)
					barWait.setVisibility(View.VISIBLE);
				if (listView != null)
					listView.setVisibility(View.GONE);
				if (btnOK != null)
					btnOK.setEnabled(false);
				if (observer != null)
					observer.onBluetoothPairingStarted(this, item.description, item.address);
				lastError = OK;
				cancelled = false;
				finished = false;
				connecting = true;
				(new Thread(this, "Bluetooth Manager Thread")).start();
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (view == btnOK && btAdapter != null && !connecting) {
			if (lblTitle != null)
				lblTitle.setVisibility(View.VISIBLE);
			if (barWait != null)
				barWait.setVisibility(View.VISIBLE);
			try {
				btAdapter.startDiscovery();
			} catch (Throwable ex) {
				return;
			}
			btnOK.setEnabled(false);
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == AlertDialog.BUTTON_NEGATIVE)
			cancelled = true;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (!closed) {
			closed = true;
			destroyUI();
			if (!finished) {
				cancelled = true;
				finished = true;
				if (observer != null) {
					if (deviceItem != null)
						observer.onBluetoothPairingFinished(this, deviceItem.description, deviceItem.address, false);
					observer.onBluetoothCancelled(this);
				}
			}
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		onDismiss(dialog);
	}
}
