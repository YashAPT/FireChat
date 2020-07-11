package com.yash.chatbox.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yash.chatbox.R;
import com.yash.chatbox.adapters.MessageAdapter;
import com.yash.chatbox.model.Chats;
import com.yash.chatbox.model.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    ImageView imageView;
    Toolbar mssgToolbar;

    String userid;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;

    RecyclerView mssgRecycler;
    EditText mssgText;
    FloatingActionButton sendFab;

    MessageAdapter messageAdapter;
    List<Chats> mChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mssgToolbar = findViewById(R.id.mssg_toolbar);
        imageView = findViewById(R.id.mssg_image);
        username = findViewById(R.id.mssg_name);
        mssgRecycler = findViewById(R.id.mssg_rec);
        sendFab = findViewById(R.id.send_button);
        mssgText = findViewById(R.id.mssg_text);
        //mssgRecycler = findViewById(R.id.mssg_rec);
        mssgRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mssgRecycler.setLayoutManager(linearLayoutManager);


        setSupportActionBar(mssgToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mssgToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        intent = getIntent();
        userid = intent.getStringExtra("userid");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                username.setText(users.getUsername());

                if (users.getImageUrl().equals("default")) {
                    imageView.setImageResource(R.drawable.userphoto);
                } else {
                    Glide.with(MessageActivity.this).load(users.getImageUrl()).into(imageView);
                }

                readMessages(firebaseUser.getUid(), userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtMessage = mssgText.getText().toString();
                if (!txtMessage.equals("")) {
                    sendMessage(firebaseUser.getUid(), userid, txtMessage);
                } else {
                    Toast.makeText(MessageActivity.this, "type a message", Toast.LENGTH_SHORT).show();
                }

                mssgText.setText("");
            }
        });


    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance()
                .getReference("ChatList").child(firebaseUser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readMessages(final String myid, final String userid) {
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chats chat = dataSnapshot.getValue(Chats.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid)
                            || chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mChat);
                    mssgRecycler.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.message_menu, menu);
        return true;
    }
}