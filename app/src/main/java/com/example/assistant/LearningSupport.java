package com.example.assistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class LearningSupport extends AppCompatActivity {

    private ArrayList<LearnItem> mLearnList;
    private LearnAdapter mAdapter;

    private String learnName = "舉手發言";

    private Button l_btn;
    private Button rs_btn;
    private Button ah_btn;
    private Button hc_btn;
    private Button jm_btn;
    private Button ls_back;
    private Button ls_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_support);
        ls_back = findViewById(R.id.l_back);
        ls_main = findViewById(R.id.l_main);
        l_btn = findViewById(R.id.l_btn);

        initList();
        Spinner spinner = (Spinner) findViewById(R.id.learn_spinner);
        mAdapter = new LearnAdapter(this, mLearnList);
        spinner.setAdapter(mAdapter);
        spinner.setSelection(0, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LearnItem clickedItem = (LearnItem) parent.getItemAtPosition(position);
                String clickedLearnName = clickedItem.getLearnName();
                learnName = clickedLearnName;

                String sPos=String.valueOf(position);
                //tvhello.setText("選項"+sPos+":"+clickedSchoolName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ls_back.setOnClickListener(view -> {
            finish();
        });

        ls_main.setOnClickListener(view -> {
            Intent intent = new Intent(LearningSupport.this, WelcomeActivity.class);
            startActivity(intent);
        });

        l_btn.setOnClickListener(view -> {
            GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
            if(learnName.equals("舉手發言")) {
                globalVariable.setTopic_nam("舉手發言");
            }
            else if (learnName.equals("請求幫忙")) {
                globalVariable.setTopic_nam("請求幫忙");
            }
            else if (learnName.equals("處理衝突")) {
                globalVariable.setTopic_nam("處理衝突");
            }
            else if (learnName.equals("參與會議")) {
                globalVariable.setTopic_nam("參與會議");
            }
            Intent intent = new Intent(LearningSupport.this, MainActivity2.class);
            startActivity(intent);
        });

    }

    private void initList() {
        mLearnList = new ArrayList<>();
        mLearnList.add(new LearnItem("舉手發言", R.drawable.talking));
        mLearnList.add(new LearnItem("請求幫忙", R.drawable.askforhelp));
        mLearnList.add(new LearnItem("處理衝突", R.drawable.conflictresolution));
        mLearnList.add(new LearnItem("參與會議", R.drawable.meeting));
    }
}