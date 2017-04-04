package com.example.lenser.followme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

        Toolbar toolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

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
                    if(device.getName().contains("HC")){
                        deviceNames.add("  "+device.getName()+"\n"+"  "+device.getAddress());
                    }
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
