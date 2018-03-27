package com.r3tr0.obdiiscantool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import adapters.BluetoothAdapter;

public class BluetoothActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        RecyclerView recyclerView =findViewById(R.id.bluetoothRecyclerView);
        ArrayList arrayList =new ArrayList<String>();
        arrayList.add("Redmi");
        arrayList.add("Microsoft");
        arrayList.add("HC-06");
        BluetoothAdapter Badapter =new BluetoothAdapter(this,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(Badapter);

    }


}
