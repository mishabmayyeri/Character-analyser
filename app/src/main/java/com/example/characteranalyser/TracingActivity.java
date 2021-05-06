package com.example.characteranalyser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class TracingActivity extends AppCompatActivity {

    private Button logoutBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing);

        logoutBtn = findViewById(R.id.logout_btn);
        mAuth = FirebaseAuth.getInstance();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendToLogin();
            }
        });

    }

    protected void sendToLogin() {
        Intent loginIntent = new Intent(TracingActivity.this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

}