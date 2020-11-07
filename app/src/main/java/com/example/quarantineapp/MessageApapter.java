package com.example.quarantineapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageApapter extends RecyclerView.Adapter<MessageApapter.MessageViewHolder> {
    private List<String> messageList;
    Context context;

    public MessageApapter(List<String> messageList, Context context){
        this.messageList=messageList;
        this.context=context;
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView msgtext;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            msgtext=itemView.findViewById(R.id.individual_chat_text);
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_chat_layout,parent,false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        String message=messageList.get(position);
        holder.msgtext.setText(message);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

}
