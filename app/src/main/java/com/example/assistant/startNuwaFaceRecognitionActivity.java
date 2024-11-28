package com.example.assistant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class startNuwaFaceRecognitionActivity extends AppCompatActivity {
    private static final String TAG = "startNuwaFaceRecognitionActivity";

    private ImageButton closeBtn;
    private Button startRecognitionBtn;
    private EditText inputNameEdit;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    // 使用 ActivityResultLauncher 替代過時的 startActivityForResult
    private ActivityResultLauncher<Intent> faceRecognitionLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_nuwa_face_recognition);

        inputNameEdit = findViewById(R.id.input_name);
        closeBtn = findViewById(R.id.imgbtn_quit);
        startRecognitionBtn = findViewById(R.id.btn_start);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // 初始化 ActivityResultLauncher
        faceRecognitionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Long mfaceID = data.getLongExtra("EXTRA_RESULT_FACEID", 0);
                        String mname = data.getStringExtra("EXTRA_RESULT_NAME");

                        if (mfaceID != 0) {
                            // 成功獲取臉部識別數據
                            Toast.makeText(this, "Face recognition successful: mname (" + mname + "), FaceID (" + mfaceID + ")", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Face recognition successful: mname (" + mname + "), FaceID (" + mfaceID + ")");

                            // 儲存臉部識別數據到 Firebase
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                String userId = user.getUid();
                                saveFaceRecognitionData(userId, mfaceID, mname);
                            }
                        } else {
                            // 識別失敗
                            Toast.makeText(this, "Face recognition failed.", Toast.LENGTH_LONG).show();
                            Log.w(TAG, "Face recognition failed: mfaceID is 0");
                        }
                    } else {
                        // 處理識別被取消的情況
                        Toast.makeText(this, "Face recognition canceled or failed.", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Face recognition canceled or failed.");
                    }
                }
        );

        // 關閉按鈕行為
        closeBtn.setOnClickListener(view -> finish());

        // 啟動臉部識別按鈕行為
       /* startRecognitionBtn.setOnClickListener(v -> {
            String name = inputNameEdit.getText().toString();
            if (!TextUtils.isEmpty(name)) {
                // 有輸入名稱則帶入名稱進行臉部辨識
                startFaceRecognitionWithName(name);
            } else {
                // 無輸入名稱則直接啟動臉部辨識
                startFaceRecognition();
            }
        });*/
    }

    private void startFaceRecognition() {
        Intent intent = new Intent("com.nuwarobotics.action.FACE_REC");
        intent.setPackage("com.nuwarobotics.app.facerecognition2");
        intent.putExtra("EXTRA_3RD_REC_ONCE", true);
        faceRecognitionLauncher.launch(intent);
    }

    private void startFaceRecognitionWithName(String name) {
        Intent intent = new Intent("com.nuwarobotics.action.FACE_REC");
        intent.setPackage("com.nuwarobotics.app.facerecognition2");
        intent.putExtra("EXTRA_3RD_REC_ONCE", true);
        intent.putExtra("EXTRA_3RD_CONFIG_NAME", name);
        faceRecognitionLauncher.launch(intent);
    }

    private void saveFaceRecognitionData(String userId, Long mfaceID, String mname) {
        DatabaseReference faceRecognitionRef = databaseReference.child(userId).child("faceRecognition");

        faceRecognitionRef.child("mfaceID").setValue(mfaceID)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Face ID saved successfully");
                    } else {
                        Log.w(TAG, "Failed to save Face ID", task.getException());
                    }
                });

        faceRecognitionRef.child("mname").setValue(mname)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Face name saved successfully");
                    } else {
                        Log.w(TAG, "Failed to save Face name", task.getException());
                    }
                });
    }
}