package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity2 extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Button register;
    private EditText name;
    private EditText phone;
    private String pass;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        //Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //fields
        pass = getIntent().getStringExtra("pass");
        email = getIntent().getStringExtra("email");
        register = findViewById(R.id.register2_b_register);
        name = findViewById(R.id.register2_t_name);
        phone = findViewById(R.id.register2_t_phone);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(RegisterActivity2.this, "empty", Toast.LENGTH_SHORT).show();
                } else {
                    String text_name = name.getText().toString();
                    String text_phone = phone.getText().toString();
                    registerUSer(email, pass, text_name, text_phone);
                    Log.d("phone", " " + text_phone);
                }
            }
        });

    }


    private void registerUSer(String text_email, String text_pass, String name, String phone) {

        auth.createUserWithEmailAndPassword(text_email, text_pass).addOnCompleteListener(RegisterActivity2.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity2.this, "Success", Toast.LENGTH_SHORT).show();
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", text_email);
                    user.put("name", name);
                    user.put("phone", phone);

//TODO Add user ID
                    /*
                    db.collection("users").endAt().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String userID = document.get("userid").toString();
                            }
                        }
                    })

                    user.put("userid", ); */
                    Log.d("phone", "" + phone);
                    db.collection("users").document(text_email).set(user);

                    startActivity(new Intent(RegisterActivity2.this, StartActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity2.this, "There was an error", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}