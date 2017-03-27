package com.example.lenser.followme;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import BluetoothFunctions.ConnectedBT;

/**
 * Created by lenser on 3/6/17.
 */

public class MyBluetoothService extends AppCompatActivity {

    Button button, btSend;
    TextView tvRcvText;
    EditText etSendText;
    InputStream inputStream;
    OutputStream outputStream;
    Thread workerThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        button = (Button)findViewById(R.id.button);
        btSend = (Button)findViewById(R.id.btSend);
        tvRcvText = (TextView)findViewById(R.id.tvRcvText);
        etSendText = (EditText)findViewById(R.id.etSendText);
        etSendText.setText("");


        ConnectedBT bt = ConnectedBT.getIOStream();
        inputStream = bt.getInputStream();
        outputStream = bt.getOutputStream();
///////////////get input and output stream
//        try {
//            inputStream = MainActivity.mmSocket.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            outputStream = MainActivity.mmSocket.getOutputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////////////
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etSendText.getText().toString();
                ConnectedBT connectedBT = new ConnectedBT();
                connectedBT.write(msg);
            }
        });

        workerThread = new Thread(new Runnable() {
            final byte[] mmBuffer = new byte[1];

            @Override
            public void run() {
                while (true) {
                    try {
                        // Read from the InputStream.
                        inputStream.read(mmBuffer);

                        final String mess = new String(mmBuffer);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvRcvText.setText(mess);
                                if (mess.compareTo("V") == 0){
                                    Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(300);
                                }
                            }
                        });
                        Log.d("Service", "String: "+ mess);
                        mmBuffer[0] = '\0';
                    } catch (IOException e) {
                        Log.d("Service", "Input stream was disconnected", e);
                        break;
                    }
                }
            }
        });
        workerThread.start();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.mmSocket != null){
                    try {
                        MainActivity.mmSocket.close();
                        finish();
                    } catch (IOException e) {
                        Log.d("Service", "Socket could not be closed");
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
