package com.example.assistant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import android.widget.ImageView;

public class ChatHistoryAdapter extends RecyclerView.Adapter<ChatHistoryAdapter.ChatHistoryViewHolder> {

    private List<Userdatabase> chatHistoryList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Userdatabase message);
    }

    public ChatHistoryAdapter(List<Userdatabase> chatHistoryList) {
        this.chatHistoryList = chatHistoryList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_history, parent, false);
        return new ChatHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHistoryViewHolder holder, int position) {
        Userdatabase message = chatHistoryList.get(position);
        holder.textViewMessageText.setText(message.getMessageText());
        try {
            holder.textViewDateTime.setText(dateFormat.format(dateFormat.parse(message.getDateTime())));
        } catch (ParseException e) {
            holder.textViewDateTime.setText(message.getDateTime()); // 如果解析失败，直接显示原始字符串
        }
    }

    @Override
    public int getItemCount() {
        return chatHistoryList.size();
    }

    public void updateChatHistoryList(List<Userdatabase> newChatHistoryList) {
        chatHistoryList.clear();
        chatHistoryList.addAll(newChatHistoryList);
        notifyDataSetChanged();
    }

    public class ChatHistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMessageText;
        public TextView textViewDateTime;
        public ImageView imageViewAvatar;

        public ChatHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessageText = itemView.findViewById(R.id.textViewMessageText);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(chatHistoryList.get(position));
                        }
                    }
                }
            });
        }
    }
}

