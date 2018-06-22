package com.r3tr0.obdiiscantool;

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

        etname =(EditText) findViewById(R.id.etName);
        etmail =(EditText) findViewById(R.id.etmail);
        etPassword =(EditText) findViewById(R.id.etpassword);
        etNumber =(EditText) findViewById(R.id.etnumber);
        bt1 =(Button) findViewById(R.id.bt1);


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

            final String username = etname.getText().toString().trim();
            String email =etmail.getText().toString().trim();
            String pass =etPassword.getText().toString().trim();
            String number=etNumber.getText().toString().trim();


            bt1.setEnabled(!username.isEmpty()&&!email.isEmpty()&&!pass.isEmpty()&&!number.isEmpty());

            bt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String email = etmail.getText().toString();
                    final String password = etPassword.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
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
                                map.put("Email",etmail);
                                map.put("Phone",etNumber);

                                current_user_id.setValue(map);


                            }


                        }
                    });

                }
            });

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


}
