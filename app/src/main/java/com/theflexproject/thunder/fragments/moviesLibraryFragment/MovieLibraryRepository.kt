package com.theflexproject.thunder.fragments.moviesLibraryFragment

import androidx.lifecycle.LiveData
import com.theflexproject.thunder.database.MovieDao
import com.theflexproject.thunder.model.Movie

class MovieLibraryRepository(private val dao: MovieDao) {
    fun getMoviesFromDatabse(): LiveData<List<Movie>> = dao.allForViewModel
}