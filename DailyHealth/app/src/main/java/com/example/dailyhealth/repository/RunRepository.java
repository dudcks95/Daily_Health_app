package com.example.dailyhealth.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.dailyhealth.db.Run;
import com.example.dailyhealth.db.RunDAO;
import com.example.dailyhealth.db.RunDB;

import java.util.List;

import javax.inject.Inject;

public class RunRepository {

    private RunDAO runDAO;

    public RunRepository(Application application) {
        RunDB runDB = RunDB.getDatabase(application);
        runDAO = runDB.getRunDAO();
    }

    public void InsertRun(Run run){
        RunDB.databaseWriteExecutor.execute(()->{
            runDAO.insertRun(run);
        });
    }

    public void deleteRun(Run run){
        RunDB.databaseWriteExecutor.execute(()->{
            runDAO.deleteRun(run);
        });
    }

    public LiveData<List<Run>> getAllRunsSortedByDate(){
        return runDAO.getAllRunsSortedByDate();
    }

}
