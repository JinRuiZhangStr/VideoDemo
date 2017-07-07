package com.example.videodemo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.dl7.player.media.IjkPlayerView;

public class Main3Activity extends AppCompatActivity {


    private IjkPlayerView mPlayerView;
    private String uri="http://covertness.qiniudn"+".com/android_zaixianyingyinbofangqi_test_baseline.mp4";
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        initView();
    }

    private void initView() {
        mPlayerView = (IjkPlayerView) findViewById(R.id.player_view);
        mUri = Uri.parse(uri);

        mPlayerView.init().setVideoPath(Environment.getExternalStorageDirectory() + "/1497794235699.mp4")
                .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_HIGH)
                .enableDanmaku()
                .start();
    }


}
