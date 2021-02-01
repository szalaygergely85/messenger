package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


public class MessagesActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth fh;
    private String displayname = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        //Create or open SQL database and tables
        SQLiteDatabase mydatabase = openOrCreateDatabase("MESSENGER", MODE_PRIVATE, null);
        mydatabase.execSQL("DROP TABLE Messages");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS MyContacts(Userid VARCHAR,Username VARCHAR);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Messages(Messageid INT, Fr VARCHAR, Text VARCHAR);");


        db = FirebaseFirestore.getInstance();
        fh = FirebaseAuth.getInstance();


        db.collection(fh.getUid()).whereEqualTo("unread", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        // write message to the database
                        String messa = document.get("message").toString();
                        String fr = document.get("from").toString();
                        String tim = document.get("time").toString();
                        Log.d("jolesz", fr);
                        mydatabase.execSQL("INSERT INTO Messages (Messageid,Fr, Text) VALUES('" + tim + "', '" + fr + "', '" + messa + "');");

                        //check the user out
                        Cursor resultSet = mydatabase.rawQuery("Select Username from MyContacts where Userid ='" + fr + "'", null);
                        if (resultSet.getCount() == 0) {
                            db.collection("users").whereEqualTo("userid", fr).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                    List<DocumentSnapshot> docs = task2.getResult().getDocuments();
                                    Log.d("jolesz", docs.toString());

                                    DocumentSnapshot user = docs.get(0);
                                    String displayname = user.get("name").toString();
                                    Log.d("jolesz", displayname);

                                }
                            });

                            mydatabase.execSQL("INSERT INTO MyContacts VALUES('" + fr + "', '" + displayname + "');");


                        }
                    }
                } else {
                    Log.d("jolesz", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}