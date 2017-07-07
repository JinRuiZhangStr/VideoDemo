package com.example.videodemo.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

/**
 * Created by 张金瑞 on 2017/7/3.
 */

public class SoundUtils {

    /**
    获取最大音量
     */
    public static int getMaxVolume(Activity activity){
        AudioManager am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return max;
    }

    /**
     *获取当前的音量
     */
    public static int getCurrentVolume(Activity activity){
        AudioManager am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        int streamVolume=am.getStreamVolume(am.STREAM_MUSIC);
        return streamVolume;
    }

    /**
     * 设置音量
     * @param activity
     * @param volume
     */
    public static void setVolume(Activity activity,int volume){
        AudioManager am = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(am.STREAM_MUSIC,volume,0);

    }

}
