package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etname ;
    private EditText etmail ;
    private EditText etPassword ;
    private EditText etNumber ;
    private Button bt1 ;



    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etname =(EditText) findViewById(R.id.etname);
        etmail =(EditText) findViewById(R.id.etemail);
        etPassword =(EditText) findViewById(R.id.etpass);
        etNumber =(EditText) findViewById(R.id.etnumber);
        bt1 =(Button) findViewById(R.id.bt2);


        etname.addTextChangedListener(loginTextwatcher);
        etmail.addTextChangedListener(loginTextwatcher);
        etNumber.addTextChangedListener(loginTextwatcher);
        etPassword.addTextChangedListener(loginTextwatcher);


        mAuth=FirebaseAuth.getInstance();
        firebaseAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null)
                {
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;

                }


            }
        };



        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = etname.getText().toString();
                final String email =etmail.getText().toString();
                final String pass =etPassword.getText().toString();
                final String number=etNumber.getText().toString();


                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {

                        if(!task.isSuccessful())

                        {

                            Toast.makeText(RegisterActivity.this,"sign up error",Toast.LENGTH_LONG).show();

                        }else
                        {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_id = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                            Map map = new HashMap();

                            map.put("Name",username);
                            map.put("Email",email);
                            map.put("Phone",number);

                            current_user_id.setValue(map);


                        }


                    }
                });

            }
        });




    }

    private TextWatcher loginTextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            final String username = etname.getText().toString().trim();
            String email =etmail.getText().toString().trim();
            String pass =etPassword.getText().toString().trim();
            String number=etNumber.getText().toString().trim();


            bt1.setEnabled(!username.isEmpty()&&!email.isEmpty()&&!pass.isEmpty()&&!number.isEmpty());


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}
