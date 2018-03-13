package com.r3tr0.obdiiscantool;

import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

import adapters.BluetoothAdapter;

public class BluetoothActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        RecyclerView recyclerView =findViewById(R.id.bluetoothRecyclerView);
        ArrayList arrayList =new ArrayList<String>();
        arrayList.add("FDevic");
        arrayList.add("SDevic");
        arrayList.add("TDevic");
        BluetoothAdapter Badapter =new BluetoothAdapter(this,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(Badapter);
    }
}
