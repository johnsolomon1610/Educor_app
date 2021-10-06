package com.example.educor_app.chats.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.educor_app.Authentications.Dashboard;
import com.example.educor_app.Authentications.ProfileActivity;
import com.example.educor_app.Events.Event_page;
import com.example.educor_app.Notifications.Notification;
import com.example.educor_app.R;
import com.example.educor_app.chats.core.logout.LogoutContract;
import com.example.educor_app.chats.core.logout.LogoutPresenter;
import com.example.educor_app.chats.ui.adapters.UserListingPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

public class UserListingActivity extends AppCompatActivity implements LogoutContract.View {
    private Toolbar mToolbar;
    private TabLayout mTabLayoutUserListing;
    private ViewPager mViewPagerUserListing;

    private LogoutPresenter mLogoutPresenter;

    private ActionBar actionBar;
    Window window;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserListingActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, UserListingActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_listing);

       // actionBar=getSupportActionBar();
      //  actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26A9E6")));

        this.setTitle("Chats");

        if (Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationview);
        bottomNavigationView.setSelectedItemId(R.id.chat);

        String Classname = getIntent().getStringExtra("name");
        String key = getIntent().getStringExtra("key");
        String Teacher=getIntent().getStringExtra("Teacher");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        Intent intent=new Intent(UserListingActivity.this, ProfileActivity.class);
                        intent.putExtra("name",Classname);
                        intent.putExtra("Teacher",Teacher);
                        intent.putExtra("key",key);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.homepage:
                        Intent lntent=new Intent(UserListingActivity.this, Dashboard.class);
                        lntent.putExtra("name",Classname);
                        lntent.putExtra("Teacher", Teacher);
                        lntent.putExtra("key", key);
                        startActivity(lntent);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.chat:
                        return true;
                    case R.id.Schedule:
                        Intent lntents=new Intent(UserListingActivity.this, Event_page.class);
                        lntents.putExtra("name",Classname);
                        lntents.putExtra("Teacher", Teacher);
                        lntents.putExtra("key", key);
                        startActivity(lntents);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.notification:
                        Intent lntentss=new Intent(UserListingActivity.this, Notification.class);
                        lntentss.putExtra("key", key);
                        lntentss.putExtra("name",Classname);
                        lntentss.putExtra("Teacher", Teacher);
                        startActivity(lntentss);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            }
        });

        bindViews();
        init();
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayoutUserListing = (TabLayout) findViewById(R.id.tab_layout_user_listing);
        mViewPagerUserListing = (ViewPager) findViewById(R.id.view_pager_user_listing);
    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);

        // set the view pager adapter
        UserListingPagerAdapter userListingPagerAdapter = new UserListingPagerAdapter(getSupportFragmentManager());
        mViewPagerUserListing.setAdapter(userListingPagerAdapter);

        // attach tab layout with view pager
        mTabLayoutUserListing.setupWithViewPager(mViewPagerUserListing);

        mLogoutPresenter = new LogoutPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_listing, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mLogoutPresenter.logout();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onLogoutSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        LoginActivity.startIntent(this,
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onLogoutFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
