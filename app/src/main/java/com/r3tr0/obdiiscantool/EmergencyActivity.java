package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import communications.EmergencyService;

public class EmergencyActivity extends AppCompatActivity {


    private Button mActive ;
    private EditText mphone;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);


        mActive=(Button)findViewById(R.id.active);
        mphone=(EditText)findViewById(R.id.phone);


        mActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=mphone.getText().toString();
                Intent intent = new Intent(EmergencyActivity.this, EmergencyService.class);
                intent.putExtra("Phone", phone);
                startService(intent);
            }
        });


    }



}
