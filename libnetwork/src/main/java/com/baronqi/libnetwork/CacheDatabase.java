package com.baronqi.libnetwork;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.baronqi.libcommon.AppGlobals;

@Database(entities = {Cache.class}, version = 1, exportSchema = true)
public abstract class CacheDatabase extends RoomDatabase {

    private static final CacheDatabase database;

    static {
        //创建内存数据库, 只存在内存中，进程被杀，数据消失
//        Room.inMemoryDatabaseBuilder()
        database = Room.databaseBuilder(AppGlobals.getApplication(), CacheDatabase.class, "jetpack")
                .allowMainThreadQueries()
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        //数据库创建回调
                        super.onCreate(db);
                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                        //数据库打开回调
                        super.onOpen(db);
                    }
                })
                .setJournalMode(JournalMode.AUTOMATIC)
                .build();
    }

    public static CacheDatabase get() {
        return database;
    }
}
