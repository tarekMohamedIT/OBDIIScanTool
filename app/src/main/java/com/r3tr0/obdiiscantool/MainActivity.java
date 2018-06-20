package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import adapters.RecyclerListAdapter;
import communications.ObdService;
import dialogs.MethodSelectionDialog;
import enums.RecyclerListAdapterMode;
import events.OnItemClickListener;

public class MainActivity extends AppCompatActivity {

    RecyclerListAdapter adapter;
    RecyclerView recyclerView;
    Button refreshButton;
    Intent obdIntent;

    MethodSelectionDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        obdIntent = new Intent(this, ObdService.class);

        ArrayList<RecyclerListAdapter.ListItem> listItems = new ArrayList<>();
        listItems.add(new RecyclerListAdapter.ListItem("Bluetooth", "Not connected", RecyclerListAdapterMode.fullWithTitleAndSubTitleMode, R.drawable.btnew));
        listItems.add(new RecyclerListAdapter.ListItem("change input", "", RecyclerListAdapterMode.partMode, R.drawable.ic_change_input));
        listItems.add(new RecyclerListAdapter.ListItem("general info", "", RecyclerListAdapterMode.partMode, R.drawable.ic_generalinfo));
        listItems.add(new RecyclerListAdapter.ListItem("fault codes", "", RecyclerListAdapterMode.partMode, R.drawable.ic_faultcodes));
        listItems.add(new RecyclerListAdapter.ListItem("record trips", "", RecyclerListAdapterMode.partMode, R.drawable.ic_recordatrip));
        adapter = new RecyclerListAdapter(this, listItems);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position){
                    case 0 :
                        if (adapter.getItem(position).getResImageID() == R.drawable.btnew)
                            startActivity(new Intent(MainActivity.this, BluetoothActivity.class));

                        else {
                            startActivity(new Intent(MainActivity.this, SelectJsonActivity.class));
                        }
                        break;

                    case 1 :
                        dialog.showDialog();
                        break;

                    case 2 :
                        startActivity(new Intent(MainActivity.this, GeneralInformationActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, FaultCodesActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, TripActivity.class));
                        break;
                }
            }
        });

        dialog = new MethodSelectionDialog(MainActivity.this, new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if (position == 0)
                    adapter.changeImage(0, R.drawable.jsonvector);
                else
                    adapter.changeImage(0, R.drawable.btnew);

                dialog.dismiss();
            }
        });

        recyclerView = findViewById(R.id.devicesRecyclerView);

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
    protected void onDestroy() {
        super.onDestroy();
    }


}
