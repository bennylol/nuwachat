package com.example.assistant;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class LearnAdapter extends ArrayAdapter<LearnItem>{
    private Context context;
    private int spos=0;
    public LearnAdapter(Context context, ArrayList<LearnItem> LearnList) {
        super(context, 0, LearnList);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        spos = position;
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.learn_spinner, parent, false);
        ImageView imageViewFlag = convertView.findViewById(R.id.image);
        TextView textViewName = convertView.findViewById(R.id.text1);
        LearnItem currentItem = getItem(position);
        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getLearnImage());
            textViewName.setText(currentItem.getLearnName());
            if (position == spos)
            {textViewName.setTextColor  (Color.argb(255, 0, 0, 0));}
        }
        return convertView;
    }
}
