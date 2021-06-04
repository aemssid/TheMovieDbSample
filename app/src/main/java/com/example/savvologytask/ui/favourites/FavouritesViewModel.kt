package com.example.savvologytask.ui.favourites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import certif.id.app.base.BaseViewModel
import com.example.savvologytask.data.local.MovieDbDao
import com.example.savvologytask.model.MovieDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritesViewModel(private val movieDbDao : MovieDbDao) : BaseViewModel() {

    val _favMoviesList = MutableLiveData<List<MovieDetails>>()
    val favMovieList = _favMoviesList


}