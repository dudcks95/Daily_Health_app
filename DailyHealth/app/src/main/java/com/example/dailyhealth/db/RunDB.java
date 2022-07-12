package com.example.dailyhealth.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(
        entities = {Run.class},
        version = 1,
        exportSchema = false
)
@TypeConverters(Converters.class)
public abstract  class RunDB extends RoomDatabase {
    public abstract  RunDAO getRunDAO();
}
