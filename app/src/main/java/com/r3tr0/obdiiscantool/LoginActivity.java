package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText Password;
    private EditText mail;
    private Button bt2;

    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Password=(EditText) findViewById(R.id.etPass2);
        mail=(EditText) findViewById(R.id.etmail);
        bt2 =(Button) findViewById(R.id.bt2);
        final TextView RegisterLink =(TextView) findViewById(R.id.regHere);
        Password.addTextChangedListener(LTextWatcher);
        mail.addTextChangedListener(LTextWatcher);



        mAuth=FirebaseAuth.getInstance();
        firebaseAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null)
                {
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;

                }


            }
        };


        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mail.getText().toString();
                final String password = Password.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {

                        if(!task.isSuccessful())

                        {

                            Toast.makeText(LoginActivity.this,"sign in error",Toast.LENGTH_LONG).show();

                        }

                    }
                });
            }
        });


        RegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);


            }
        });



    }

    private TextWatcher LTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String email =mail.getText().toString().trim();
            String pass =Password.getText().toString().trim();

            bt2.setEnabled(!email.isEmpty()&&!pass.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    }

