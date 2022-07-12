package com.example.dailyhealth.repository;

import androidx.lifecycle.LiveData;

import com.example.dailyhealth.db.Run;
import com.example.dailyhealth.db.RunDAO;

import java.util.List;

import javax.inject.Inject;

public class RunRepository {

    @Inject
    RunDAO runDAO;

    public void InsertRun(Run run){
        runDAO.insertRun(run);
    }

    public void deleteRun(Run run){
        runDAO.deleteRun(run);
    }

    public LiveData<List<Run>> getAllRunsSortedByDate(){
        return runDAO.getAllRunsSortedByDate();
    }

}
