package com.theflexproject.thunder.fragments.moviesLibraryFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theflexproject.thunder.model.Movie
import kotlinx.coroutines.launch

class MovieLibraryViewModel(private val repository: MovieLibraryRepository) : ViewModel() {


//    private var _movies: MutableLiveData<List<Movie>>? = null
//    val movies get() = _movies

    val moviesDB = repository.getMoviesFromDatabse()

}