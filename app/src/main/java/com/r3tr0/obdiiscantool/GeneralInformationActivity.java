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
import java.util.Arrays;
import java.lang.*;
import adapters.GeneralInformationAdapter;
import communications.ObdReceiver;
import communications.ObdService;
import enums.ServiceCommand;
import events.OnBroadcastReceivedListener;

public class GeneralInformationActivity extends AppCompatActivity {

    GeneralInformationAdapter generalInformationAdapter;
    ObdReceiver receiver;
    private String data ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);
        RecyclerView recyclerView =findViewById(R.id.GeneralInformation);

        ArrayList<models.GeneralInformation> arrayList = new ArrayList<>();
        generalInformationAdapter = new GeneralInformationAdapter(this, arrayList);

        receiver = new ObdReceiver(new OnBroadcastReceivedListener() {
            @Override
            public void onBroadcastReceived(Object message) {
                Intent intent = (Intent) message;
                data = intent.getStringExtra("data");
                byte[] bytes= data.getBytes();
                int number=new Byte(bytes[3]).intValue();


                generalInformationAdapter.replaceAll(intent.<models.GeneralInformation>getParcelableArrayListExtra("data"));
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
        registerReceiver(receiver, new IntentFilter(ObdService.RECEIVER_ACTION));
        Intent intent = new Intent(this, ObdService.class);
        intent.putExtra("cmd", ServiceCommand.write);
        intent.putExtra("data", "general");
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
