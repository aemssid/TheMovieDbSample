package com.example.savvologytask.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import certif.id.app.base.BaseViewModel
import com.example.savvologytask.data.local.MovieDbDao
import com.example.savvologytask.data.remote.MovieListResponse
import com.example.savvologytask.data.remote.MovieReviewResponse
import com.example.savvologytask.data.remote.SearchResultMovieDetails
import com.example.savvologytask.data.remote.SearchResultPersonDetails
import com.example.savvologytask.data.remote.service.MovieService
import com.example.savvologytask.helper.NetInfo
import com.example.savvologytask.model.MovieCredits
import com.example.savvologytask.model.MovieDetails
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class MovieDetailsViewModel(private val movieDbDao: MovieDbDao) : BaseViewModel() {

    private val _movieDetailsResponse = MutableLiveData<MovieDetails>()
    val movieDetails : LiveData<MovieDetails>
        get() = _movieDetailsResponse

    //Movie details (Synopsis) Error
    private val _movieDetailError = MutableLiveData<String>()
    val movieDetailsError : LiveData<String>
        get() = _movieDetailError

    //Movie Credit (Synopsis) Live Data
    private val _movieCreditsResponse = MutableLiveData<MovieCredits>()
    val movieCredits : LiveData<MovieCredits>
        get() = _movieCreditsResponse

    //Movie Credit (Synopsis) Error
    private val _movieCreditError = MutableLiveData<String>()
    val movieCreditError : LiveData<String>
        get() = _movieCreditError

    //Movie Review (Synopsis) Live Data
    private val _movieReviewsResponse = MutableLiveData<MovieReviewResponse>()
    val movieReviews : LiveData<MovieReviewResponse>
        get() = _movieReviewsResponse

    //Movie Review (Synopsis) Error
    private val _movieReviewError = MutableLiveData<String>()
    val movieReviewError : LiveData<String>
        get() = _movieReviewError

    //Similar Movie Live Data
    private val _similarMoviesResponse = MutableLiveData<MovieListResponse>()
    val similarMovies : LiveData<MovieListResponse>
        get() = _similarMoviesResponse

    //Similar Movie Error
    private val _similarMovieError = MutableLiveData<String>()
    val similarMovieError : LiveData<String>
        get() = _similarMovieError


    private val _internetConnectionError = MutableLiveData<Boolean>(true)
    val internetAvailable : LiveData<Boolean>
        get() = _internetConnectionError


    private val _isBookMarked = MutableLiveData<Boolean>(false)
    val isBookMarked : LiveData<Boolean>
        get() = _isBookMarked

    companion object{
        private const val API_KEY = "14d6d178878ae5394f68679fe1402c22"
    }

    val movieService : MovieService by inject()

    fun getMovieDetails(movieId: Long) = viewModelScope.launch {
        if(!NetInfo.isInternetOn()){
            return@launch
        }

        runCatching {
            movieService.getMovieDetails(movieId, API_KEY)
        }.fold({
            _movieDetailsResponse.postValue(it)
        },{
            _movieDetailError.postValue(it.message)
        })
    }


    fun getMovieCredits(movieId: Long) = viewModelScope.launch {
        if(!NetInfo.isInternetOn()){
            return@launch
        }

        runCatching {
            movieService.getMovieCredits(movieId, API_KEY)
        }.fold({
            _movieCreditsResponse.postValue(it)
        },{
            _movieCreditError.postValue(it.message)
        })
    }


    fun getMovieReviews(movieId: Long) = viewModelScope.launch {
        if(!NetInfo.isInternetOn()){
            return@launch
        }

        runCatching {
            movieService.getMovieReviews(movieId, API_KEY)
        }.fold({
            _movieReviewsResponse.postValue(it)
        },{
            _movieReviewError.postValue(it.message)
        })
    }

    fun getSimilarMovies(movieId: Long) = viewModelScope.launch {
        if(!NetInfo.isInternetOn()){
            return@launch
        }

        runCatching {
            movieService.getSimilarMovies(movieId, API_KEY)
        }.fold({
            _similarMoviesResponse.postValue(it)
        },{
            _similarMovieError.postValue(it.message)
        })
    }

    fun isBookMarked() = viewModelScope.launch {
         _isBookMarked.postValue(movieDbDao.checkForBookmark(movieDetails.value!!.id))

    }

    fun bookmarkMovie() = viewModelScope.launch {
        movieDbDao.insert(movieDetails.value!!)
    }

    fun removeBookmarkedMovie() = viewModelScope.launch {
        movieDbDao.removeBookmarked(movieDetails.value!!.id!!)
    }
}