package com.example.educor_app.Screenrecorder;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.educor_app.R;

public class FullVideoActivity extends AppCompatActivity {

    VideoView mVideoView;
    Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_video);

        Intent videoIntent = getIntent();
        videoUri = Uri.parse(videoIntent.getExtras().get("videoUri").toString());

        mVideoView = (VideoView)findViewById(R.id.VideoView);
        if (videoUri != null)
        {
            mVideoView.setVideoURI(videoUri);
        }
        else
        {
            return;
        }

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {

                        android.widget.MediaController mc = new android.widget.MediaController(FullVideoActivity.this);
                        mVideoView.setMediaController(mc);
                        mVideoView.requestFocus();
                        mc.setAnchorView(mVideoView);


                    }
                });

                mVideoView.start();

            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish();
            }
        });

    }

}
