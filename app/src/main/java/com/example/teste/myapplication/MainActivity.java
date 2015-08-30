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
    Vibrator vibrator; //define a variavel que sera usada para configurar o servico de vibracao do celular
    // *ignore String[] morse = new String[50];
    int num=0,x=0;//variavei para guardar a letra que esta contando e o numero de erros
    BluetoothConnectionManager bt; //define a variavel que sera usada para configurar o bluetooth
    Button Disconect;
    OutputStream outputStream;
    boolean Conectado=false;
    @Override
    public void onBluetoothPairingStarted(BluetoothConnectionManager manager, String description, String address) {
//comeca o pareamento bluetooth
    }
    @Override
    public void onBluetoothPairingFinished(BluetoothConnectionManager manager, String description, String address, boolean success) {
//Ve quando o pareamento do dispositivo cluetooth acabou
    }
    @Override
    public void onBluetoothCancelled(BluetoothConnectionManager manager) {
//Ve se o processo do bluetooth ta cancelado
    }
    @Override
    public void onBluetoothConnected(BluetoothConnectionManager manager) {
        Conectado = true;
        Disconect.setText("Conectado!");//Ver se o bluetooth esta conectado
        outputStream = bt.getOutputStream();
    }
    @Override
    public void onBluetoothError(BluetoothConnectionManager manager, int error) {
//Ver se ha algum erro no bluetooth
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);//define uma variavel para usar o sistema de vibracao
        Vt = (TextView) findViewById(R.id.Vt); // define as variaveis como textview, para vizualizar o texto escrito no aplicativo
        Vp = (TextView) findViewById(R.id.Vp);
        Il = (TextView) findViewById(R.id.Il);
        Ip = (TextView) findViewById(R.id.Ip);
