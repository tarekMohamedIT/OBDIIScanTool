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
import communications.ObdReceiver;
import communications.ObdService;
import events.OnBroadcastReceivedListener;
import helpers.BaseObdCommand;

public class FaultCodes extends AppCompatActivity {

    ObdReceiver receiver;
    ArrayList<BaseObdCommand> commands;
    ArrayList arrayList;
    boolean gotFirstCommand = false;
    int currentWorkingIndex;
    CommandThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_codes);

        RecyclerView recyclerView = findViewById(R.id.faultcodesRecyclerView);


        arrayList = new ArrayList<String>();
        commands = new ArrayList<>();
        if (!ObdService.isReadingRealData) {
            arrayList.add("1) Suspension problem");
            arrayList.add("2) Error in fuel injection");
            arrayList.add("3) Error in fuel injection");
            arrayList.add("4) Brakes not working well");
        }
        final BluetoothAdapter Badapter = new BluetoothAdapter(this, arrayList);

        Badapter.setOnItemClickListener(new BluetoothAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, View view) {
                Toast.makeText(FaultCodes.this, "testing message", Toast.LENGTH_SHORT).show();
            }
        });

        receiver = new ObdReceiver(new OnBroadcastReceivedListener() {
            @Override
            public void onBroadcastReceived(Object message) {
                Intent intent = (Intent) message;

                String data = intent.getStringExtra("data");

                if (!gotFirstCommand) {// first command yarab :)
                    Badapter.clear();
                    Badapter.add("Fault codes number : " + commands.get(0).performCalculations(data.getBytes()));
                    gotFirstCommand = true;
                } else {
                    commands.get(1).performCalculations(data.getBytes());
                    gotFirstCommand = false;
                }

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(Badapter);

        commands.add(new BaseObdCommand<Integer>(this, "0101") {
            @Override
            public String getName() {
                return "faults number";//khkjghkgl
            }

            @Override
            public Integer performCalculations(byte[] bytes) {//e3ml el calculation w return el number ka integer
                return null;
            }
        });

        commands.add(new BaseObdCommand(this, "03") {
            @Override
            public String getName() {
                return "faults";//bla2
            }

            @Override
            public Object performCalculations(byte[] bytes) {//hena return null bas kol error e3mlo add fel adapter
                //Badapter.add("hena el error b3d ma t3mlo translate");
                //int number = Integer.parseInt(bytes[0] + "" + bytes[1], 16);
                return null;
            }
        });

        thread = new CommandThread();
        thread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ObdService.RECEIVER_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
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
                synchronized (FaultCodes.this) {
                    commands.get(0).sendCommand();
                    commands.get(1).sendCommand();
                }
            }
        }
    }


}
