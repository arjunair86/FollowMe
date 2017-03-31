package com.example.lenser.followme;

import android.animation.Animator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import BluetoothFunctions.ConnectBT;
import BluetoothFunctions.GetPairedDevices;

public class MainActivity extends AppCompatActivity {

    public static BluetoothSocket mmSocket;
    BluetoothAdapter bluetoothAdapter;
    Button btPairedDevices;
    ListView lvPairedDevices;
    ArrayAdapter<String> adapter;
    ArrayList<BluetoothDevice> bluetoothDevices;
    ArrayList<String> deviceNames;

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
                ConnectBT connectBT = new ConnectBT(MainActivity.this, bluetoothDevices.get(position));
                connectBT.execute();
            }
        });
    }
}
