package com.r3tr0.obdiiscantool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import adapters.BluetoothAdapter;

public class FaultCodes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_codes);
        RecyclerView recyclerView =findViewById(R.id.faultcodes);
        ArrayList arrayList =new ArrayList<String>();
        arrayList.add("First Fault");
        arrayList.add("Second Fault");
        BluetoothAdapter Badapter =new BluetoothAdapter(this,arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(Badapter);


    }
}
