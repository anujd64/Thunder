package com.theflexproject.thunder.experimental.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.theflexproject.thunder.experimental.repository.MovieRepo;
import com.theflexproject.thunder.model.Movie;

import java.util.List;

public class MovieViewModel extends ViewModel {

        private MovieRepo repository;
        private LiveData<List<Movie>> myData;

        public MovieViewModel(Application application) {
            repository = new MovieRepo(application);
            myData = repository.getMyData();
        }

        public LiveData<List<Movie>> getMyData() {
            return myData;
        }


}
