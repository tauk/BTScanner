package com.tauk.btscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/*
 * To get this app running
 * Make sure the following:
 * 1. On phones Apps give this app Location permission
 * 2. Make sure Location is ON your phone
 */

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tvData);
    }

    public void doBTScan(View view) {

        //create instance of BluetoothAdapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //do a scan if adapter is enabled
        if (bluetoothAdapter.isEnabled()) {

            //Ask the Android system to start scan for other devices
            //The results of this scan will come as Intents from the Android System
            boolean scanStarted = bluetoothAdapter.startDiscovery();

            textView.setText(textView.getText() + "\n Did scan start:" +scanStarted);
            textView.setText(textView.getText() + "\n Discovering:" +bluetoothAdapter.isDiscovering());

            //register a BroadcastReceiver object for BT device BluetoothDevice.ACTION_FOUND intent
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

            //Create an object of our BroadcastReceiver subclass
            BroadcastReceiver btScanResultReceiver = new BTScanResultReceiver();

            //register our BroadcastReceiver object that will receive the BT Scan results
            //the filter make sure that we receive only BT Scan results from the Android system
            registerReceiver(btScanResultReceiver, filter);
        }
        else {
            Toast.makeText(this, "Bluetooth is disabled on phone", Toast.LENGTH_LONG).show();
            textView.setText(textView.getText() + "\n Discovering:" +bluetoothAdapter.isDiscovering());
        }

    }

    // Create a BroadcastReceiver to receive the scan results in its onReceive() method
    class BTScanResultReceiver extends  BroadcastReceiver {

        //the BT scan results are inside an Intent object
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent as a ParcelableExtra.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address

                textView.setText(textView.getText() + "\n"+deviceName +" " + deviceHardwareAddress);
                Log.d("BT DEVICE FOUND", deviceName +" " + deviceHardwareAddress);
                Toast.makeText(context, deviceName + " " + deviceHardwareAddress,
                                                                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}



