package com.example.assistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class EmotionalExpression extends AppCompatActivity {

    private ArrayList<EmoItem> mEmoList;
    private EmoAdapter mAdapter;

    private String emoName = "聊聊感受";

    private Button e_btn;
    private Button tf_btn;
    private Button de_btn;
    private Button ee_back;
    private Button ee_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotional_expression);
        ee_back = findViewById(R.id.emo_back);
        ee_main = findViewById(R.id.emo_main);
        e_btn = findViewById(R.id.e_btn);

        initList();
        Spinner spinner = (Spinner) findViewById(R.id.emo_spinner);
        mAdapter = new EmoAdapter(this, mEmoList);
        spinner.setAdapter(mAdapter);
        spinner.setSelection(0, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EmoItem clickedItem = (EmoItem) parent.getItemAtPosition(position);
                String clickedEmoName = clickedItem.getEmoName();
                emoName = clickedEmoName;

                String sPos=String.valueOf(position);
                //tvhello.setText("選項"+sPos+":"+clickedSchoolName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ee_back.setOnClickListener(view -> {
            finish();
        });

        ee_main.setOnClickListener(view -> {
            Intent intent = new Intent(EmotionalExpression.this, WelcomeActivity.class);
            startActivity(intent);
        });

        e_btn.setOnClickListener(view -> {
            if(emoName.equals("聊聊感受")) {
                GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
                globalVariable.setTopic_nam("聊聊感受");
                Intent intent = new Intent(EmotionalExpression.this, MainActivity2.class);
                startActivity(intent);
            }
            else if (emoName.equals("處理情緒")) {
                GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
                globalVariable.setTopic_nam("處理情緒");
                Intent intent = new Intent(EmotionalExpression.this, MainActivity2.class);
                startActivity(intent);
            }
        });

    }

    private void initList() {
        mEmoList = new ArrayList<>();
        mEmoList.add(new EmoItem("聊聊感受", R.drawable.expression));
        mEmoList.add(new EmoItem("處理情緒", R.drawable.emotional));
    }
}