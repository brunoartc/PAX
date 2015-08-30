package com.example.teste.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements BluetoothConnectionManager.BluetoothObserver {
    TextView Vt,Vp,Il,Ip,ID,texto;
    Vibrator vibrator;
 //   String[] morse = new String[50];
    int num=0,x=0;
    BluetoothConnectionManager bt;
    Button Disconect;
    OutputStream outputStream;
    boolean Conectado=false;


    @Override
    public void onBluetoothPairingStarted(BluetoothConnectionManager manager, String description, String address) {

    }

    @Override
    public void onBluetoothPairingFinished(BluetoothConnectionManager manager, String description, String address, boolean success) {

    }

    @Override
    public void onBluetoothCancelled(BluetoothConnectionManager manager) {

    }

    @Override
    public void onBluetoothConnected(BluetoothConnectionManager manager) {
        Conectado = true;
        Disconect.setText("Conectado!");
        outputStream = bt.getOutputStream();
    }

    @Override
    public void onBluetoothError(BluetoothConnectionManager manager, int error) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        Vt = (TextView) findViewById(R.id.Vt);
        Vp = (TextView) findViewById(R.id.Vp);
        Il = (TextView) findViewById(R.id.Il);
        Ip = (TextView) findViewById(R.id.Ip);
        ID = (TextView) findViewById(R.id.ID);
        texto = (TextView) findViewById(R.id.texto);
        Disconect = (Button) findViewById(R.id.Disconect);
    }

    @Override
    protected void onDestroy() {
        outputStream = null;
        Conectado = false;
        if (bt != null) {
            bt.destroy();
        }
        Disconect.setText("Conectar");
        super.onDestroy();
    }

    public void ponto(View v){
        vibrator.cancel();
        vibrator.vibrate(Integer.parseInt(Vp.getText().toString()));
    }
    public void traco(View v){
        vibrator.cancel();
        vibrator.vibrate(Integer.parseInt(Vt.getText().toString()));
    }
    public void Desc(View v){
        outputStream = null;
        if (Conectado) {
            Conectado = false;
            if (bt != null) {
                bt.destroy();
            }
            Disconect.setText("Conectar");
        } else {
            Conectado = false;
            if (bt != null) {
                bt.destroy();
            }
            bt = new BluetoothConnectionManager(this, this);
            bt.showDialogAndScan();
            Disconect.setText("Conectando...");
        }
    }
    public void morse(View v) throws InterruptedException {
        int num2 = texto.length();
        //String[] morse = new String[num2];
        while (num <= num2-1) {
            switch(texto.getText().charAt(num)) {
                case' ':
                    TimeUnit.MILLISECONDS.sleep(Integer.parseInt(Ip.getText().toString()));
                    num++;
                    break;
                case 'a':
                //   morse[num] = ".-";
                    tos('-','.','A','A');
                    num++;
                    break;
                case 'b':
                 //   morse[num] = "-...";
                    tos('-','.','.','.');
                    num++;
                    break;
                case 'c':
                  //  morse[num] = ".-";
                    tos('-','.','-','.');
                    num++;
                    break;
                case 'd':
                   // morse[num] = ".-";
                    tos('-','.','.','A');
                    num++;
                    break;
                case 'e':
                  //  morse[num] = ".-";
                    tos('.','A','A','A');
                    num++;
                    break;
                case 'f':
                 //   morse[num] = ".-";
                    tos('.','.','-','.');
                    num++;
                    break;
                case 'g':
                  //  morse[num] = ".-";
                    tos('-','-','.','A');
                    num++;
                    break;
                case 'h':
                   // morse[num] = ".-";
                    tos('.','.','.','.');
                    num++;
                    break;
                case 'i':
                   // morse[num] = ".-";
                    tos('.','.','A','A');
                    num++;
                    break;
                case 'j':
                  //  morse[num] = ".-";
                    tos('.','-','-','-');
                    num++;
                    break;
                case 'k':
                   // morse[num] = ".-";
                    tos('-','.','-','A');
                    num++;
                    break;
                case 'l':
                   // morse[num] = ".-";
                    tos('.','-','.','.');
                    num++;
                    break;
                case 'm':
                  //  morse[num] = ".-";
                    tos('-','-','A','A');
                    num++;
                    break;
                case 'n':
                  //  morse[num] = ".-";
                    tos('-','.','A','A');
                    num++;
                    break;
                case 'o':
                //    morse[num] = ".-";
                    tos('-','-','-','A');
                    num++;
                    break;
                case 'p':
                 //   morse[num] = ".-";
                    tos('.','-','-','.');
                    num++;
                    break;
                case 'q':
                //    morse[num] = ".-";
                    tos('-','-','.','-');
                    num++;
                    break;
                case 'r':
                 //   morse[num] = ".-";
                    tos('.','-','.','A');
                    num++;
                    break;
                case 's':
                //   morse[num] = ".-";
                    tos('.','.','.','A');
                    num++;
                    break;
                case 't':
               //     morse[num] = ".-";
                    tos('-','A','A','A');
                    num++;
                    break;
                case 'u':
                //    morse[num] = ".-";
                    tos('.','.','-','A');
                    num++;
                    break;
                case 'V':
                case 'v':
                //    morse[num] = ".-";
                    tos('.','.','.','-');
                    num++;
                    break;
                case 'w':
               //     morse[num] = ".-";
                    tos('.','-','-','A');
                    num++;
                    break;
                case 'x':
               //     morse[num] = ".-";
                    tos('-','.','.','-');
                    num++;
                    break;
                case 'y':
               //     morse[num] = ".-";
                    tos('-','.','-','-');
                    num++;
                    break;
                case 'z':
               //     morse[num] = ".-";
                    tos('-','-','.','.');
                    num++;
                    break;
                default:
                    x++;
                    num++;
                    break;
            }
        }
        if(x!=0){
            Toast toast = Toast.makeText(getApplicationContext(),"Erro "+String.valueOf(x)+" caractere(s) invalido(s)",Toast.LENGTH_SHORT);
            toast.show();
            x=0;
        }
    num=0;
    }
    public void tos(char um,char dois, char tres, char quatro) throws InterruptedException {
        if (outputStream != null) {
            byte[] buffer = new byte[] { (byte)um, (byte)dois, (byte)tres, (byte)quatro };
            try {
                outputStream.write(buffer);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        if (um=='-') vibra(Integer.parseInt(Vt.getText().toString()));
        else if(um=='A'){} else vibra(Integer.parseInt(Vp.getText().toString()));
        if (dois=='-') vibra(Integer.parseInt(Vt.getText().toString()));
        else if(dois=='A'){} else vibra(Integer.parseInt(Vp.getText().toString()));
        if (tres=='-') vibra(Integer.parseInt(Vt.getText().toString()));
        else if(tres=='A'){} else vibra(Integer.parseInt(Vp.getText().toString()));
        if (quatro=='-') vibra(Integer.parseInt(Vt.getText().toString()));
        else if(quatro=='A'){} else vibra(Integer.parseInt(Vp.getText().toString()));
    //    Toast toast = Toast.makeText(getApplicationContext(), mr, Toast.LENGTH_SHORT);
  //      toast.show();
//        TimeUnit.MILLISECONDS.sleep(10000);
    }
    public void vibra(int x)throws InterruptedException{
        if (Integer.parseInt(Il.getText().toString())<Integer.parseInt(Vt.getText().toString())){
            Toast.makeText(getApplicationContext(),"Erro Letr<T",Toast.LENGTH_SHORT).show();
        }else
        vibrator.vibrate(x);TimeUnit.MILLISECONDS.sleep(Integer.parseInt(Il.getText().toString()));
    }
    public void Blu(char um,char dois,char tres,char quatro) throws IOException {
        /*if (Blue.isEnabled() && !Conectado) {
            try {
                if (btSocket == null || !Conectado) {
                    BluetoothDevice dispositivo = Blue.getRemoteDevice(ID.getText().toString());
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }catch (IOException e){Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_SHORT).show();}
            Conectado = true;
        } else if(btSocket!=null) {
            btSocket.getOutputStream().write(um);
            btSocket.getOutputStream().write(dois);
            btSocket.getOutputStream().write(tres);
            btSocket.getOutputStream().write(quatro);
        }*/
    }
}
