package com.baronqi.jetpackvideo.util;

import com.baronqi.jetpackvideo.AppGlobals;

public class DimenUtil {

    public static int dp2px(int size) {
        return (int) (AppGlobals.getApplication().getResources().getDisplayMetrics().density * size + .5f);
    }
}
