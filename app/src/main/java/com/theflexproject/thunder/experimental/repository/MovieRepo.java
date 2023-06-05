package com.theflexproject.thunder.experimental.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.theflexproject.thunder.database.AppDatabase;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.Movie;

import java.util.List;

public class MovieRepo {
    private final String TAG = getClass().getSimpleName();

    private AppDatabase database;
    private LiveData<List<Movie>> myData;

    public MovieRepo(Application application) {
        database = DatabaseClient.getInstance(application).getAppDatabase();
        myData = database.movieDao().getAllForViewModel();
    }

    public LiveData<List<Movie>> getMyData() {
        return myData;
    }

}
