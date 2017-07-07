package com.example.videodemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.videodemo.utils.BrightNessUtils;
import com.example.videodemo.utils.SoundUtils;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button start;
    private Button stop;
    private Button pause;
    private VideoView videoView;
    private int duration;
    private AppCompatSeekBar seekBar;
    private int video_progress;
    private Runnable runnable;
    private File file;
    private PlayStatus playing_status;
    private Timer timer;
    private Button btn_intent;
    private FrameLayout left;
    private FrameLayout right;

    private float oldLeftY=0;
    private float oldRightY=0;
    int dp=50;
    int volume=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_SETTINGS},0);

        int permission= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_SETTINGS);
        Log.e("权限申请", "onCreate: "+ permission);
        Log.e("权限申请2", "onCreate: "+PackageManager.PERMISSION_GRANTED );

        initView();
        initVideoListener();
        file = new File(Environment.getExternalStorageDirectory() + "/1497794235699.mp4");
        if (file.exists()) {
            videoView.setVideoPath(file.getAbsolutePath());//给控件设置本地文件播放路径
//            videoView.start();//开始播放
            duration = videoView.getDuration();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
        } else {
            Toast.makeText(this, "找不到播放的资源", Toast.LENGTH_SHORT).show();
        }
        if (permission==PackageManager.PERMISSION_GRANTED){
            initBrightManager();
            initVolumeManager();
        }
    }

    private void initVideoListener() {
        runnable = new Runnable() {
            @Override
            public void run() {
                video_progress = videoView.getCurrentPosition();
                if (duration != 0) {
                    seekBar.setProgress(video_progress);
                }
            }
        };
    }

    private void initView() {
        start = (Button) findViewById(R.id.btn_start);
        stop = (Button) findViewById(R.id.btn_stop);
        pause = (Button) findViewById(R.id.btn_pause);
        btn_intent = (Button) findViewById(R.id.btn_intent);
        videoView = (VideoView) findViewById(R.id.videoview);
        seekBar = (AppCompatSeekBar) findViewById(R.id.seekbar);
        left = (FrameLayout) findViewById(R.id.left);
        right = (FrameLayout) findViewById(R.id.right);
        initEvent();
    }

    private void initEvent() {
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        pause.setOnClickListener(this);
        btn_intent.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                    startVideo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void startVideo(int position) {
        if ((videoView != null) && (file.exists()) && (PlayStatus.STATUS != playing_status)) {
            if (playing_status == PlayStatus.STOP) {
                videoView.seekTo(0);
            }
            if ((position == 0)) {
                videoView.start();
            } else {
                videoView.seekTo(position);
                videoView.start();
            }

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    video_progress = videoView.getCurrentPosition();
                    if (duration != 0) {
                        seekBar.setProgress(video_progress);
                    }
                }
            }, 0, 100);
            playing_status = PlayStatus.STATUS;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_start:
                duration = videoView.getDuration();
                seekBar.setMax(duration);
                startVideo(0);
                seekBar.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_stop:
                if ((videoView!=null)&&file.exists()&&videoView.canPause()){
                    if (timer!=null){
                        timer.purge();
                        timer=null;
                    }
                    seekBar.setProgress(0);
                    videoView.pause();
                    playing_status=PlayStatus.STOP;
                }
                break;
            case R.id.btn_pause:
                if ((videoView!=null)&&file.exists()&&videoView.canPause()){
                    if (timer!=null){
                        timer.purge();
                        timer=null;
                    }
                    videoView.pause();
                    playing_status=PlayStatus.PAUSE;
                }
                break;
            case R.id.btn_intent:
                Intent intent=new Intent(this,Main2Activity.class);
                startActivity(intent);
                break;

        }
    }

    private void initBrightManager(){
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    if (oldLeftY==0){
                        oldLeftY=event.getRawY();
                    }
                }
                if (event.getAction()==MotionEvent.ACTION_MOVE){
                    float newLeftY=event.getRawY();
                    float degree=newLeftY-oldLeftY;
                    float abs=Math.abs(degree);
                    int brightNess= BrightNessUtils.getBrightNess(MainActivity.this);
                    if (newLeftY-oldLeftY>0){
                        if (abs>=4*dp){
                            brightNess-=240;
                        }else if (abs>=3*dp){
                            brightNess-=180;
                        }else if (abs>=2*dp){
                            brightNess-=120;
                        }else if (abs>=1*dp){
                            brightNess=brightNess-60;
                        }
                        if (brightNess<0){
                            brightNess=0;
                        }
                        BrightNessUtils.setBrightNess(MainActivity.this,brightNess);
                    }else {
                        if (abs>=4*dp){
                            brightNess+=240;
                        }else if (abs>=3*dp){
                            brightNess+=180;
                        }else if (abs>=2*dp){
                            brightNess+=120;
                        }else if (abs>=1*dp){
                            brightNess=brightNess+60;
                        }
                        if (brightNess>=255){
                            brightNess=255;
                        }
                        BrightNessUtils.setBrightNess(MainActivity.this,brightNess);
                    }
                }

                if (event.getAction()==MotionEvent.ACTION_UP){
                    oldLeftY=0;
                }

                return true;
            }
        });
    }

    private void initVolumeManager(){
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_DOWN){
                    if (oldRightY==0){
                        oldRightY=event.getRawY();
                    }
                }
                if (event.getAction()==MotionEvent.ACTION_MOVE){
                    float newRightY=event.getAction();
                    float degree=newRightY=oldRightY;
                    float abs=Math.abs(degree);

                    int maxVolume= SoundUtils.getMaxVolume(MainActivity.this);
                    int bit=maxVolume/4;
                    int currentVolume=SoundUtils.getCurrentVolume(MainActivity.this);

                    if (newRightY-oldRightY>0){
                        if (abs>=4*dp){
                            currentVolume-=4*bit;
                        }else if (abs>=3*dp){
                            currentVolume-=3*bit;
                        }else if (abs>=2*dp){
                            currentVolume-=2*bit;
                        }else if (abs>=1*dp){
                            currentVolume-=1*bit;
                        }
                        if (currentVolume<0){
                            currentVolume=0;
                        }
                        SoundUtils.setVolume(MainActivity.this,currentVolume);
                    }else {
                        if (abs>=4*dp){
                            currentVolume+=240;
                        }else if (abs>=3*dp){
                            currentVolume+=180;
                        }else if (abs>=2*dp){
                            currentVolume+=120;
                        }else if (abs>=1*dp){
                            currentVolume+=60;
                        }
                        if (currentVolume>=maxVolume){
                            currentVolume=maxVolume;
                        }
                        SoundUtils.setVolume(MainActivity.this,currentVolume);
                    }
                }
                if (event.getAction()==MotionEvent.ACTION_UP){
                    oldRightY=0;
                }


                return true;
            }
        });

    }




    /**
     * 枚举
     */
    public enum PlayStatus {
        STOP, PAUSE, STATUS;
    }

    /**
     * 权限申请回掉
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 0:
                if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){

                }
                break;
        }
    }
}
