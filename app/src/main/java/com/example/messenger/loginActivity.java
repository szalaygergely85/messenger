package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    private Button loginb;
    private EditText email=null;
    private EditText pass=null;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.text_email2);
        pass = findViewById(R.id.text_pass2);
        auth = FirebaseAuth.getInstance();
        loginb = findViewById(R.id.button_login2);
        loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String text_email = email.getText().toString();
                String text_pass = pass.getText().toString();
                loginuser(text_email, text_pass);
            }
        });
    }

    private void loginuser(String text_email, String text_pass) {
        auth.signInWithEmailAndPassword(text_email, text_pass).addOnSuccessListener(loginActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(loginActivity.this, "Successfully signed in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(loginActivity.this, MainActivity.class));
                finish();

            }
        });

    }
}