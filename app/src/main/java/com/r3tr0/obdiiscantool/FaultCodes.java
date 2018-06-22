package com.r3tr0.obdiiscantool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import adapters.BluetoothAdapter;

public class FaultCodes extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_codes);
        RecyclerView recyclerView = findViewById(R.id.devicesRecyclerView);
        ArrayList arrayList =new ArrayList<String>();
        arrayList.add("1) Suspension problem");
        arrayList.add("2) Error in fuel injection");
        arrayList.add("3) Error in fuel injection");
        arrayList.add("4) Brakes not working well");
        BluetoothAdapter Badapter =new BluetoothAdapter(this,arrayList);

        Badapter.setOnItemClickListener(new BluetoothAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, View view) {
                Toast.makeText(FaultCodes.this, "testing message", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(Badapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
