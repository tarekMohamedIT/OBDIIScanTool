package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import adapters.RecyclerListAdapter;
import communications.ObdReceiver;
import communications.ObdService;
import dialogs.MethodSelectionDialog;
import enums.RecyclerListAdapterMode;
import enums.ServiceCommand;
import enums.ServiceFlag;
import events.OnBroadcastReceivedListener;
import events.OnItemClickListener;

public class MainActivity extends AppCompatActivity {

    RecyclerListAdapter adapter;
    RecyclerView recyclerView;
    ObdReceiver receiver;
    Intent obdIntent;
    ServiceFlag flag;
    MethodSelectionDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.r3tr0.obdiiscantool.R.layout.activity_main);

        obdIntent = new Intent(this, ObdService.class);
        receiver = new ObdReceiver(new OnBroadcastReceivedListener() {
            @Override
            public void onBroadcastReceived(Object message) {
                Intent intent = (Intent) message;

                flag = ((ServiceFlag) intent.getSerializableExtra("status"));

                if (flag != null) {
                    adapter.changeSubTitle(0, flag.name());
                }
            }
        });

        ArrayList<RecyclerListAdapter.ListItem> listItems = new ArrayList<>();
        listItems.add(new RecyclerListAdapter.ListItem("Bluetooth", "Not connected", RecyclerListAdapterMode.fullWithTitleAndSubTitleMode, com.r3tr0.obdiiscantool.R.drawable.btnew, Color.parseColor("#ff22569f")));
        listItems.add(new RecyclerListAdapter.ListItem("change input", "", RecyclerListAdapterMode.partMode, com.r3tr0.obdiiscantool.R.drawable.input_method));
        listItems.add(new RecyclerListAdapter.ListItem("general info", "", RecyclerListAdapterMode.partMode, com.r3tr0.obdiiscantool.R.drawable.general_info));
        listItems.add(new RecyclerListAdapter.ListItem("fault codes", "", RecyclerListAdapterMode.partMode, com.r3tr0.obdiiscantool.R.drawable.diagnosis));
        listItems.add(new RecyclerListAdapter.ListItem("record trips", "", RecyclerListAdapterMode.partMode, com.r3tr0.obdiiscantool.R.drawable.trip));
        listItems.add(new RecyclerListAdapter.ListItem("Emergency", "", RecyclerListAdapterMode.partMode, com.r3tr0.obdiiscantool.R.drawable.emergency));
        listItems.add(new RecyclerListAdapter.ListItem("Exit", "", RecyclerListAdapterMode.partMode, com.r3tr0.obdiiscantool.R.drawable.exit));

        adapter = new RecyclerListAdapter(this, listItems);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position){
                    case 0 :
                        if (adapter.getItem(position).getResImageID() == com.r3tr0.obdiiscantool.R.drawable.btnew)
                            startActivity(new Intent(MainActivity.this, BluetoothActivity.class));

                        else {
                            startActivity(new Intent(MainActivity.this, SelectJsonActivity.class));
                        }
                        break;

                    case 1 :
                        dialog.showDialog();
                        break;

                    case 2 :
                        if (flag == ServiceFlag.disconnected
                                || flag == ServiceFlag.connectionFailed
                                || flag == ServiceFlag.invalidFaultCodes
                                || flag == ServiceFlag.invalidGeneralInformation) {

                            Toast.makeText(MainActivity.this, "You must select a valid method", Toast.LENGTH_LONG).show();
                        } else
                            startActivity(new Intent(MainActivity.this, GeneralInformationActivity.class));
                        break;
                    case 3:
                        if (flag == ServiceFlag.disconnected
                                || flag == ServiceFlag.connectionFailed
                                || flag == ServiceFlag.invalidFaultCodes
                                || flag == ServiceFlag.invalidGeneralInformation) {

                            Toast.makeText(MainActivity.this, "You must select a valid method", Toast.LENGTH_LONG).show();
                        } else
                            startActivity(new Intent(MainActivity.this, FaultCodes.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, TripActivity.class));
                        break;

                    case 5:
                        startActivity(new Intent(MainActivity.this, EmergencyActivity.class));
                        break;


                    case 6:
                        finish();
                        break;
                }
            }
        });

        dialog = new MethodSelectionDialog(MainActivity.this, new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (position == 0) {
                    adapter.changeImage(0, com.r3tr0.obdiiscantool.R.drawable.jsonew);
                    adapter.changeColor(0, Color.parseColor("#ff634016"));
                    adapter.changeTitle(0, "Json files");
                } else {
                    adapter.changeImage(0, com.r3tr0.obdiiscantool.R.drawable.btnew);
                    adapter.changeColor(0, Color.parseColor("#ff22569f"));
                    adapter.changeTitle(0, "Bluetooth");
                }

                dialog.dismiss();
            }
        });

        recyclerView = findViewById(com.r3tr0.obdiiscantool.R.id.devicesRecyclerView);

        GridLayoutManager manager = new GridLayoutManager(this, LinearLayoutManager.VERTICAL);
        manager.setSpanCount(2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == RecyclerListAdapterMode.fullMode.getValue()
                        || adapter.getItemViewType(position) == RecyclerListAdapterMode.fullWithTitleAndSubTitleMode.getValue())
                    return 2;

                else return 1;
            }
        });


        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public String getAllData(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            StringBuilder wholeData = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
                wholeData.append(line).append("\n");

            return wholeData.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ObdService.RECEIVER_ACTION));

        obdIntent.putExtra("cmd", ServiceCommand.getStatus);
        startService(obdIntent);
        obdIntent.removeExtra("cmd");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
