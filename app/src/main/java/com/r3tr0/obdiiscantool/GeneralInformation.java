package com.r3tr0.obdiiscantool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import adapters.GeneralInformationAdapter;
import models.GeneralInformationModel;

public class GeneralInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);
        RecyclerView recyclerView =findViewById(R.id.GeneralInformation);
        ArrayList arrayList=new ArrayList<GeneralInformationModel>();
        arrayList.add(new GeneralInformationModel(0,"speed",100,200));
        arrayList.add(new GeneralInformationModel(0,"Temperature",30,60));
        arrayList.add(new GeneralInformationModel(0,"GAZ",100,200));

        GeneralInformationAdapter generalInformationAdapter= new GeneralInformationAdapter(this,arrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(generalInformationAdapter);


    }
}
