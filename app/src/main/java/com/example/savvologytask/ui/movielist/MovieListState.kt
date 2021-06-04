package com.example.savvologytask.ui.movielist

import com.example.savvologytask.data.remote.MovieListResponse

sealed class MovieListState {
    object Initial : MovieListState()
    object NoInternet : MovieListState()
    object Loading : MovieListState()

    data class MovieListSuccess(var response : MovieListResponse) : MovieListState()
    data class MovieListFailure(var error : String) : MovieListState()
}