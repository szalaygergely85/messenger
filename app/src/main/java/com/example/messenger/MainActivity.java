package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button logout;
    private Button send;
    private EditText mess;

    private FirebaseFirestore db2;
    private FirebaseAuth fh;
    private static String TO = "KWjw68Nti2cdhGetEpCytsaBnMT2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fh = FirebaseAuth.getInstance();
        db2 = FirebaseFirestore.getInstance();

        mess = findViewById(R.id.editTextMessage);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fh.signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
            }
        });


        send = findViewById(R.id.button_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_mess = mess.getText().toString();
                Map<String, Object> message = new HashMap<>();
                message.put("to", TO);
                message.put("from", fh.getUid().toString());
                message.put("message", txt_mess);
                Log.d("messagetosend", TO + " " + fh.getUid().toString() + " " + txt_mess);
                db2.collection("messages").document(TO).set(message);


            }
        });


    }
}