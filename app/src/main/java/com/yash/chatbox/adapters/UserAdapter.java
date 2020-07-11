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
import com.yash.chatbox.R;
import com.yash.chatbox.activities.MessageActivity;
import com.yash.chatbox.activities.RegisterActivity;
import com.yash.chatbox.model.Users;

import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    List<Users> mUsers;

    public UserAdapter(Context context, List<Users> mUsers) {
        this.context = context;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Users users = mUsers.get(position);
        holder.username.setText(users.getUsername());

        if (users.getImageUrl().equals("default")) {
            holder.imageView.setImageResource(R.drawable.userphoto);
        } else {
            Glide.with(context).load(users.getImageUrl()).placeholder(R.color.dark).into(holder.imageView);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, MessageActivity.class);
                i.putExtra("userid", users.getId());
                CustomIntent.customType(context, "fadein-to-fadeout");
                context.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user_username);
            imageView = itemView.findViewById(R.id.user_img);

        }
    }
}
