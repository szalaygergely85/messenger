package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.tasks.Tasks.whenAll;


public class MessagesActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth fh;
    private String displayname = null;
    private User userclass;
    private String UserID;

    MessagesListAdapter dataAdapter = null;
    ListView listView;
    List<MessageHead> messageInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        userclass = (User) this.getIntent().getSerializableExtra("userclass");
        listView = (ListView) findViewById(R.id.listview_messages);
        listView.setAdapter(dataAdapter);


        //Create or open SQL database and tables
        SQLiteDatabase mydatabase = openOrCreateDatabase("MESSENGER", MODE_PRIVATE, null);


        db = FirebaseFirestore.getInstance();
        fh = FirebaseAuth.getInstance();

        try {
            Cursor resultSet2 = mydatabase.rawQuery("Select Messages.FRName, Messages.Text, Messages.Fr, MAX(Messages.Messageid) from Messages Where Messages.ToID= '" + userclass.getId() + "' Group By Messages.Fr", null);
            Log.d("ezzzz", "" + resultSet2.getCount());

            messageInfoList = new ArrayList<MessageHead>();
            if (resultSet2.moveToFirst()) {
                do {
                    String User = resultSet2.getString(0);
                    String messaget = resultSet2.getString(1);
                    String UserID = resultSet2.getString(2);
                    MessageHead messageh = new MessageHead(messaget, User, UserID);
                    messageInfoList.add(messageh);

                } while (resultSet2.moveToNext());


                dataAdapter = new MessagesListAdapter(MessagesActivity.this, R.layout.listview_messages, messageInfoList);
                listView.setAdapter(dataAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MessageHead Contact = new MessageHead();
                        Contact = (MessageHead) parent.getItemAtPosition(position);
                        Intent intent = new Intent(MessagesActivity.this, ChatActivity.class);
                        intent.putExtra("userclass", userclass);
                        intent.putExtra("messagehead", Contact);


                        startActivity(intent);
                    }
                });
            }
        } catch (SQLException e) {
            Log.e("SQL", e.toString());
        }

    }
}