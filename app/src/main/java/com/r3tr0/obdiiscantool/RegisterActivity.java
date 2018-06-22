package com.r3tr0.obdiiscantool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText etname ;
    private EditText etmail ;
    private EditText etPassword ;
    private EditText etNumber ;
    private Button bt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etname =(EditText) findViewById(R.id.etname);
        etmail =(EditText) findViewById(R.id.etmail);
        etPassword =(EditText) findViewById(R.id.etpass);
        etNumber =(EditText) findViewById(R.id.etnumber);
        bt =(Button) findViewById(R.id.bt2);


        etname.addTextChangedListener(loginTextwatcher);
        etmail.addTextChangedListener(loginTextwatcher);
        etNumber.addTextChangedListener(loginTextwatcher);
        etPassword.addTextChangedListener(loginTextwatcher);



    }

    private TextWatcher loginTextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String username = etname.getText().toString().trim();
            String email =etmail.getText().toString().trim();
            String pass =etPassword.getText().toString().trim();
            String number=etNumber.getText().toString().trim();


            bt.setEnabled(!username.isEmpty()&&!email.isEmpty()&&!pass.isEmpty()&&!number.isEmpty());


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
