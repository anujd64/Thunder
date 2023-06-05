package com.theflexproject.thunder.fragments.homeNew

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.theflexproject.thunder.database.MovieDao
import com.theflexproject.thunder.database.TVShowDao
import com.theflexproject.thunder.model.Movie
import com.theflexproject.thunder.model.MyMedia
import com.theflexproject.thunder.model.TVShowInfo.TVShow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeNewRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val tvShowDao: TVShowDao
) {

        private val _recentlyAdded = MutableLiveData<List<MyMedia>>()
        private val _recentlyReleased = MutableLiveData<List<MyMedia>>()
        private val _topRated = MutableLiveData<List<MyMedia>>()
        private val _lastPlayed = MutableLiveData<List<MyMedia>>()
        private val _watchlistMovies = MutableLiveData<List<MyMedia>>()
        private val _watchlistShows = MutableLiveData<List<MyMedia>>()
        private val _recentlyReleasedTV = MutableLiveData<List<MyMedia>>()
        private val _topRatedTV = MutableLiveData<List<MyMedia>>()

        fun getRecentlyAdded(): LiveData<List<MyMedia>> {
            _recentlyAdded.postValue(movieDao.getrecentlyadded())
            Log.d("HomeNewRepository", "getRecentlyAdded: ${movieDao.getrecentlyadded()}")
            return _recentlyAdded
        }

        fun getRecentlyReleased(): LiveData<List<MyMedia>> {
            _recentlyReleased.postValue(movieDao.getrecentreleases())
            Log.d("HomeNewRepository", "getRecentlyReleased: ${movieDao.getrecentreleases()}")
            return _recentlyReleased
        }

        fun getTopRatedMovies(): LiveData<List<MyMedia>> {
            _topRated.postValue(movieDao.topRated)
            return _topRated
        }

        fun getLastPlayed(): LiveData<List<MyMedia>> {
            _lastPlayed.postValue(movieDao.played)
            return _lastPlayed
        }

        fun getWatchlistMovies(): LiveData<List<MyMedia>> {
            _watchlistMovies.postValue(movieDao.watchlisted)
            return _watchlistMovies
        }

        fun getWatchlistShows(): LiveData<List<MyMedia>> {
            _watchlistShows.postValue(tvShowDao.watchlisted)
            return _watchlistShows
        }

        fun getTopRatedShows(): LiveData<List<MyMedia>> {
            _topRatedTV.postValue(tvShowDao.topRated)
            return _topRatedTV
        }

        fun getRecentlyReleasedTVShows(): LiveData<List<MyMedia>> {
            _recentlyReleasedTV.postValue(tvShowDao.newShows)
            return _recentlyReleasedTV
        }





//    fun getRecentlyAdded(): LiveData<List<Movie>> {
//        val recentlyReleased = movieDao.getrecentlyadded()
//        Log.d("HomeNewRepository", "getRecentlyAdded: ${recentlyReleased.value.toString()}")
//        return recentlyReleased
//    }
//    fun getRecentlyReleased(): LiveData<List<Movie>> = movieDao.getrecentreleases()
//    fun getTopRatedMovies(): LiveData<List<Movie>> = movieDao.topRated
//    fun getLastPlayed(): LiveData<List<Movie>> = movieDao.played
//    fun getWatchlistMovies(): LiveData<List<Movie>> = movieDao.watchlisted
//    fun getWatchlistShows(): LiveData<List<TVShow>> = tvShowDao.watchlisted
//    fun getTopRatedShows(): LiveData<List<TVShow>> = tvShowDao.topRated
//    fun getRecentlyReleasedTVShows(): LiveData<List<TVShow>> = tvShowDao.newShows

}
