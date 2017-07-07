package com.example.videodemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class Main2Activity extends AppCompatActivity {

    private VideoView vtm_videoView;
    private String uri="http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com/D046015255134077DDB3ACA0D7E68D45.flv";
    private boolean initialized;
    private static final String TAG = "Main2Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        initialized = Vitamio.isInitialized(this);
        if (!LibsChecker.checkVitamioLibs(this)){
            return;
        }

        Log.e(TAG, "onCreate: "+initialized);
        initView();
    }

    private void initView() {

        if (initialized){
            vtm_videoView = (VideoView) findViewById(R.id.vtm_videoView);
//            File file=new File(Environment.getExternalStorageDirectory() + "/1497794235699.mp4");
//            vtm_videoView.setVideoPath(file.getAbsolutePath());
            vtm_videoView.setVideoURI(Uri.parse(uri));
            MediaController controller=new MediaController(this);
            vtm_videoView.setMediaController(controller);
            vtm_videoView.start();
//            vtm_videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//                @Override
//                public void onBufferingUpdate(MediaPlayer mp, int percent) {
////percent 当前缓冲百分比
//                }
//            });
//            vtm_videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//                @Override
//                public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                    switch (what) {
//                        //开始缓冲
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
////                            percentTv.setVisibility(View.VISIBLE);
////                            netSpeedTv.setVisibility(View.VISIBLE);
//                            mp.pause();
//                            return true;
//                        //缓冲结束
//                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
////                            percentTv.setVisibility(View.GONE);
////                            netSpeedTv.setVisibility(View.GONE);
//                            mp.start();
//                            return true;
//
//                    }
//                    return false;
//                }
//            });
        }
    }
    public void click(View view){
        Intent intent=new Intent(this,Main3Activity.class);
        startActivity(intent);
    }

}
