package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class StartActivity extends AppCompatActivity {
    private Button buttonlog;
    FirebaseAuth fh;
    FirebaseFirestore db;
    private Intent intent;
    private SQLiteDatabase mydatabase;
    private Cursor resultSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Create or open SQL database and tables
        mydatabase = openOrCreateDatabase("MESSENGER", MODE_PRIVATE, null);
        //  mydatabase.execSQL("DROP TABLE MyContacts");
        //  mydatabase.execSQL("DROP TABLE Messages");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS MyContacts(Userid VARCHAR, Username VARCHAR);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Messages(Messageid INT, Fr VARCHAR, ToID VARCHAR, FRName VARCHAR, Text VARCHAR, Read BOOLEAN);");

        fh = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        buttonlog = findViewById(R.id.button_login);
        buttonlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, loginActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fh = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            db.collection("users").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (!documentSnapshot.get("userid").toString().equals(null) && !documentSnapshot.get("name").toString().equals(null)) {
                        User userclass = new User(documentSnapshot.get("userid").toString(), documentSnapshot.get("name").toString());


                        intent = new Intent(StartActivity.this, MessagesActivity.class);
                        intent.putExtra("userclass", userclass);


                        new Thread() {
                            @Override
                            public void run() {
                                Log.d("Useremail", userclass.getName());
                                checkmessage();

                            }
                        }.start();

                        Log.d("Useremail", fh.getCurrentUser().getEmail());
                        startActivity(intent);
                        finish();
                    }
                }
            });

        }
    }

    public void register(android.view.View source) {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();

    }

    public void checkmessage() {
        while (true) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                Log.d("Useremail", "userclass.getName()");
                db.collection(fh.getCurrentUser().getUid()).whereEqualTo("downloaded", false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // write message to the database
                                if (!document.get("message").toString().equals(null) &&
                                        !document.get("from").toString().equals(null) &&
                                        !document.get("fromName").toString().equals(null) &&
                                        !document.get("to").toString().equals(null) &&
                                        !document.get("time").toString().equals(null)) {


                                    String messa = document.get("message").toString();
                                    String fr = document.get("from").toString();
                                    String frN = document.get("fromName").toString();
                                    String To = document.get("to").toString();
                                    String tim = document.get("time").toString();
                                    Log.d("jolesz", fr);
                                    try {
                                        mydatabase.execSQL("INSERT INTO Messages  VALUES(" + tim + ", '" + fr + "', '" + To + "' , '" + frN + "' , '" + messa + "', 'false');");
                                    } catch (SQLException e) {
                                        Log.e("SQL", e.toString());
                                    }
                                    db.collection(fh.getCurrentUser().getUid()).document(tim).update("downloaded", true);
                                    //check the user out
                                    try {
                                        Cursor resultSet = mydatabase.rawQuery("Select * from MyContacts where Userid ='" + fr + "'", null);
                                        if (resultSet.getCount() == 0) {
                                            mydatabase.execSQL("INSERT INTO MyContacts VALUES('" + fr + "', '" + frN + "');");
                                        }
                                    } catch (SQLException e) {
                                        Log.e("SQL", e.toString());
                                    }


                                }
                            }
                        } else {
                            Log.d("jolesz", "Error getting documents: ", task.getException());
                        }
                    }
                });


                Log.d("Hello", "Hello from the thread");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {

                }
            }
        }

    }
}