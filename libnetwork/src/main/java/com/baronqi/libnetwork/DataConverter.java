package com.baronqi.libnetwork;

import androidx.room.TypeConverter;

import java.util.Date;

public class DataConverter {

    @TypeConverter
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static Date longToDate(long time) {
        return new Date(time);
    }
}
