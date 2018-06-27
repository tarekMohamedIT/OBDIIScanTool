package com.r3tr0.obdiiscantool;

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
import communications.CommandsReceiver;
import communications.CommandsService;
import communications.ObdService;
import enums.CommandType;
import events.OnBroadcastReceivedListener;
import helpers.BaseObdCommand;
import models.FaultCode;

public class FaultCodes extends AppCompatActivity {

    CommandsReceiver receiver;
    ArrayList<BaseObdCommand> commands;
    BluetoothAdapter bAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.r3tr0.obdiiscantool.R.layout.activity_fault_codes);
        RecyclerView recyclerView = findViewById(com.r3tr0.obdiiscantool.R.id.faultcodesRecyclerView);


        commands = new ArrayList<>();
        bAdapter = new BluetoothAdapter(this, new ArrayList<String>());

        bAdapter.setOnItemClickListener(new BluetoothAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, View view) {
                Toast.makeText(FaultCodes.this, "testing message", Toast.LENGTH_SHORT).show();
            }
        });

        receiver = new CommandsReceiver(new OnBroadcastReceivedListener() {
            @Override
            public void onBroadcastReceived(Object message) {
                Intent intent = (Intent) message;

                if (intent.getStringExtra("command").equals("Faults number")) {
                    bAdapter.clear();
                    bAdapter.add("Number of errors : " + intent.getIntExtra("data", -1));
                } else if (intent.getStringExtra("command").equals("Fault codes")) {
                    if (!ObdService.isReadingRealData) {
                        ArrayList<FaultCode> faults = intent.getParcelableArrayListExtra("data");
                        if (faults != null)
                            for (FaultCode fault : faults)
                                bAdapter.add(fault.getName());
                    } else {
                        ArrayList<String> faults = intent.getStringArrayListExtra("data");
                        if (faults != null)
                            for (String fault : faults)
                                bAdapter.add(fault);
                    }
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(CommandsService.RECEIVER_ACTION));
        Intent intent = new Intent(this, CommandsService.class);
        intent.putExtra("cmd", CommandType.diagnosis.getValue());
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);

        Intent intent = new Intent(this, CommandsService.class);
        intent.putExtra("cmd", CommandType.none.getValue());
        startService(intent);
    }

}
