package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import adapters.GeneralInformationAdapter;
import communications.CommandsReceiver;
import communications.CommandsService;
import enums.CommandType;
import events.OnBroadcastReceivedListener;
import helpers.BaseObdCommand;
import models.GeneralInformation;

public class GeneralInformationActivity extends AppCompatActivity {

    GeneralInformationAdapter generalInformationAdapter;
    CommandsReceiver receiver;
    ArrayList<BaseObdCommand> commands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.r3tr0.obdiiscantool.R.layout.activity_general_information);
        RecyclerView recyclerView = findViewById(com.r3tr0.obdiiscantool.R.id.GeneralInformation);
        commands = new ArrayList<>();

        ArrayList<models.GeneralInformation> arrayList = new ArrayList<>();
        generalInformationAdapter = new GeneralInformationAdapter(this, arrayList);

        receiver = new CommandsReceiver(new OnBroadcastReceivedListener() {
            @Override
            public void onBroadcastReceived(Object message) {
                Intent intent = (Intent) message;
                boolean found = false;
                GeneralInformation item = (GeneralInformation) intent.getExtras().get("data");

                for (int i = 0; i < generalInformationAdapter.getGeneralInformationArrayList().size(); i++) {
                    if (generalInformationAdapter.getGeneralInformationArrayList().get(i).getHeadline().equals(item.getHeadline().trim().replace(" ", "_"))) {
                        found = true;
                        generalInformationAdapter.modifyItemAt(i, item);
                        break;
                    }
                }

                if (!found) {
                    generalInformationAdapter.add(item);
                }
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(generalInformationAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(CommandsService.RECEIVER_ACTION));
        Intent intent = new Intent(this, CommandsService.class);
        intent.putExtra("cmd", CommandType.general.getValue());
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
