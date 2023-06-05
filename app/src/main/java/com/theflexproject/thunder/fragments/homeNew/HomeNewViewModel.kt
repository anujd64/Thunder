package com.theflexproject.thunder.fragments.homeNew

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theflexproject.thunder.model.MyMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeNewViewModel @Inject constructor(
    private val repository: HomeNewRepository
) : ViewModel() {

//    init {
//        getHomeData()
//    }
    fun getHomeData() : HomeData? {
        var homeData: HomeData? = null
        viewModelScope.launch (Dispatchers.IO){
            homeData = HomeData(
                repository.getRecentlyAdded(),
                repository.getRecentlyReleased(),
                repository.getTopRatedMovies(),
                repository.getLastPlayed(),
                repository.getWatchlistMovies(),
                repository.getWatchlistShows(),
                repository.getTopRatedShows(),
                repository.getRecentlyReleasedTVShows()
            )
        }
        return homeData

    }

//    private val _homedata: MutableLiveData<Resource<HomeData>> = MutableLiveData<Resource<HomeData>>()

//    init{
//        _homedata.postValue(
//            Resource.Success(
//                HomeData(
//                    repository.getRecentlyAdded() as LiveData<List<MyMedia>>,
//                    repository.getRecentlyReleased() as LiveData<List<MyMedia>>,
//                    repository.getTopRatedMovies() as LiveData<List<MyMedia>>,
//                    repository.getLastPlayed() as LiveData<List<MyMedia>>,
//                    repository.getWatchlistMovies() as LiveData<List<MyMedia>>,
//                    repository.getWatchlistShows() as LiveData<List<MyMedia>>,
//                    repository.getTopRatedShows()  as LiveData<List<MyMedia>>,
//                    repository.getRecentlyReleasedTVShows() as LiveData<List<MyMedia>>
//                )
//            )
//        )
//    }
//
//    val homedata: LiveData<Resource<HomeData>> get() = _homedata

//    fun getHomeData() : HomeData {
//        return HomeData(
//        repository.getRecentlyAdded(),
//        repository.getRecentlyReleased(),
//        repository.getTopRatedMovies(),
//        repository.getLastPlayed(),
//        repository.getWatchlistMovies(),
//        repository.getWatchlistShows(),
//        repository.getTopRatedShows(),
//        repository.getRecentlyReleasedTVShows()
//    )

//        _homedata.value = Resource.Loading()
//        viewModelScope.launch(Dispatchers.IO){
//            _homedata.postValue(
//                Resource.Success(
//
//                )
//            )
//        }
//    }
}