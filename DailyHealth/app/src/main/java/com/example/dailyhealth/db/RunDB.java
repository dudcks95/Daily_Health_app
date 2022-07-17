package com.example.dailyhealth.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {Run.class},
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters.class)
public abstract class RunDB extends RoomDatabase {
    public abstract  RunDAO getRunDAO();

    private static volatile RunDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    // to perform the insert on a background thread.
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RunDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RunDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RunDB.class, "RunDB")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
