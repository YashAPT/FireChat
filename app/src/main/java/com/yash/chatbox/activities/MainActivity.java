package com.yash.chatbox.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yash.chatbox.R;
import com.yash.chatbox.fragments.ChatsFragment;

import com.yash.chatbox.fragments.ProfileFragment;
import com.yash.chatbox.fragments.UserFragment;
import com.yash.chatbox.model.Users;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference myRef;
    Toolbar mainToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatsFragment(), "CHATS");
        viewPagerAdapter.addFragment(new UserFragment(), "USERS");
        viewPagerAdapter.addFragment(new ProfileFragment(), "PROFILE");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                CustomIntent.customType(MainActivity.this, "fadein-to-fadeout");
                finish();
                return true;


        }
        return true;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {


        private List<Fragment> lstFragment;
        private List<String> lstTitle;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.lstFragment = new ArrayList<>();
            this.lstTitle = new ArrayList<>();

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return lstFragment.get(position);
        }

        @Override
        public int getCount() {
            return lstFragment.size();

        }

        public void addFragment(Fragment fragment, String title) {
            lstFragment.add(fragment);
            lstTitle.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return lstTitle.get(position);
        }
    }


}