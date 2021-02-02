package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.tasks.Tasks.whenAll;


public class MessagesActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth fh;
    private String displayname = null;

    MessagesListAdapter dataAdapter = null;
    ListView listView;
    List<MessageHead> contactsInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        listView = (ListView) findViewById(R.id.listview_messages);
        listView.setAdapter(dataAdapter);


        //Create or open SQL database and tables
        SQLiteDatabase mydatabase = openOrCreateDatabase("MESSENGER", MODE_PRIVATE, null);
        // mydatabase.execSQL("DROP TABLE MyContacts");
        // mydatabase.execSQL("DROP TABLE Messages");
        // mydatabase.execSQL("CREATE TABLE IF NOT EXISTS MyContacts(Userid VARCHAR, Username VARCHAR);");
        //   mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Messages(Messageid INT, Fr VARCHAR, Text VARCHAR);");


        db = FirebaseFirestore.getInstance();
        fh = FirebaseAuth.getInstance();


       /* db.collection(fh.getUid()).whereEqualTo("unread", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        // write message to the database
                        String messa = document.get("message").toString();
                        String fr = document.get("from").toString();
                        String tim = document.get("time").toString();
                        Log.d("jolesz", fr);
                        mydatabase.execSQL("INSERT INTO Messages  VALUES(" + tim + ", '" + fr + "', '" + messa + "');");

                        //check the user out
                        Cursor resultSet = mydatabase.rawQuery("Select * from MyContacts where Userid ='" + fr + "'", null);

                        Log.d("jolesz", "" + resultSet.getCount());
                        if (resultSet.getCount() == 0) {
                            Task task3 = db.collection("users").whereEqualTo("userid", fr).get();
                            task3.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                    List<DocumentSnapshot> docs = task2.getResult().getDocuments();

                                    DocumentSnapshot user = docs.get(0);
                                    String displayname = user.get("name").toString();

                                    mydatabase.execSQL("INSERT INTO MyContacts VALUES('" + fr + "', '" + displayname + "');");

                                }
                            });


                        }
                    }
                } else {
                    Log.d("jolesz", "Error getting documents: ", task.getException());
                }
            }
        });*/

        Cursor resultSet2 = mydatabase.rawQuery("Select Messages.Text, MyContacts.Userid from Messages, MyContacts where Messages.Messageid=(Select MAX(Messages.Messageid) from Messages Where Messages.Fr=(Select distinct MyContacts.Userid FROM MyContacts))", null);
        Log.d("ezzzz", "" + resultSet2.getCount());

        contactsInfoList = new ArrayList<MessageHead>();
        if (resultSet2.moveToFirst()) {
            do {
                String User = resultSet2.getString(0);
                String messaget = resultSet2.getString(1);
                MessageHead messageh = new MessageHead(messaget, User);
                contactsInfoList.add(messageh);

            } while (resultSet2.moveToNext());
            dataAdapter = new MessagesListAdapter(MessagesActivity.this, R.layout.listview_messages, contactsInfoList);
            listView.setAdapter(dataAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MessageHead Contact = new MessageHead();
                    Contact = (MessageHead) parent.getItemAtPosition(position);
                    startActivity(new Intent(MessagesActivity.this, MainActivity.class));
                }
            });
        }


    }
}