package com.yash.chatbox.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yash.chatbox.R;
import com.yash.chatbox.activities.MessageActivity;
import com.yash.chatbox.activities.RegisterActivity;
import com.yash.chatbox.model.Chats;
import com.yash.chatbox.model.Users;

import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    List<Chats> mChats;

    public static final int MSSG_TYPE_LEFT = 0;
    public static final int MSSG_TYPE_RIGHT = 1;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chats> mChats) {
        this.context = context;
        this.mChats = mChats;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);

            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);

            return new MessageAdapter.ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chats chat = mChats.get(position);
        holder.message.setText(chat.getMessage());

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.show_message);


        }
    }


    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChats.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSSG_TYPE_RIGHT;
        } else {
            return MSSG_TYPE_LEFT;
        }
    }


}
