package com.example.lenser.followme;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static BluetoothSocket mmSocket;
    BluetoothAdapter bluetoothAdapter;
    Button btPairedDevices;
    ListView lvPairedDevices;
    ArrayAdapter<String> adapter;
    ArrayList<BluetoothDevice> bluetoothDevices;
    ArrayList<String> deviceNames;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btPairedDevices = (Button)findViewById(R.id.btPairedDevices);
        lvPairedDevices = (ListView)findViewById(R.id.lvPairedDevices);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevices = new ArrayList<BluetoothDevice>();
        deviceNames = new ArrayList<>();

        if(!bluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
        }
//////////////////////////////LIST PAIRED DEVICES///////////////////////////////
        btPairedDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!deviceNames.isEmpty()){
                    deviceNames.clear();
                }
                GetPairedDevices getPairedDevices = new GetPairedDevices();
                bluetoothDevices = getPairedDevices.showPairedDevices();
                deviceNames = new ArrayList<String>();
                for(BluetoothDevice device: bluetoothDevices){
                    deviceNames.add(device.getName()+"\n"+device.getAddress());
                }
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_black, deviceNames);
                lvPairedDevices.setAdapter(adapter);
            }
        });
////////////////////////////CONNECT BT DEVICE////////////////////////////////
        lvPairedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConnectBT connectBT = new ConnectBT(bluetoothDevices.get(position));
                connectBT.execute();
            }
        });
    }

    public class ConnectBT extends AsyncTask<Void, Void, Void>{
        BluetoothDevice bluetoothDevice;
        private boolean connectSuccess = false;

        public ConnectBT(BluetoothDevice bluetoothDevice) {
            this.bluetoothDevice = bluetoothDevice;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this, "Connecting...", "Please Wait...");
        }

        @Override
        protected Void doInBackground(Void... params) {
            final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            BluetoothSocket tmp = null;
            try {
                tmp = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                mmSocket = tmp;
                mmSocket.connect();
                connectSuccess = true;
            } catch (IOException e) {
                Log.d("Main", "doInBackground failed");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(!connectSuccess){
                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, MyBluetoothService.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
