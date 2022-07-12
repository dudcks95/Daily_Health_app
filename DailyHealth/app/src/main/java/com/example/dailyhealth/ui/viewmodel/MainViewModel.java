package com.example.dailyhealth.ui.viewmodel;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.dailyhealth.db.Run;
import com.example.dailyhealth.repository.RunRepository;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    @Inject
    RunRepository runRepository;

    public void insertRun(Run run){
        runRepository.InsertRun(run);
    }
}
