package com.example.assistant;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
            return;
        }

        String userId = currentUser.getUid();
        loadUserName(userId);

        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnChatHistory = findViewById(R.id.btnChatHistory);
        Button btnStartChat = findViewById(R.id.btnStartChat);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnProfile.setOnClickListener(v -> startActivity(new Intent(WelcomeActivity.this, ProfileActivity.class)));
        btnChatHistory.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, ChatHistoryActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
        btnStartChat.setOnClickListener(v -> {
            Toast.makeText(WelcomeActivity.this, "开始聊天按钮被点击", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        });
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadUserName(String userId) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nameValue = dataSnapshot.child("name").getValue(String.class);
                    if (nameValue != null) {
                        TextView tvWelcome = findViewById(R.id.tvWelcome);
                        tvWelcome.setText("嗨~ " + nameValue + "!");
                        Log.d(TAG, "user_name: " + nameValue);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(WelcomeActivity.this, "Failed to load user name", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
