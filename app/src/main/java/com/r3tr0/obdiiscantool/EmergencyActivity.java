package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import communications.EmergencyService;

public class EmergencyActivity extends AppCompatActivity {


    private Button mActive ;
    private EditText mphone;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.r3tr0.obdiiscantool.R.layout.activity_emergency);


        mActive=(Button)findViewById(com.r3tr0.obdiiscantool.R.id.active);
        mphone=(EditText)findViewById(com.r3tr0.obdiiscantool.R.id.phone);


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
