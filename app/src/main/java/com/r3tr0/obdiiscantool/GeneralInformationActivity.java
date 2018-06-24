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
import helpers.BaseObdCommand;
import models.GeneralInformation;

public class GeneralInformationActivity extends AppCompatActivity {

    GeneralInformationAdapter generalInformationAdapter;
    ObdReceiver receiver;
    ServiceFlag flag;
    CommandThread thread;

    ArrayList<BaseObdCommand> commands;
    int currentWorkingIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.r3tr0.obdiiscantool.R.layout.activity_general_information);
        RecyclerView recyclerView = findViewById(com.r3tr0.obdiiscantool.R.id.GeneralInformation);
        commands = new ArrayList<>();

        ArrayList<models.GeneralInformation> arrayList = new ArrayList<>();
        generalInformationAdapter = new GeneralInformationAdapter(this, arrayList);

        receiver = new ObdReceiver(new OnBroadcastReceivedListener() {
            @Override
            public void onBroadcastReceived(Object message) {
                Intent intent = (Intent) message;

                if (!ObdService.isReadingRealData)
                    generalInformationAdapter.replaceAll(intent.<models.GeneralInformation>getParcelableArrayListExtra("data"));

                else {
                    if (currentWorkingIndex != -1) {

                        GeneralInformation item = generalInformationAdapter.getGeneralInformationArrayList().get(currentWorkingIndex);
                        item.setUsedValue(
                                (Float) commands.get(currentWorkingIndex).performCalculations(intent.getStringExtra("data").getBytes()));
                        generalInformationAdapter.notifyItemChanged(currentWorkingIndex);
                    }
                }
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

        else {
            /** NEW COMMANDS ADDING */

            generalInformationAdapter.add(new GeneralInformation(0, "Speed", new ArrayList<Float>()));
            commands.add(new BaseObdCommand<Integer>(this, "*Command here*") {
                @Override
                public String getName() {
                    return "speed";
                }

                @Override
                public Integer performCalculations(byte[] bytes) {
                    return null;
                }
            });

            /** END */

            thread = new CommandThread();
            thread.start();
        }

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
        if (thread != null && thread.isrunning)
            thread.stopRunning();
    }

    public class CommandThread extends Thread {

        boolean isrunning = true;

        @Override
        public synchronized void start() {
            super.start();
            isrunning = true;
        }

        public void stopRunning() {
            isrunning = false;
        }

        @Override
        public void run() {
            super.run();

            while (isrunning) {
                for (int i = 0; i < commands.size(); i++) {
                    synchronized (GeneralInformationActivity.this) {
                        currentWorkingIndex = i;
                        commands.get(i).sendCommand();
                    }
                }
            }
        }
    }

}
