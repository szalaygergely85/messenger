package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText email=null;
    private EditText pass=null;
    private Button register = null;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.text_email2);
        pass = findViewById(R.id.text_pass2);
        register = findViewById(R.id.button_register);

        auth = FirebaseAuth.getInstance();
        //TODO more fields
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text_email = email.getText().toString();
                String text_pass = pass.getText().toString();

                if (TextUtils.isEmpty(text_email) || TextUtils.isEmpty(text_pass)) {
                    Toast.makeText(RegisterActivity.this, "empty", Toast.LENGTH_SHORT).show();
                }else {

                    registerUSer(text_email, text_pass);
                }
            }
        });
    }

    private void registerUSer(String text_email, String text_pass) {

        auth.createUserWithEmailAndPassword(text_email, text_pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, StartActivity.class));
                    finish();
                }else {
                    Toast.makeText(RegisterActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}