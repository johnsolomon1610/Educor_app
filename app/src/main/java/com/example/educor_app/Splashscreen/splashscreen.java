package com.example.educor_app.Splashscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.educor_app.Authentications.LoginActivity;
import com.example.educor_app.Classroom.Classrooms;
import com.example.educor_app.R;

public class splashscreen extends AppCompatActivity {
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        img = (ImageView) findViewById(R.id.logo);
        Animation vecl = (Animation) AnimationUtils.loadAnimation(splashscreen.this, R.anim.mytransition);
        img.startAnimation(vecl);

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    String checkbox = preferences.getString("remember", "");
                    if (checkbox.equals("true")) {
                        Intent intent = new Intent(splashscreen.this, Classrooms.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(splashscreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timer.start();
    }
}