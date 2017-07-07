package com.example.videodemo.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by 张金瑞 on 2017/7/3.
 */

public class Dp2px {

    static int Dp2px(Activity activity,int dp){
        WindowManager wm= (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display dd=wm.getDefaultDisplay();
        DisplayMetrics dm=new DisplayMetrics();
        dd.getMetrics(dm);
        float density=dm.density;
        float pxf=density*dp+0.5f;
        int px= (int) pxf;
        return px;
    }

}
