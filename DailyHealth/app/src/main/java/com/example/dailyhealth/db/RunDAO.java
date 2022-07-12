package com.example.dailyhealth.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RunDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRun(Run run);

    @Delete
    void deleteRun(Run run);

    @Query("SELECT * FROM running ORDER BY timestamp DESC")
    LiveData<List<Run>> getAllRunsSortedByDate();

}
