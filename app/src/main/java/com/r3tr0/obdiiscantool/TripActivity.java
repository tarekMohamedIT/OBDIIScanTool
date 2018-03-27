package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class TripActivity extends AppCompatActivity {
    Button button;

    public void onClick(View view) {
        if (button.getText().equals("Start"))
            button.setText("End");

        else {
            startActivity(new Intent(TripActivity.this, TripEndActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        button = findViewById(R.id.endButton);
    }
}
