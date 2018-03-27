package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import java.util.ArrayList;

import adapters.RecyclerListAdapter;

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
        adapter.setOnItemClickListener(new RecyclerListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                switch (position){
                    case 0 :
                        startActivity(new Intent(MainActivity.this, BluetoothActivity.class));
                        break;
                    case 1 :
                        startActivity(new Intent(MainActivity.this, GeneralInformation.class));
                        break;
                    case 2 :
                        startActivity(new Intent(MainActivity.this, FaultCodes.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, TripActivity.class));
                        break;
                }
            }
        });

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
