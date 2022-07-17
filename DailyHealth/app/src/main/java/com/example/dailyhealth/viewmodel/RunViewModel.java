package com.example.dailyhealth.viewmodel;

import android.app.Application;
import android.view.View;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.dailyhealth.db.Run;
import com.example.dailyhealth.repository.RunRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


public class RunViewModel extends AndroidViewModel {

    private RunRepository runRepository;
    public final LiveData<List<Run>> runList;

    public RunViewModel(Application application) {
        super(application);
        runRepository = new RunRepository(application);
        runList = runRepository.getAllRunsSortedByDate();
    }

    public void insertRun(Run run){
        runRepository.InsertRun(run);
    }

    public LiveData<List<Run>>  getAllRunsSortedByDate(){
        return runRepository.getAllRunsSortedByDate();
    }
}
