package com.example.assistant;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private EditText editTextName, editTextAge, editTextGrade;
    private Button buttonSave, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextGrade = findViewById(R.id.editTextGrade);
        buttonSave = findViewById(R.id.buttonSave);
        buttonBack = findViewById(R.id.buttonBack);

        buttonSave.setOnClickListener(v -> saveUserProfile());
        buttonBack.setOnClickListener(v -> finish());
    }

    private void saveUserProfile() {
        String name = editTextName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String grade = editTextGrade.getText().toString().trim();

        if (name.isEmpty() || age.isEmpty() || grade.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            saveUserProfileToDatabase(name, age, grade, userId);
        }
    }

    private void saveUserProfileToDatabase(String name, String age, String grade, String userId) {
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("name", name);
        userProfile.put("age", age);
        userProfile.put("grade", grade);

        databaseReference.child(userId).updateChildren(userProfile)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserProfileActivity.this, "Failed to Update Profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
