package rohan.rao.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import rohan.rao.whatsappclone.Chat.ChatListAdapter;
import rohan.rao.whatsappclone.Chat.ChatObject;
import rohan.rao.whatsappclone.User.UserListAdapter;
import rohan.rao.whatsappclone.User.UserObject;

public class MainPageActivity extends AppCompatActivity {

    private RecyclerView mChatList;
    private  RecyclerView.Adapter mChatListAdapter;
    private RecyclerView.LayoutManager  mChatListLayoutManager;

    ArrayList<ChatObject> chatList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Fresco.initialize(this);
        chatList= new ArrayList<>();
        Button mLogout = findViewById(R.id.logout);
        Button mFindUser = findViewById(R.id.findUser);

        mFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FindUserActivity.class));
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        });

        getPermissions ();
        initializeRecyclerView();
        getUserChatList();
    }

    private void getUserChatList(){
        DatabaseReference mUserChatDB = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("chat");
        mUserChatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot childSnapshot:  snapshot.getChildren()){
                        ChatObject mChat  =  new ChatObject(childSnapshot.getKey());
                        boolean exists = false;
                        for(ChatObject  mChatIterator: chatList){
                            if(mChatIterator.getChatId().equals(mChat.getChatId()))
                                exists = true;
                        }
                        if(exists)
                            continue;
                        chatList.add(mChat);
                        mChatListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    private void initializeRecyclerView() {
        mChatList =findViewById(R.id.chatList);
        mChatList.setNestedScrollingEnabled(false);
        mChatList.setHasFixedSize(false);
        mChatListLayoutManager = new LinearLayoutManager(getApplicationContext(),  LinearLayoutManager.VERTICAL, false);
        mChatList.setLayoutManager(mChatListLayoutManager);
        mChatListAdapter = new ChatListAdapter(chatList);
        mChatList.setAdapter(mChatListAdapter);

    }
}