package com.example.assistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class Interaction extends AppCompatActivity {

    private ArrayList<InItem> mInList;
    private InAdapter mAdapter;

    private String inName = "打招呼";

    private Button i_btn;

    private Button it_back;
    private Button it_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction);
        it_back = findViewById(R.id.i_back);
        it_main = findViewById(R.id.i_main);
        i_btn = findViewById(R.id.i_btn);

        initList();
        Spinner spinner = (Spinner) findViewById(R.id.in_spinner);
        mAdapter = new InAdapter(this, mInList);
        spinner.setAdapter(mAdapter);
        spinner.setSelection(0, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                InItem clickedItem = (InItem) parent.getItemAtPosition(position);
                String clickedInName = clickedItem.getInName();
                inName = clickedInName;

                String sPos=String.valueOf(position);
                //tvhello.setText("選項"+sPos+":"+clickedSchoolName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        it_back.setOnClickListener(view -> {
            finish();
        });

        it_main.setOnClickListener(view -> {
            Intent intent = new Intent(Interaction.this, WelcomeActivity.class);
            startActivity(intent);
        });

        i_btn.setOnClickListener(view -> {
            GlobalVariable globalVariable = ((GlobalVariable)getApplicationContext());
            if(inName.equals("打招呼")) {
                globalVariable.setTopic_nam("打招呼");
            }
            else if (inName.equals("認識同學")) {
                globalVariable.setTopic_nam("認識同學");
            }
            else if (inName.equals("分享物品")) {
                globalVariable.setTopic_nam("分享物品");
            }
            else if (inName.equals("參與活動")) {
                globalVariable.setTopic_nam("參與活動");
            }
            else if (inName.equals("邀請別人")) {
                globalVariable.setTopic_nam("邀請別人");
            }
            else if (inName.equals("讚美別人")) {
                globalVariable.setTopic_nam("讚美別人");
            }
            else if (inName.equals("討論興趣")) {
                globalVariable.setTopic_nam("討論興趣");
            }
            else if (inName.equals("給予幫忙")) {
                globalVariable.setTopic_nam("給予幫忙");
            }
            else if (inName.equals("接受讚美並回應")) {
                globalVariable.setTopic_nam("接受讚美並回應");
            }
            else if (inName.equals("回應他人需求")) {
                globalVariable.setTopic_nam("回應他人需求");
            }
            Intent intent = new Intent(Interaction.this, MainActivity2.class);
            startActivity(intent);
        });



    }

    private void initList() {
        mInList = new ArrayList<>();
        mInList.add(new InItem("打招呼", R.drawable.greetings));
        mInList.add(new InItem("認識同學", R.drawable.makefriends));
        mInList.add(new InItem("分享物品", R.drawable.sharing));
        mInList.add(new InItem("參與活動", R.drawable.participation));
        mInList.add(new InItem("邀請別人", R.drawable.adduser));
        mInList.add(new InItem("讚美別人", R.drawable.like));
        mInList.add(new InItem("接受讚美並回應", R.drawable.socialservices));
        mInList.add(new InItem("討論興趣", R.drawable.focusgroup));
        mInList.add(new InItem("給予幫忙", R.drawable.helping));
        mInList.add(new InItem("回應他人需求", R.drawable.socialcare));
    }
}