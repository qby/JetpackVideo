package com.baronqi.libnetwork;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.io.Serializable;

@Entity
public class Cache implements Serializable {

    @PrimaryKey
    @NonNull
    public String key;

    public byte[] data;

}
