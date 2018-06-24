package com.r3tr0.obdiiscantool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private TextView RegisterLink;




    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.r3tr0.obdiiscantool.R.layout.activity_login);

        Password=(EditText) findViewById(com.r3tr0.obdiiscantool.R.id.etPass2);
        mail=(EditText) findViewById(com.r3tr0.obdiiscantool.R.id.etmail);
        bt2 =(Button) findViewById(com.r3tr0.obdiiscantool.R.id.bt2);

         RegisterLink =(TextView) findViewById(com.r3tr0.obdiiscantool.R.id.etText);
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


        RegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);


            }
        });



        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                String password = Password.getText().toString();

                if (mail.getText().toString().trim().isEmpty() || Password.getText().toString().trim().isEmpty())
                    Toast.makeText(LoginActivity.this, "You Must Enter Data", Toast.LENGTH_LONG).show();

                else
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {

                        if (!task.isSuccessful())

                        {

                            Toast.makeText(LoginActivity.this,"sign in error",Toast.LENGTH_LONG).show();
                        }



                        else
                        {

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }




                    }
                });
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

