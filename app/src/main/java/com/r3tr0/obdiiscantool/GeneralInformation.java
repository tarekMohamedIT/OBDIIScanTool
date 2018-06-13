package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import adapters.GeneralInformationAdapter;
import communications.ObdReceiver;
import communications.ObdService;
import events.OnBroadcastReceivedListener;
import models.GeneralInformationModel;

public class GeneralInformation extends AppCompatActivity {

    GeneralInformationAdapter generalInformationAdapter;
    ObdReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);
        RecyclerView recyclerView =findViewById(R.id.GeneralInformation);

        ArrayList<GeneralInformationModel> arrayList = new ArrayList<>();
        generalInformationAdapter = new GeneralInformationAdapter(this, arrayList);

        receiver = new ObdReceiver(new OnBroadcastReceivedListener() {
            @Override
            public void onBroadcastReceived(Object message) {
                Intent intent = (Intent) message;
                if (intent.getStringExtra("type").equals("general")) {
                    generalInformationAdapter.replaceAll(intent.<GeneralInformationModel>getParcelableArrayListExtra("data"));
                }
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(generalInformationAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                generalInformationAdapter.modifyItems();
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter("com.r3tr0.obdiiscantool.Obd"));
        Intent intent = new Intent(this, ObdService.class);
        intent.putExtra("cmd", ObdService.COMMAND_READ_ALL);
        intent.putExtra("type", ObdService.TYPE_GENERAL_INFO);
        startService(intent);
        Log.e("test", "resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        Log.e("test", "paused");
    }
}
