package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.r3tr0.popups.dialogs.FileDialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import communications.ObdService;

public class Select_json extends AppCompatActivity {
Intent obdIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        obdIntent = new Intent(this, ObdService.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Button MyButton = findViewById(R.id.selectss);

        MyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileDialog dialog = new FileDialog(Select_json.this);
                dialog.setOnDialogFinishListener(new FileDialog.OnDialogFinishListener() {
                    @Override
                    public void onDismiss(File file) {
                        if (file != null && file.getName().toLowerCase().endsWith(".json")) {
                            obdIntent.putExtra("cmd", ObdService.COMMAND_JSON);
                            obdIntent.putExtra("json", getAllData(file));
                            startService(obdIntent);

                        } else if (file == null) {
                            new AlertDialog.Builder(Select_json.this)
                                    .setTitle("error").setMessage("You didn't select a file!")
                                    .setNeutralButton("Ok", null).show();
                        } else if (!file.getName().toLowerCase().endsWith(".json")) {
                            new AlertDialog.Builder(Select_json.this)
                                    .setTitle("error").setMessage("The selected file is not a JSON file!")
                                    .setNeutralButton("Ok", null).show();
                        }
                    }
                });

                dialog.showDialog();
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
}
