package com.r3tr0.obdiiscantool;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.GeneralInformationAdapter;
import models.GeneralInformationModel;

public class GeneralInformation extends AppCompatActivity {

    GeneralInformationAdapter generalInformationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_information);
        RecyclerView recyclerView =findViewById(R.id.GeneralInformation);
        ArrayList<GeneralInformationModel> arrayList = new ArrayList<>();

        try {
            JSONArray array = new JSONObject(PreferenceManager.getDefaultSharedPreferences(this).getString("json", "")).getJSONArray("car1");

            for (int j = 0; j < array.length(); j += 8) {
                for (int i = 0; i < 8; i++) {
                    JSONObject gaugeObject = array.getJSONObject(i + j);
                    if (j == 0)
                        arrayList.add(new GeneralInformationModel(0, gaugeObject.getString("name"), new ArrayList<Float>()));
                    arrayList.get(i).addValue((float) gaugeObject.getDouble("value"));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        generalInformationAdapter = new GeneralInformationAdapter(this, arrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(generalInformationAdapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                generalInformationAdapter.modifyItems();
            }
        }, 1000);

    }
}
