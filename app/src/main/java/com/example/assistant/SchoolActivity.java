package com.example.assistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class SchoolActivity extends AppCompatActivity {
    private ArrayList<SchoolItem> mSchoolList;
    private SchoolAdapter mAdapter;

    private String schoolName = "日常需求";

    private Button s_btn;
    private Button dn_btn;
    private Button ee_btn;
    private Button in_btn;
    private Button ls_btn;
    private Button ch_btn;
    private Button sc_back;
    private Button sc_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        sc_back = findViewById(R.id.school_back);
        sc_main = findViewById(R.id.school_main);
        s_btn = findViewById(R.id.s_btn);

        initList();
        Spinner spinner = (Spinner) findViewById(R.id.school_spinner);
        mAdapter = new SchoolAdapter(this, mSchoolList);
        spinner.setAdapter(mAdapter);
        spinner.setSelection(0, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SchoolItem clickedItem = (SchoolItem) parent.getItemAtPosition(position);
                String clickedSchoolName = clickedItem.getSchoolName();
                schoolName = clickedSchoolName;

                String sPos=String.valueOf(position);
                //tvhello.setText("選項"+sPos+":"+clickedSchoolName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sc_back.setOnClickListener(view -> {
            finish();
        });

        sc_main.setOnClickListener(view -> {
            Intent intent = new Intent(SchoolActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });

        s_btn.setOnClickListener(view -> {
            if(schoolName.equals("日常需求")) {
                GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
                globalVariable.setAst_num("1");
                Intent intent = new Intent(SchoolActivity.this, DailyNeedsActivity.class);
                startActivity(intent);
            }
            else if (schoolName.equals("情感表達")) {
                GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
                globalVariable.setAst_num("2");
                Intent intent = new Intent(SchoolActivity.this, EmotionalExpression.class);
                startActivity(intent);
            }
            else if (schoolName.equals("互動交流")) {
                GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
                globalVariable.setAst_num("3");
                Intent intent = new Intent(SchoolActivity.this, Interaction.class);
                startActivity(intent);
            }
            else if (schoolName.equals("學習支援")) {
                GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
                globalVariable.setAst_num("4");
                Intent intent = new Intent(SchoolActivity.this, LearningSupport.class);
                startActivity(intent);
            }
            else if (schoolName.equals("閒聊")) {
                GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
                globalVariable.setAst_num("5");
                globalVariable.setTopic_nam("閒聊");
                Intent intent = new Intent(SchoolActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });



    }

    private void initList() {
        mSchoolList = new ArrayList<>();
        mSchoolList.add(new SchoolItem("日常需求", R.drawable.needs));
        mSchoolList.add(new SchoolItem("情感表達", R.drawable.reaction));
        mSchoolList.add(new SchoolItem("互動交流", R.drawable.interactions));
        mSchoolList.add(new SchoolItem("學習支援", R.drawable.listening));
        mSchoolList.add(new SchoolItem("閒聊", R.drawable.chat));
    }
}