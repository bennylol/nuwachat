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
public class EmoAdapter extends ArrayAdapter<EmoItem>{
    private Context context;
    private int spos=0;
    public EmoAdapter(Context context, ArrayList<EmoItem> EmoList) {
        super(context, 0, EmoList);
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
                R.layout.emo_spinner, parent, false);
        ImageView imageViewFlag = convertView.findViewById(R.id.image);
        TextView textViewName = convertView.findViewById(R.id.text1);
        EmoItem currentItem = getItem(position);
        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getEmoImage());
            textViewName.setText(currentItem.getEmoName());
            if (position == spos)
            {textViewName.setTextColor  (Color.argb(255, 0, 0, 0));}
        }
        return convertView;
    }
}
