package com.example.dev;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {UserData.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase Instance;

    public abstract ProfileDao profileDao();
    //public abstract StepDao stepDao();

    static synchronized AppDatabase getDatabase(final Context context) {
        if (Instance == null) {
            Instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return Instance;
    }
}
