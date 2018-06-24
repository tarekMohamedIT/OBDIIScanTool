package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import adapters.BluetoothAdapter;
import communications.ObdReceiver;
import communications.ObdService;
import enums.ServiceCommand;
import events.OnBroadcastReceivedListener;
import helpers.BaseObdCommand;
import models.FaultCode;

public class FaultCodes extends AppCompatActivity {

    ObdReceiver receiver;
    ArrayList<BaseObdCommand> commands;
    ArrayList arrayList;
    boolean gotFirstCommand = false;
    int currentWorkingIndex,numberOffaults=0;
    CommandThread thread;
    BluetoothAdapter Badapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.r3tr0.obdiiscantool.R.layout.activity_fault_codes);
        RecyclerView recyclerView = findViewById(com.r3tr0.obdiiscantool.R.id.faultcodesRecyclerView);


        arrayList = new ArrayList<String>();
        commands = new ArrayList<>();
        Badapter = new BluetoothAdapter(this, arrayList);

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

                if (ObdService.isReadingRealData) {
                    String data = intent.getStringExtra("data");
                    Log.e("test", "received something!");

                    if (!gotFirstCommand) {// first command yarab :)
                        Badapter.clear();
                        Badapter.add("Fault codes number : " + commands.get(0).performCalculations(data.replace(ObdService.ELM_NAME, "").getBytes()));
                        numberOffaults= (int) commands.get(0).performCalculations(data.replace(ObdService.ELM_NAME, "").getBytes());
                        gotFirstCommand = true;
                    } else {
                        commands.get(1).performCalculations(data.getBytes());
                        gotFirstCommand = false;
                    }
                } else {
                    ArrayList<FaultCode> codes = intent.getParcelableArrayListExtra("data");
                    Badapter.clear();
                    for (FaultCode code : codes) {
                        Badapter.add(code.getId() + " : " + code.getName());
                    }
                }

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(Badapter);

        if (ObdService.isReadingRealData) {
            commands.add(new BaseObdCommand<Integer>(this, "0101") {
                @Override
                public String getName() {
                    return "faults number";
                }

                @Override
                public Integer performCalculations(byte[] bytes) {//e3ml el calculation w return el number ka integer
                    return Integer.parseInt(((char) (bytes[4] & 0xff)) + "" + ((char) (bytes[5] & 0xff)), 16);
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

                    if(numberOffaults==0)
                        return null ;
                    else
                    {
                        int counter = (numberOffaults*4) ;

                        for (int i = 2 ; i<=counter ; i+=4)
                        {
                            int identifyErrorType = Integer.parseInt(String.valueOf(((char) (bytes[i] & 0xff))),16);
                            int FirstByte=Integer.parseInt(String.valueOf(((char) (bytes[i+1] & 0xff))),16);
                            int SecondByte=Integer.parseInt(String.valueOf(((char) (bytes[i+2] & 0xff))),16);
                            int ThirdByte =Integer.parseInt(String.valueOf(((char) (bytes[i+3] & 0xff))),16);
                            if(identifyErrorType==0||identifyErrorType==1||identifyErrorType==2||identifyErrorType==3)
                            {
                                Badapter.add("Fault Code : " + "P"+identifyErrorType+FirstByte+SecondByte+ThirdByte);

                            }else if (identifyErrorType==4||identifyErrorType==5||identifyErrorType==6||identifyErrorType==7)
                            {
                                Badapter.add("Fault Code : " + "C"+identifyErrorType+FirstByte+SecondByte+ThirdByte);
                            }
                            else if (identifyErrorType==8||identifyErrorType==9||identifyErrorType==10||identifyErrorType==11)
                            {
                                Badapter.add("Fault Code : " + "B"+identifyErrorType+FirstByte+SecondByte+ThirdByte);
                            }
                            else if (identifyErrorType==12||identifyErrorType==13||identifyErrorType==14||identifyErrorType==15)
                            {
                                Badapter.add("Fault Code : " + "U"+identifyErrorType+FirstByte+SecondByte+ThirdByte);
                            }
                        }

                    }



                    Log.e("test perform ", Arrays.toString(bytes));
                    Log.e("test perform int", String.valueOf(bytes));
                    return null;
                }
            });

            thread = new CommandThread();
            thread.start();
        } else {
            Intent obdIntent = new Intent(this, ObdService.class);
            obdIntent.putExtra("cmd", ServiceCommand.write);
            obdIntent.putExtra("data", "fault");
            startService(obdIntent);
        }

        Button resetButton = findViewById(com.r3tr0.obdiiscantool.R.id.refreshButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.start();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ObdService.RECEIVER_ACTION));
        if (ObdService.isReadingRealData) {
            Intent obdIntent = new Intent(this, ObdService.class);
            obdIntent.putExtra("cmd", ServiceCommand.startReading);
            startService(obdIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        if (ObdService.isReadingRealData) {
            thread.stopRunning();
            Intent obdIntent = new Intent(this, ObdService.class);
            obdIntent.putExtra("cmd", ServiceCommand.stopReading);
            startService(obdIntent);
        }
    }

    public class CommandThread extends Thread {

        boolean isRunning = true;

        @Override
        public synchronized void start() {
            super.start();
            isRunning = true;
        }

        public void stopRunning() {
            isRunning = false;
        }

        @Override
        public void run() {
            super.run();
            Log.e("test", "thread started");
            while (isRunning) {
                synchronized (FaultCodes.this) {
                    try {
                        commands.get(0).sendCommand();
                        Thread.sleep(400);
                        commands.get(1).sendCommand();
                        Thread.sleep(2000);
                        isRunning = false;

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
