package com.baronqi.jetpackvideo.util;

public  class StringConvert {

    public static String convertUGC(int count) {
        if (count < 1000) {
            return String.valueOf(count);
        }

        return count + "万";
    }

}
