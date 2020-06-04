package com.baronqi.jetpackvideo.util;

import com.baronqi.libcommon.AppGlobals;

public class DimenUtil {

    public static int dp2px(int size) {
        return (int) (AppGlobals.getApplication().getResources().getDisplayMetrics().density * size + .5f);
    }
}
