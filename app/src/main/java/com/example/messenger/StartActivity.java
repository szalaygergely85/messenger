package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Create or open SQL database and tables
        mydatabase = openOrCreateDatabase("MESSENGER", MODE_PRIVATE, null);
        // mydatabase.execSQL("DROP TABLE MyContacts");
        // mydatabase.execSQL("DROP TABLE Messages");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS MyContacts(Userid VARCHAR, Username VARCHAR);");
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Messages(Messageid INT, Fr VARCHAR, Text VARCHAR);");

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

        if (fh.getCurrentUser() != null) {
            FirebaseUser user = fh.getCurrentUser();

            db.collection("users").document(user.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    intent = new Intent(StartActivity.this, MessagesActivity.class);
                    intent.putExtra("user", user);
                    User currentuser = new User(documentSnapshot.get("email").toString(), documentSnapshot.get("name").toString(), documentSnapshot.get("userid").toString());


                    new Thread() {
                        @Override
                        public void run() {
                            checkmessage(currentuser);
                        }
                    }.start();

                    Log.d("Useremail", currentuser.getEmail());
                    startActivity(intent);
                    finish();
                }
            });

        }
    }

    public void register(android.view.View source) {
        startActivity(new Intent(this, RegisterActivity.class));
        finish();

    }

    public void checkmessage(User user) {
        while (true) {
            Log.d("User", fh.getCurrentUser().getUid());
            db.collection(fh.getCurrentUser().getUid()).whereEqualTo("unread", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                            db.collection(fh.getCurrentUser().getUid()).document(tim).update("unread", false);
                            //check the user out
                            Cursor resultSet = mydatabase.rawQuery("Select * from MyContacts where Userid ='" + fr + "'", null);

                            Log.d("jolesz", "" + resultSet.getCount());
                            if (resultSet.getCount() == 0) {
                                mydatabase.execSQL("INSERT INTO MyContacts VALUES('" + user.getId() + "', '" + user.getName() + "');");
                            }
                        }
                    } else {
                        Log.d("jolesz", "Error getting documents: ", task.getException());
                    }
                }
            });


            Log.d("Hello", "Hello from the thread");
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {

            }

        }

    }
}