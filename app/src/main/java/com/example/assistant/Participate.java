package com.example.assistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Participate extends AppCompatActivity {

    private Button sa_btn;
    private Button ot_btn;
    private Button p_back;
    private Button p_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate);
        sa_btn = findViewById(R.id.sabtn);
        ot_btn = findViewById(R.id.otbtn);
        p_back = findViewById(R.id.p_back);
        p_main = findViewById(R.id.p_main);

        p_back.setOnClickListener(view -> {
            finish();
        });

        p_main.setOnClickListener(view -> {
            Intent intent = new Intent(Participate.this, WelcomeActivity.class);
            startActivity(intent);
        });

        sa_btn.setOnClickListener(view -> {
            GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
            globalVariable.setTopic_nam("運動會");
            Intent intent = new Intent(Participate.this, MainActivity2.class);
            startActivity(intent);
        });

        ot_btn.setOnClickListener(view -> {
            GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
            globalVariable.setTopic_nam("校外教學");
            Intent intent = new Intent(Participate.this, MainActivity2.class);
            startActivity(intent);
        });
    }
}