// *ignorar ID = (TextView) findViewById(R.id.ID);
        texto = (TextView) findViewById(R.id.texto);
        Disconect = (Button) findViewById(R.id.Disconect);//define uma variavel para ser usada como botao
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
    public void ponto(View v) throws InterruptedException {
        vibrator.cancel(); //para a ultima execucao do servico de vibracao
        vibrator.vibrate(Integer.parseInt(Vp.getText().toString())); //mesma cosia de cima para a variavel byte "dois" //para por um tempo de acordo com a variavel Vp definida
        tos('.', 'A', 'A', 'A'); //envia para "tos" a letra 'e' em morse
    }
    public void traco(View v) throws InterruptedException {
        vibrator.cancel();//para a ultima execucao do servico de vibracao
        vibrator.vibrate(Integer.parseInt(Vt.getText().toString())); //para por um tempo de acordo com a variavel Vt definida
        tos('-', 'A', 'A', 'A');//envia para "tos" a letra 't' em morse
    }
    public void Desc(View v){
        outputStream = null;
        if (Conectado) {
            Conectado = false;
            if (bt != null) {
                bt.destroy();
            }
            Disconect.setText("Conectar");//Troca o texto do botao para Conectar caso necessario
        } else {
            Conectado = false;
            if (bt != null) {
                bt.destroy();
            }
            bt = new BluetoothConnectionManager(this, this);
            bt.showDialogAndScan();
            Disconect.setText("Conectando...");//Troca o texto do botao para Conectando... caso necessario
        }
    }
    public void morse(View v) throws InterruptedException {
        int num2 = texto.length();
//String[] morse = new String[num2];
        while (num <= num2-1) {
            switch(texto.getText().charAt(num)) { //Ve qual palavra esta escrita, separa em letras e trauz para morse ja enviando para o bluetooth por meio da funcao "tos"
                case' ':
                    TimeUnit.MILLISECONDS.sleep(Integer.parseInt(Ip.getText().toString())); //mesma cosia de cima para a variavel byte "dois"
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'A':
                case 'a': //Envia a letra "a" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','-','A','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'B':
                case 'b': //Envia a letra "b" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = "-...";
                    tos('-','.','.','.');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'C':
                case 'c': //Envia a letra "c" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','.','-','.');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'D':
                case 'd'://Envia a letra "d" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','.','.','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'E':
                case 'e'://Envia a letra "e" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','A','A','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'F':
                case 'f'://Envia a letra "f" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','.','-','.');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'G':
                case 'g'://Envia a letra "g" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','-','.','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'H':
                case 'h'://Envia a letra "h" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','.','.','.');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'I':
                case 'i'://Envia a letra "i" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','.','A','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'J':
                case 'j'://Envia a letra "j" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','-','-','-');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'K':
                case 'k'://Envia a letra "k" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','.','-','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'L':
                case 'l'://Envia a letra "l" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','-','.','.');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'M':
                case 'm'://Envia a letra "m" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','-','A','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'N':
                case 'n'://Envia a letra "n" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','.','A','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'O':
                case 'o'://Envia a letra "o" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','-','-','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'P':
                case 'p'://Envia a letra "p" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','-','-','.');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'Q':
                case 'q'://Envia a letra "q" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','-','.','-');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'R':
                case 'r'://Envia a letra "r" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','-','.','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'S':
                case 's'://Envia a letra "s" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','.','.','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'T':
                case 't'://Envia a letra "t" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','A','A','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'U':
                case 'u'://Envia a letra "u" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','.','-','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'V':
                case 'v'://Envia a letra "v" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','.','.','-');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'W':
                case 'w'://Envia a letra "w" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('.','-','-','A');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'X':
                case 'x'://Envia a letra "x" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','.','.','-');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'Y':
                case 'y'://Envia a letra "y" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','.','-','-');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                case 'Z':
                case 'z'://Envia a letra "z" e variantes(letra maiuscula/com acento) para a funcao tos
// morse[num] = ".-";
                    tos('-','-','.','.');
                    num++; //vai para a proxima letra
                    break; //para a execucao do switch/case
                default: //Quando a letra recebida nao e reconhecida
                    x++;//adiciona na contagem de erros
                    num++; //pula a letra
                    break; //para a execucao do switch/case
            }
            TimeUnit.MILLISECONDS.sleep(Integer.parseInt(Il.getText().toString())); //mesma cosia de cima para a variavel byte "dois"
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
            byte[] buffer = new byte[] { (byte)um, (byte)dois, (byte)tres, (byte)quatro };//verifica quais variaveis rescebeu
            try {
                outputStream.write(buffer);//tenta enviar os bytes por bluetooth para o celular
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        if (um=='-') vibra(Integer.parseInt(Vt.getText().toString())); //mesma cosia de cima para a variavel byte "dois" //caso o byte que receba for '-' faca vibrar por um tempo(Vt.getText().toString(),variavel que guarda o tempo transformada em texto)
        else if(um=='A'){} else vibra(Integer.parseInt(Vp.getText().toString())); // caso for '.'(else) por menos tempo(Vp.getText().toString(),variavel que guarda o tempo transformada em texto) e 'A' para nao fazer nada
        if (dois=='-') vibra(Integer.parseInt(Vt.getText().toString())); //mesma cosia de cima para a variavel byte "dois"
        else if(dois=='A'){} else vibra(Integer.parseInt(Vp.getText().toString())); //mesma cosia de cima para a variavel byte "dois"
        if (tres=='-') vibra(Integer.parseInt(Vt.getText().toString())); //mesma cosia de cima para a variavel byte "tres"
        else if(tres=='A'){} else vibra(Integer.parseInt(Vp.getText().toString())); //mesma cosia de cima para a variavel byte "tres"
        if (quatro=='-') vibra(Integer.parseInt(Vt.getText().toString())); //mesma cosia de cima para a variavel byte "quatro"
        else if(quatro=='A'){} else vibra(Integer.parseInt(Vp.getText().toString())); //mesma cosia de cima para a variavel byte "quatro"
// Toast toast = Toast.makeText(getApplicationContext(), mr, Toast.LENGTH_SHORT);
// toast.show();
// TimeUnit.MILLISECONDS.sleep(10000);
    }
    public void vibra(int x)throws InterruptedException{
        if (Integer.parseInt(Vt.getText().toString())<=Integer.parseInt(Vp.getText().toString())){
            Toast.makeText(getApplicationContext(),"Traco<Ponto",Toast.LENGTH_SHORT).show(); //caso o traco for menor que o ponto da um erro na tela
        }else
            vibrator.vibrate(x);TimeUnit.MILLISECONDS.sleep(Integer.parseInt(Il.getText().toString())); //vibra o celular e faz um pause enquanto o celular vibra
    }
}