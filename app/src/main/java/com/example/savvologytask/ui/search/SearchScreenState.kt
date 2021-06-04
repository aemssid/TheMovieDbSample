package com.example.savvologytask.ui.search

import com.example.savvologytask.data.remote.MultiSearchResponse
import com.example.savvologytask.ui.movielist.MovieListState

sealed class SearchScreenState {
    object Initial : SearchScreenState()
    object NoInternet : SearchScreenState()
    object Loading : SearchScreenState()

    data class SearchResultSuccess(var response : MultiSearchResponse) : SearchScreenState()
    object SearchResultFailure : SearchScreenState()

}