package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import adapters.GeneralInformationAdapter;
import communications.ObdReceiver;
import communications.ObdService;
import enums.ServiceCommand;
import enums.ServiceFlag;
import events.OnBroadcastReceivedListener;

public class GeneralInformationActivity extends AppCompatActivity {

    GeneralInformationAdapter generalInformationAdapter;
    ObdReceiver receiver;
    ServiceFlag flag;

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

                if (!ObdService.isReadingRealData)
                    generalInformationAdapter.replaceAll(intent.<models.GeneralInformation>getParcelableArrayListExtra("data"));
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(generalInformationAdapter);

        if (!ObdService.isReadingRealData)
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
        if (!ObdService.isReadingRealData) {
            intent.putExtra("cmd", ServiceCommand.write);
            intent.putExtra("data", "general");
            startService(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

}
