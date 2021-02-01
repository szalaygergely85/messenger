package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText email = null;
    private EditText pass = null;
    private EditText pass2 = null;
    private Button register = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.text_email);
        pass = findViewById(R.id.text_pass);
        pass2 = findViewById(R.id.text_pass3);
        register = findViewById(R.id.button_register);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO if(email.getText().toString().isempty())
                String text_email = email.getText().toString();
                String text_pass = pass.getText().toString();
                String text_pass2 = pass2.getText().toString();


                if (text_pass.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Passoword must be 6 char long", Toast.LENGTH_SHORT).show();

                } else if (!text_pass2.equals(text_pass)) {
                    Toast.makeText(RegisterActivity.this, "Two password must be the same", Toast.LENGTH_SHORT).show();

                } else {
                    Intent Register2 = new Intent(RegisterActivity.this, RegisterActivity2.class);
                    Register2.putExtra("email", text_email);
                    Register2.putExtra("pass", text_pass);
                    startActivity(Register2);
                }

            }
        });
    }


}