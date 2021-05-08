package com.example.characteranalyser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button tracingBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayAdapter<Attempt> adapter;
    ListView scoreList;
    TextView studentName;
    TextView studentClass;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentName = findViewById(R.id.student_name);
        studentClass = findViewById(R.id.student_class);
        tracingBtn = findViewById(R.id.tracing_btn);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null) {

            userId = mAuth.getCurrentUser().getUid();

            DocumentReference documentReference = db.collection("user").document(userId);

            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    studentName.setText(value.getString("name"));
                    studentClass.setText(value.getString("class"));
                }
            });

//             documentReference.get()
//                     .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                         @Override
//                         public void onSuccess(DocumentSnapshot documentSnapshot) {
//                         if (documentSnapshot.exists()) {
//                             studentName.setText(documentSnapshot.getString("name"));
//                             studentClass.setText(documentSnapshot.getString("class"));
//
//                         } else {
//                             Toast.makeText(MainActivity.this,"Document does not exist" + userId,Toast.LENGTH_SHORT).show();
//                         }
//                         }
//                     })
//                     .addOnFailureListener(new OnFailureListener() {
//                         @Override
//                         public void onFailure(@NonNull Exception e) {
//
//                         }
//                     });


            scoreList = findViewById(R.id.score_list);
            adapter = new ArrayAdapter<Attempt>(
                    this,
                    android.R.layout.simple_list_item_1,
                    new ArrayList<Attempt>()
            );
            scoreList.setAdapter(adapter);
            db.collection("Attempts")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            ArrayList<Attempt> attempts = new ArrayList<>();
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                                Attempt a = document.toObject(Attempt.class);
                                attempts.add(a);
                            }
                            adapter.addAll(attempts);
                        }
                    });

            tracingBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent tracingIntent = new Intent(MainActivity.this, TracingActivity.class);
                    startActivity(tracingIntent);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            sendToLogin();
        }
    }
    protected void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}