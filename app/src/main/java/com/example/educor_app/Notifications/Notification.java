package com.example.educor_app.Notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.educor_app.Authentications.Dashboard;
import com.example.educor_app.Authentications.ProfileActivity;
import com.example.educor_app.Events.Event_page;
import com.example.educor_app.R;
import com.example.educor_app.chats.ui.activities.UserListingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Notification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationview);
        bottomNavigationView.setSelectedItemId(R.id.notification);

        String Classname = getIntent().getStringExtra("name");
        String key = getIntent().getStringExtra("key");
        String Teacher=getIntent().getStringExtra("Teacher");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent lntentss=new Intent(Notification.this, ProfileActivity.class);
                        lntentss.putExtra("key", key);
                        lntentss.putExtra("name",Classname);
                        lntentss.putExtra("Teacher", Teacher);
                        startActivity(lntentss);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.homepage:
                        Intent intent=new Intent(Notification.this, Dashboard.class);
                        intent.putExtra("name",Classname);
                        intent.putExtra("Teacher",Teacher);
                        intent.putExtra("key",key);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.chat:
                        Intent lntent=new Intent(Notification.this, UserListingActivity.class);
                        lntent.putExtra("name",Classname);
                        lntent.putExtra("Teacher", Teacher);
                        lntent.putExtra("key", key);
                        startActivity(lntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.Schedule:
                        Intent lntents=new Intent(Notification.this, Event_page.class);
                        lntents.putExtra("name",Classname);
                        lntents.putExtra("Teacher", Teacher);
                        lntents.putExtra("key", key);
                        startActivity(lntents);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.notification:
                        return true;
                }
                return false;
            }
        });

    }
}