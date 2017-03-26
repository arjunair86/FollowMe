package com.example.lenser.followme;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by lenser on 3/23617.
 */

public class GetPairedDevices {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Set<BluetoothDevice> bluetoothDevices;
    ArrayList<BluetoothDevice> devices;

    public ArrayList<BluetoothDevice> showPairedDevices() {
        devices = new ArrayList<>();
        bluetoothDevices = bluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device: bluetoothDevices){
            devices.add(device);
        }

        return devices;
    }
}
