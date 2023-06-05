package com.theflexproject.thunder.fragments.homeNew

import androidx.lifecycle.LiveData
import com.theflexproject.thunder.model.MyMedia

data class HomeData(
    val recentlyAdded: LiveData<List<MyMedia>>,
    val recentlyReleased: LiveData<List<MyMedia>>,
    val topRated: LiveData<List<MyMedia>>,
    val lastPlayed: LiveData<List<MyMedia>>,
    val watchlistMovies: LiveData<List<MyMedia>>,
    val watchlistShows: LiveData<List<MyMedia>>,
    val recentlyReleasedShows: LiveData<List<MyMedia>>,
    val topRatedShows: LiveData<List<MyMedia>>

)