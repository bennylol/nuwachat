package com.example.assistant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;

public class FaceRecognitionLoginActivity extends AppCompatActivity {

    private static final String TAG = "FaceRecognitionLoginActivity";
    private static final int FACE_RECOGNITION_REQUEST_CODE = 1;

    private FirebaseAuth auth;
    private NuwaRobotAPI nuwaRobotAPI;
    private IClientId mClientId;

    private ProgressBar progressBar;
    private ImageView ivFaceRecognition;
    private Button btnCancelFaceRecognition;
    private TextView tvFaceRecognitionPrompt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition_login);

        // 初始化视图组件
        progressBar = findViewById(R.id.progressBar);
        ivFaceRecognition = findViewById(R.id.ivFaceRecognition);
        btnCancelFaceRecognition = findViewById(R.id.btnCancelFaceRecognition);
        tvFaceRecognitionPrompt = findViewById(R.id.tvFaceRecognitionPrompt);

        auth = FirebaseAuth.getInstance();
        mClientId = new IClientId(this.getPackageName());
        nuwaRobotAPI = new NuwaRobotAPI(this, mClientId);

        // 启动脸部识别活动
        Intent intent = new Intent("com.nuwarobotics.action.FACE_REC");
        intent.setPackage("com.nuwarobotics.app.facerecognition2");
        intent.putExtra("EXTRA_3RD_REC_ONCE", true);
        startActivityForResult(intent, FACE_RECOGNITION_REQUEST_CODE);

        // 取消按钮点击事件
        btnCancelFaceRecognition.setOnClickListener(v -> {
            Toast.makeText(FaceRecognitionLoginActivity.this, "Face recognition canceled", Toast.LENGTH_SHORT).show();
            finish();  // 结束活动
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FACE_RECOGNITION_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Long faceID = data.getLongExtra("EXTRA_RESULT_FACEID", 0);

                if (faceID != 0) {
                    // 使用识别到的面部ID进行登录
                    authenticateWithFaceID(faceID);
                } else {
                    Toast.makeText(this, "Face recognition failed.", Toast.LENGTH_LONG).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Face recognition canceled or failed.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void authenticateWithFaceID(Long faceID) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.orderByChild("faceID").equalTo(faceID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String email = userSnapshot.child("email").getValue(String.class);
                        String password = "defaultPassword"; // 或者从数据库获取用户的实际密码

                        if (email != null) {
                            // 通过Firebase进行登录
                            auth.signInWithEmailAndPassword(email, password)
                                    .addOnSuccessListener(authResult -> {
                                        Toast.makeText(FaceRecognitionLoginActivity.this, "Face Login Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(FaceRecognitionLoginActivity.this, WelcomeActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(FaceRecognitionLoginActivity.this, "Face Login Failed", Toast.LENGTH_SHORT).show();
                                        finish();
                                    });
                        }
                    }
                } else {
                    Toast.makeText(FaceRecognitionLoginActivity.this, "Face ID not recognized.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FaceRecognitionLoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
