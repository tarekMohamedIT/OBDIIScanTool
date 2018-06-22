package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.r3tr0.popups.dialogs.FileDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import communications.ObdReceiver;
import communications.ObdService;
import enums.ServiceCommand;
import enums.ServiceFlag;
import events.OnBroadcastReceivedListener;

public class SelectJsonActivity extends AppCompatActivity {
    Intent obdIntent;
    ObdReceiver receiver;

    Button generalInformationButton;
    Button faultCodesButton;
    Button acceptButton;

    boolean isGeneralInformationAdded;
    boolean isFaultCodesAdded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        obdIntent = new Intent(this, ObdService.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);


        generalInformationButton = findViewById(R.id.generalButton);
        faultCodesButton = findViewById(R.id.faultButton);
        acceptButton = findViewById(R.id.acceptButton);


        generalInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileDialog dialog = new FileDialog(SelectJsonActivity.this);
                dialog.setOnDialogFinishListener(new FileDialog.OnDialogFinishListener() {
                    @Override
                    public void onDismiss(File file) {
                        if (file != null && file.getName().toLowerCase().endsWith(".json")) {
                            obdIntent.putExtra("general", getAllData(file));
                            isGeneralInformationAdded = true;
                        } else if (file == null) {
                            new AlertDialog.Builder(SelectJsonActivity.this)
                                    .setTitle("error").setMessage("You didn't select a file!")
                                    .setNeutralButton("Ok", null).show();
                        } else if (!file.getName().toLowerCase().endsWith(".json")) {
                            new AlertDialog.Builder(SelectJsonActivity.this)
                                    .setTitle("error").setMessage("The selected file is not a JSON file!")
                                    .setNeutralButton("Ok", null).show();
                        }
                    }
                });

                dialog.showDialog();
            }
        });

        faultCodesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileDialog dialog = new FileDialog(SelectJsonActivity.this);
                dialog.setOnDialogFinishListener(new FileDialog.OnDialogFinishListener() {
                    @Override
                    public void onDismiss(File file) {
                        if (file != null && file.getName().toLowerCase().endsWith(".json")) {
                            obdIntent.putExtra("faults", getAllData(file));
                            isFaultCodesAdded = true;
                        } else if (file == null) {
                            new AlertDialog.Builder(SelectJsonActivity.this)
                                    .setTitle("error").setMessage("You didn't select a file!")
                                    .setNeutralButton("Ok", null).show();
                        } else if (!file.getName().toLowerCase().endsWith(".json")) {
                            new AlertDialog.Builder(SelectJsonActivity.this)
                                    .setTitle("error").setMessage("The selected file is not a JSON file!")
                                    .setNeutralButton("Ok", null).show();
                        }
                    }
                });

                dialog.showDialog();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obdIntent.putExtra("cmd", ServiceCommand.initializeJson);
                startService(obdIntent);
                obdIntent.removeExtra("cmd");
            }
        });

        receiver = new ObdReceiver(new OnBroadcastReceivedListener() {
            @Override
            public void onBroadcastReceived(Object message) {
                Intent intent = (Intent) message;

                ServiceFlag flag = (ServiceFlag) intent.getSerializableExtra("status");
                Toast.makeText(SelectJsonActivity.this, flag.name(), Toast.LENGTH_LONG).show();

                if (flag == ServiceFlag.readingJson)
                    finish();
            }
        });


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
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
