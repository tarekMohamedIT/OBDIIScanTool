package com.r3tr0.obdiiscantool;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import adapters.BluetoothAdapter;
import communications.ObdReceiver;
import communications.ObdService;
import enums.ServiceCommand;
import enums.ServiceFlag;
import events.OnBroadcastReceivedListener;

public class BluetoothActivity extends AppCompatActivity {

    ObdReceiver receiver;
    Intent obdIntent;
    ArrayList<BluetoothDevice> devices;
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        RecyclerView recyclerView =findViewById(R.id.bluetoothRecyclerView);

        bluetoothAdapter = new BluetoothAdapter(this, new ArrayList<String>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bluetoothAdapter);

        receiver = new ObdReceiver(new OnBroadcastReceivedListener() {
            @Override
            public void onBroadcastReceived(Object message) {
                Intent intent = (Intent) message;

                ArrayList<BluetoothDevice> devicesTmp = intent.getParcelableArrayListExtra("devices");
                ServiceFlag flag = (ServiceFlag) intent.getSerializableExtra("status");

                if (devicesTmp != null) {
                    devices = devicesTmp;
                    ArrayList<String> strings = new ArrayList<>();
                    for (BluetoothDevice device : devices)
                        strings.add(device.getName());
                    bluetoothAdapter.clear();
                    bluetoothAdapter.addAll(strings);
                }

                if (flag != null) {
                    Toast.makeText(BluetoothActivity.this, flag.name(), Toast.LENGTH_LONG).show();
                    if (flag == ServiceFlag.connected)
                        finish();
                }


            }
        });
        obdIntent = new Intent(this, ObdService.class);

        bluetoothAdapter.setOnItemClickListener(new BluetoothAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, View view) {
                obdIntent.putExtra("cmd", ServiceCommand.initializeBluetooth);
                obdIntent.putExtra("device", devices.get(position));
                startService(obdIntent);
                obdIntent.removeExtra("cmd");
                obdIntent.removeExtra("device");
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ObdService.RECEIVER_ACTION));
        obdIntent.putExtra("cmd", ServiceCommand.getPairedDevices);
        startService(obdIntent);
        obdIntent.removeExtra("cmd");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
