package com.example.savvologytask.ui.movielist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import certif.id.app.base.BaseViewModel
import com.example.savvologytask.data.remote.service.MovieService
import com.example.savvologytask.helper.EventWrapper
import com.example.savvologytask.helper.NetInfo
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class MovieListViewModel : BaseViewModel() {

    val state = MutableLiveData<EventWrapper<MovieListState>>()
    fun state() : LiveData<EventWrapper<MovieListState>> = state

    val pageNumberLiveData = MutableLiveData<Int>(1)


    companion object{
        private const val API_KEY = "14d6d178878ae5394f68679fe1402c22"
    }

    val movieService : MovieService by inject()

    fun fetchNowPlaying() = viewModelScope.launch {
        if(!NetInfo.isInternetOn()){
            state.postValue(EventWrapper(MovieListState.NoInternet))
            return@launch
        }

        runCatching {
            movieService.getMovieList(API_KEY,pageNumberLiveData.value!!)
        }.fold({
            state.postValue(EventWrapper(MovieListState.MovieListSuccess(it)))
        },{
            state.postValue(EventWrapper(MovieListState.MovieListFailure(it.message!!)))
        })
    }

}