package com.example.educor_app.Authentications;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.educor_app.R;

public class network_indicator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_indicator);

        final LottieAnimationView lottiePreloader=findViewById(R.id.network_button);
        Intent intent=getIntent();
        String Activitys=intent.getStringExtra("from");

        lottiePreloader.setVisibility(View.VISIBLE);
        lottiePreloader.setSpeed(1);
        lottiePreloader.playAnimation();

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if(Activitys.equals("RegisterActivity")){
                        Intent intent = new Intent(network_indicator.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(Activitys.equals("RegisterFormActivity")){
                        Intent intent = new Intent(network_indicator.this, RegisterFormActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(Activitys.equals("LoginActivity")){
                        Intent intent = new Intent(network_indicator.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(Activitys.equals("SendOTP")){
                        Intent intent = new Intent(network_indicator.this, SendOTP.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(Activitys.equals("VerifyOTP")){
                        Intent intent = new Intent(network_indicator.this, VerifyOTP.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        };
        timer.start();
    }
}