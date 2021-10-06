package com.example.educor_app.Splashscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.educor_app.Authentications.LoginActivity;
import com.example.educor_app.Authentications.RegisterActivity;
import com.example.educor_app.R;

public class Launching_splashscreen extends AppCompatActivity {

    ViewPager mSlideViewPager;
    LinearLayout mDotLayout;

    private TextView[] mDots;
    private  SliderAdapter sliderAdapter;
    TextView mNextBtn;

    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching_splashscreen);

        mSlideViewPager = findViewById(R.id.SlideViewPager);
        mDotLayout = findViewById(R.id.dotsLayout);

        mNextBtn = findViewById(R.id.NextBtn);

        boolean isFirsttime;
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editors = sharedPreferences.edit();
        isFirsttime = sharedPreferences.getBoolean("isFirsttime", true);
        if (isFirsttime) {
            editors.putBoolean("isFirsttime", false);
            editors.apply();

            sliderAdapter = new SliderAdapter(this);

            mSlideViewPager.setAdapter(sliderAdapter);

            addDotsIndicator(0);

            mSlideViewPager.addOnPageChangeListener(viewListener);

            mNextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3500);
                                Intent intent = new Intent(Launching_splashscreen.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }

                        }
                    });
                    thread.start();


                }
            });
        }else{
            Intent intent = new Intent(Launching_splashscreen.this,splashscreen.class);
            startActivity(intent);
            finish();
        }
    }

    public void addDotsIndicator(int position){

        mDots = new TextView[4];
        mDotLayout.removeAllViews();

        for(int i=0;i< mDots.length;i++){

            mDots[i] =new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            mDotLayout.addView(mDots[i]);
        }
        if(mDots.length>0){

            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }

    ViewPager.OnPageChangeListener viewListener =new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {

            addDotsIndicator(i);

            mCurrentPage=i;

            if(i==0){

                mNextBtn.setEnabled(false);
                mNextBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setText("");

            }else if(i==mDots.length-1){

                mNextBtn.setEnabled(true);
                mNextBtn.setVisibility(View.VISIBLE);

                mNextBtn.setText("Let's start");
            }
            else {

                mNextBtn.setEnabled(false);
                mNextBtn.setVisibility(View.INVISIBLE);
                mNextBtn.setText("");
                //mNextBtn.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

}