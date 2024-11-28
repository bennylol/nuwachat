package com.example.assistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class DailyNeedsActivity extends AppCompatActivity {

    private ArrayList<DailyItem> mDailyList;
    private DailyAdapter mAdapter;

    private String dailyName = "生理需求";

    private Button d_btn;

    private Button pn_btn;
    private Button mc_btn;
    private Button cc_btn;
    private Button bi_btn;
    private Button dn_back;
    private Button dn_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_needs);
        d_btn = findViewById(R.id.d_btn);
        dn_back = findViewById(R.id.daily_back);
        dn_main = findViewById(R.id.daily_main);

        initList();
        Spinner spinner = (Spinner) findViewById(R.id.daily_spinner);
        mAdapter = new DailyAdapter(this, mDailyList);
        spinner.setAdapter(mAdapter);
        spinner.setSelection(0, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DailyItem clickedItem = (DailyItem) parent.getItemAtPosition(position);
                String clickedDailyName = clickedItem.getDailyName();
                dailyName = clickedDailyName;

                String sPos=String.valueOf(position);
                //tvhello.setText("選項"+sPos+":"+clickedSchoolName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        dn_back.setOnClickListener(view -> {
            finish();
        });

        dn_main.setOnClickListener(view -> {
            Intent intent = new Intent(DailyNeedsActivity.this, WelcomeActivity.class);
            startActivity(intent);
        });

        d_btn.setOnClickListener(view -> {
            if(dailyName.equals("生理需求")) {
                GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
                globalVariable.setTopic_nam("生理需求");
                Intent intent = new Intent(DailyNeedsActivity.this, MainActivity2.class);
                startActivity(intent);
            }
            else if (dailyName.equals("三餐選擇")) {
                GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
                globalVariable.setTopic_nam("三餐選擇");
                Intent intent = new Intent(DailyNeedsActivity.this, MainActivity2.class);
                startActivity(intent);
            }
            else if (dailyName.equals("衣服選擇")) {
                GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
                globalVariable.setTopic_nam("衣服選澤");
                Intent intent = new Intent(DailyNeedsActivity.this, MainActivity2.class);
                startActivity(intent);
            }
            else if (dailyName.equals("借用物品")) {
                GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
                globalVariable.setTopic_nam("借用物品");
                Intent intent = new Intent(DailyNeedsActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });


    }

    private void initList() {
        mDailyList = new ArrayList<>();
        mDailyList.add(new DailyItem("生理需求", R.drawable.physiology));
        mDailyList.add(new DailyItem("三餐選擇", R.drawable.meal));
        mDailyList.add(new DailyItem("衣服選擇", R.drawable.cloth));
        mDailyList.add(new DailyItem("借用物品", R.drawable.borrowing));
    }
}