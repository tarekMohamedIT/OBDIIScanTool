package com.r3tr0.obdiiscantool;

import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import adapters.BluetoothDevicesAdapter;
import adapters.RecyclerListAdapter;
import bluetooth.BluetoothClient;

public class MainActivity extends AppCompatActivity {

    RecyclerListAdapter adapter;
    RecyclerView recyclerView;
    Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ArrayList<RecyclerListAdapter.ListItem> listItems = new ArrayList<>();
        listItems.add(new RecyclerListAdapter.ListItem(RecyclerListAdapter.MODE_FULL, R.drawable.ic_bluetoothvector,21));
        listItems.add(new RecyclerListAdapter.ListItem(RecyclerListAdapter.MODE_PART, R.drawable.ic_generalinfo,21));
        listItems.add(new RecyclerListAdapter.ListItem(RecyclerListAdapter.MODE_PART, R.drawable.ic_faultcodes,21));
        listItems.add(new RecyclerListAdapter.ListItem(RecyclerListAdapter.MODE_PART, R.drawable.ic_recordatrip,21));

        adapter = new RecyclerListAdapter(this, listItems);


        recyclerView = findViewById(R.id.devicesRecyclerView);


        GridLayoutManager manager = new GridLayoutManager(this, LinearLayoutManager.VERTICAL);
        manager.setSpanCount(2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == RecyclerListAdapter.MODE_FULL)
                    return 2;
                else return 1;
            }
        });


        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
