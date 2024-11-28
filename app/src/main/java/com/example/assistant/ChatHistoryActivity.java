package com.example.assistant;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ChatHistoryActivity extends AppCompatActivity {

    private static final String TAG = "ChatHistoryActivity";

    private FirebaseAuth mAuth;
    private RecyclerView recyclerViewChatHistory;
    private ChatHistoryAdapter chatHistoryAdapter;
    private List<Userdatabase> chatHistoryList;
    private DatabaseReference chatRef;
    private ProgressBar progressBar;
    private TextView tvEmptyView;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        // 初始化 Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 初始化视图
        recyclerViewChatHistory = findViewById(R.id.recyclerViewChatHistory);
        recyclerViewChatHistory.setLayoutManager(new LinearLayoutManager(this));
        chatHistoryList = new ArrayList<>();
        chatHistoryAdapter = new ChatHistoryAdapter(chatHistoryList);
        recyclerViewChatHistory.setAdapter(chatHistoryAdapter);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyView = findViewById(R.id.tvEmptyView);
        btnBack = findViewById(R.id.btnBack);

        // 设置返回按钮的点击事件
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 结束当前活动，返回上一页
            }
        });

        // 獲取當前用户
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d(TAG, "用户ID: " + userId);

            // 獲取 FirebaseDatabase 参考
            chatRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("chats").child("chatId1").child("messages");

            // 加載聊天紀錄
            loadChatHistory();
        } else {
            Toast.makeText(this, "用戶未登錄", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 设置点击事件监听器
        chatHistoryAdapter.setOnItemClickListener(new ChatHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Userdatabase message) {
                // 顯示信息
                Toast.makeText(ChatHistoryActivity.this, "點擊了消息: " + message.getMessageText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadChatHistory() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewChatHistory.setVisibility(View.GONE);
        tvEmptyView.setVisibility(View.GONE);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                chatHistoryList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Userdatabase message = snapshot.getValue(Userdatabase.class);
                        if (message != null) {
                            chatHistoryList.add(message);
                            Log.d(TAG, "加载消息: " + message.getMessageText());
                        } else {
                            Log.d(TAG, "消息解析失败");
                        }
                    }
                    recyclerViewChatHistory.setVisibility(View.VISIBLE);
                    tvEmptyView.setVisibility(View.GONE);
                } else {
                    Log.d(TAG, "沒有找到聊天紀錄");
                    recyclerViewChatHistory.setVisibility(View.GONE);
                    tvEmptyView.setVisibility(View.VISIBLE);
                }
                chatHistoryAdapter.notifyDataSetChanged();
                //return null;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "加載聊天紀錄出錯", databaseError.toException());
                Toast.makeText(ChatHistoryActivity.this, "無法加载聊天紀錄", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
