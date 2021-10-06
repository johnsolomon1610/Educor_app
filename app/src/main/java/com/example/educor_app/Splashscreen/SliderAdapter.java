package com.example.educor_app.Splashscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.educor_app.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ImageView slideImageView;
    TextView slideHeading, slideDescription;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {

            R.drawable.learning,
            R.drawable.meeting,
            R.drawable.content,
            R.drawable.message

    };
    public String[] slide_headings = {

            "E-Learning",
            "Meeting",
            "Share your content",
            "Message"
    };

    public String[] slide_descs = {

            "Get knowledge through virtual class",
            "Start or join a video conferencing",
            "They see what you see",
            "Send texts,messages and files"
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        slideHeading = view.findViewById(R.id.slide_heading);
        slideDescription = view.findViewById(R.id.slide_desc);
        slideImageView = view.findViewById(R.id.slide_image);

        String s1 = slide_headings[position];
        String s2 = slide_descs[position];

        slideHeading.setText(s1);
        slideDescription.setText(s2);
        slideImageView.setImageResource(slide_images[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ConstraintLayout) object);
    }
}
