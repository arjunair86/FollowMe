package com.example.lenser.followme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    public static TextView tvRcvText;
    EditText etSendText;
    InputStream inputStream;
    OutputStream outputStream;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        button = (Button)findViewById(R.id.button);
        btSend = (Button)findViewById(R.id.btSend);
        tvRcvText = (TextView)findViewById(R.id.tvRcvText);
        etSendText = (EditText)findViewById(R.id.etSendText);
        etSendText.setText("");

        //////////get IO stream
        try {
            inputStream = MainActivity.mmSocket.getInputStream();
            outputStream = MainActivity.mmSocket.getOutputStream();
        } catch (IOException e) {
            Log.d("Service", "Error getting IO stream");
        }

        final ConnectedBT bt = new ConnectedBT(inputStream, outputStream, MyBluetoothService.this);
        ///////////send message
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etSendText.getText().toString();
                etSendText.setText("");
                bt.write(msg);
            }
        });
        ///////////read message
        bt.start();
        ///////////disconnect device
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bt.disconnect();
            }
        });
    }
}
