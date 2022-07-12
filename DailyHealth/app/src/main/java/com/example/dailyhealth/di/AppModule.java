package com.example.dailyhealth.di;


import android.content.Context;

import androidx.room.Room;

import com.example.dailyhealth.db.RunDAO;
import com.example.dailyhealth.db.RunDB;
import com.example.dailyhealth.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ApplicationComponent.class)
public class AppModule {
    // singleton으로 만들어져야한다.

    @Singleton
    @Provides
    public RunDB provideRunDB( @ApplicationContext  Context app){
        return Room.databaseBuilder(app, RunDB.class, Constants.RUN_DB_NAME).build();
    }

    @Singleton
    @Provides
    public RunDAO provideRunDao(RunDB runDB){
        return runDB.getRunDAO();
    }

}
