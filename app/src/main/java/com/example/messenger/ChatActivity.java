package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private Button logout;
    private Button send;
    private EditText mess;
    private FirebaseFirestore db2;
    private FirebaseAuth fh;
    private String TO;
    private User userclass;
    private MessageHead messageHead;
    private SQLiteDatabase mydatabase;
    private ChatListAdapter dataAdapter = null;
    private ListView listView;
    private List<Chat> chatInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageHead = (MessageHead) this.getIntent().getSerializableExtra("messagehead");


        mydatabase = openOrCreateDatabase("MESSENGER", MODE_PRIVATE, null);

        userclass = (User) this.getIntent().getSerializableExtra("userclass");

        // Chat List Part

        listView = (ListView) findViewById(R.id.listview_chat);
        listView.setAdapter(dataAdapter);

        Cursor resultSet2 = mydatabase.rawQuery("Select Messages.Fr, Messages.Text, Messages.Messageid from Messages Where Messages.Fr='" + messageHead.getUserID() + "' OR Messages.ToID='" + messageHead.getUserID() + "'", null);

        chatInfoList = new ArrayList<Chat>();
        if (resultSet2.moveToFirst()) {
            do {
                String User = resultSet2.getString(0);
                String messaget = resultSet2.getString(1);
                String time = resultSet2.getString(2);
                Log.d("jolesz", User);
                Chat messageh = new Chat(messaget, User, time);
                chatInfoList.add(messageh);

            } while (resultSet2.moveToNext());
            dataAdapter = new ChatListAdapter(ChatActivity.this, R.layout.listview_chat, chatInfoList);
            listView.setAdapter(dataAdapter);
        }


        //Send message PART




        TO = messageHead.getSender();
        Log.d("Userclass", userclass.getName());
        fh = FirebaseAuth.getInstance();
        db2 = FirebaseFirestore.getInstance();

        mess = findViewById(R.id.editTextMessage);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fh.signOut();
                startActivity(new Intent(ChatActivity.this, StartActivity.class));
                finish();
            }
        });


        send = findViewById(R.id.button_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                //This method returns the time in millis
                long timeMilli = date.getTime();
                String txt_mess = mess.getText().toString();
                Map<String, Object> message = new HashMap<>();
                message.put("to", messageHead.getUserID());
                message.put("from", userclass.getId());
                message.put("fromName", TO);
                message.put("message", txt_mess);
                message.put("time", timeMilli);
                message.put("downloaded", false);
                message.put("unread", true);

                Log.d("messagetosend", messageHead.getUserID() + " " + fh.getUid().toString() + " " + txt_mess);


                mydatabase.execSQL("INSERT INTO Messages  VALUES(" + timeMilli + ", '" + userclass.getId() + "', '" + messageHead.getUserID() + "' ,'" + userclass.getName() + "' , '" + txt_mess + "', 'true');");

                db2.collection(messageHead.getUserID()).document("" + timeMilli).set(message);
                mess.setText("");


            }
        });


    }
}