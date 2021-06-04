package com.example.savvologytask.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import certif.id.app.base.BaseViewModel
import com.example.savvologytask.data.local.MovieDbDao
import com.example.savvologytask.data.remote.SearchResultMovieDetails
import com.example.savvologytask.data.remote.SearchResultPersonDetails
import com.example.savvologytask.data.remote.service.MovieService
import com.example.savvologytask.helper.EventWrapper
import com.example.savvologytask.helper.NetInfo
import com.example.savvologytask.ui.movielist.MovieListState
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class SearchViewModel(private val movieDbDao: MovieDbDao) : BaseViewModel() {

    val state = MutableLiveData<EventWrapper<SearchScreenState>>()
    fun state() : LiveData<EventWrapper<SearchScreenState>> = state

    val movieService : MovieService by inject()

    val searchQuery = MutableLiveData<String>("")
    val pageNumberLiveData = MutableLiveData<Int>(1)

    companion object{
        private const val API_KEY = "14d6d178878ae5394f68679fe1402c22"
    }



    fun getSearchResult() = viewModelScope.launch {
        if(!NetInfo.isInternetOn()){
            state.postValue(EventWrapper(SearchScreenState.NoInternet))
            return@launch
        }

        runCatching {
            movieService.searchForQuery(searchQuery.value.toString(), API_KEY,pageNumberLiveData.value!!)
        }.fold({
            state.postValue(EventWrapper(SearchScreenState.SearchResultSuccess(it)))
        },{
            state.postValue(EventWrapper(SearchScreenState.SearchResultFailure))
        })
    }

    /*fun bookmarkMovie(movieDetails: SearchResultMovieDetails) = viewModelScope.launch {
        movieDbDao.insert(movieDetails)
    }

    fun removeBookmarkedMovie(id : Long) = viewModelScope.launch {
        movieDbDao.removeBookmarked(id)
    }

    fun bookmarkPerson(personDetails: SearchResultPersonDetails) = viewModelScope.launch {
        movieDbDao.insert(personDetails)
    }

    fun removeBookmarkedPerson(id : Long) = viewModelScope.launch {
        movieDbDao.removeBookmarkedPerson(id)
    }*/
}