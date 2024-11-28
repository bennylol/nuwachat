package com.example.assistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class InviteOthers extends AppCompatActivity {

    private Button ag_btn;
    private Button re_btn;
    private Button io_back;
    private Button io_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_others);
        ag_btn = findViewById(R.id.agreebtn);
        re_btn = findViewById(R.id.refusebtn);
        io_back = findViewById(R.id.io_back);
        io_main = findViewById(R.id.io_main);

        io_back.setOnClickListener(view -> {
            finish();
        });

        io_main.setOnClickListener(view -> {
            Intent intent = new Intent(InviteOthers.this, WelcomeActivity.class);
            startActivity(intent);
        });

        ag_btn.setOnClickListener(view -> {
            GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
            globalVariable.setTopic_nam("對方同意");
            Intent intent = new Intent(InviteOthers.this, MainActivity2.class);
            startActivity(intent);
        });

        re_btn.setOnClickListener(view -> {
            GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
            globalVariable.setTopic_nam("對方拒絕");
            Intent intent = new Intent(InviteOthers.this, MainActivity2.class);
            startActivity(intent);
        });
    }
}