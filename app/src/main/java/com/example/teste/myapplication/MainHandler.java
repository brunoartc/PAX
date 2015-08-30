package com.example.teste.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

public final class MainHandler extends Handler {
	public static final int MSG_HANDLER_TOAST = 0x0500;
	
	private static MainHandler theHandler;
	private static Thread mainThread;
	
	private MainHandler() {
		super(Looper.getMainLooper());
	}
	
	public static MainHandler initialize() {
		if (theHandler == null) {
			theHandler = new MainHandler();
			mainThread = Looper.getMainLooper().getThread();
		}
		return theHandler;
	}
	
	public static boolean isOnMainThread() {
		return (mainThread == Thread.currentThread());
	}
	
	public static void toast(Throwable ex) {
		theHandler.sendMessageAtTime(Message.obtain(theHandler, MSG_HANDLER_TOAST, 0, 0, ex), SystemClock.uptimeMillis());
	}
	
	public static void toast(int resId) {
		theHandler.sendMessageAtTime(Message.obtain(theHandler, MSG_HANDLER_TOAST, resId, 0, null), SystemClock.uptimeMillis());
	}
	
	public static void toast(String message) {
		theHandler.sendMessageAtTime(Message.obtain(theHandler, MSG_HANDLER_TOAST, 0, 0, message), SystemClock.uptimeMillis());
	}
	
	public static void postToMainThread(Runnable runnable) {
		theHandler.post(runnable);
	}
	
	public static void postToMainThreadDelayed(Runnable runnable, long delayMillis) {
		theHandler.postAtTime(runnable, SystemClock.uptimeMillis() + delayMillis);
	}
	
	public static void sendMessage(MainHandler.Callback callback, int what) {
		theHandler.sendMessageAtTime(Message.obtain(theHandler, what, callback), SystemClock.uptimeMillis());
	}
	
	public static void sendMessage(MainHandler.Callback callback, int what, int arg1, int arg2) {
		theHandler.sendMessageAtTime(Message.obtain(theHandler, what, arg1, arg2, callback), SystemClock.uptimeMillis());
	}
	
	public static void sendMessageDelayed(MainHandler.Callback callback, int what, long delayMillis) {
		theHandler.sendMessageAtTime(Message.obtain(theHandler, what, callback), SystemClock.uptimeMillis() + delayMillis);
	}
	
	public static void sendMessageDelayed(MainHandler.Callback callback, int what, int arg1, int arg2, long delayMillis) {
		theHandler.sendMessageAtTime(Message.obtain(theHandler, what, arg1, arg2, callback), SystemClock.uptimeMillis() + delayMillis);
	}
	
	public static void sendMessageAtTime(MainHandler.Callback callback, int what, int arg1, int arg2, long when) {
		theHandler.sendMessageAtTime(Message.obtain(theHandler, what, arg1, arg2, callback), when);
	}
	
	public static void removeMessages(MainHandler.Callback callback, int what) {
		theHandler.removeMessages(what, callback);
	}
}
