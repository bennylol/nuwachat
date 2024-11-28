package com.example.assistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private EditText editTextName, editTextAge, editTextGrade;
    private TextView profileEmail, profileFaceID, profileFaceName;
    private Button buttonSave, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        editTextName = findViewById(R.id.editTextName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextGrade = findViewById(R.id.editTextGrade);
        profileEmail = findViewById(R.id.profileEmail);
        profileFaceID = findViewById(R.id.profileFaceID);
        profileFaceName = findViewById(R.id.profileFaceName);
        buttonSave = findViewById(R.id.buttonSave);
        buttonBack = findViewById(R.id.buttonBack);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            profileEmail.setText("Email: " + user.getEmail());
            loadUserProfile(user.getUid());
        } else {
            Toast.makeText(ProfileActivity.this, "No user logged in", Toast.LENGTH_SHORT).show();
        }

        // 保存按鈕
        buttonSave.setOnClickListener(v -> {
            if (user != null) {
                saveUserProfile(user.getUid());
            }
        });

        // 返回按鈕
        buttonBack.setOnClickListener(v -> finish());
    }

    // 加載用戶資料
    private void loadUserProfile(String userId) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> userProfile = (Map<String, Object>) dataSnapshot.getValue();
                    if (userProfile != null) {
                        // 加載資料到 EditText
                        editTextName.setText((String) userProfile.get("name"));
                        editTextAge.setText((String) userProfile.get("age"));
                        editTextGrade.setText((String) userProfile.get("grade"));

                        // 加載臉部辨識資料
                        Map<String, Object> faceRecognition = (Map<String, Object>) userProfile.get("faceRecognition");
                        if (faceRecognition != null) {
                            profileFaceName.setText("Face Name: " + faceRecognition.get("name"));
                            profileFaceID.setText("Face ID: " + faceRecognition.get("faceID"));
                        } else {
                            profileFaceName.setText("Face Name: 未知");
                            profileFaceID.setText("Face ID: 未知");
                        }
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "User profile not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 保存更新後的用戶資料
    private void saveUserProfile(String userId) {
        String name = editTextName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String grade = editTextGrade.getText().toString().trim();

        if (name.isEmpty() || age.isEmpty() || grade.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // 更新資料到 Firebase
        Map<String, Object> userProfileUpdates = new HashMap<>();
        userProfileUpdates.put("name", name);
        userProfileUpdates.put("age", age);
        userProfileUpdates.put("grade", grade);

        databaseReference.child(userId).updateChildren(userProfileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
