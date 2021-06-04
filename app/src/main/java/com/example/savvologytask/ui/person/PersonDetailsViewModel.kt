package com.example.savvologytask.ui.person

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import certif.id.app.base.BaseViewModel
import com.example.savvologytask.data.local.MovieDbDao
import com.example.savvologytask.data.remote.PersonCreditResponse
import com.example.savvologytask.data.remote.service.MovieService
import com.example.savvologytask.helper.EventWrapper
import com.example.savvologytask.helper.NetInfo
import com.example.savvologytask.model.PersonDetails
import kotlinx.coroutines.launch
import org.koin.core.component.inject

class PersonDetailsViewModel(private val movieDbDao: MovieDbDao) : BaseViewModel() {

    val state = MutableLiveData<EventWrapper<PersonDetailsState>>()
    fun state() = state

    private val _personDetailsResponse = MutableLiveData<PersonDetails>()
    val personDetails: LiveData<PersonDetails>
        get() = _personDetailsResponse

    private val _personCreditResponse = MutableLiveData<PersonCreditResponse>()
    val personCredit: LiveData<PersonCreditResponse>
        get() = _personCreditResponse

    private val _isBookMarked = MutableLiveData<Boolean>(false)
    val isBookMarked: LiveData<Boolean>
        get() = _isBookMarked

    companion object {
        private const val API_KEY = "14d6d178878ae5394f68679fe1402c22"
    }

    val movieService: MovieService by inject()


    fun getPersonDetails(personId: Long) = viewModelScope.launch {
        if (!NetInfo.isInternetOn()) {
            state.postValue(EventWrapper(PersonDetailsState.NoInternet))
            return@launch
        }

        runCatching {
            movieService.getPersonDetails(personId, API_KEY)
        }.fold({
            _personDetailsResponse.postValue(it)
        }, {
            state.postValue(EventWrapper(PersonDetailsState.PersonDetailsFailure(it.message!!)))
        })
    }

    fun getPersonCredits(personId: Long) = viewModelScope.launch {
        if(!NetInfo.isInternetOn()){
            state.postValue(EventWrapper(PersonDetailsState.NoInternet))
            return@launch
        }

        runCatching {
            movieService.getPersonCredits(personId, API_KEY)
        }.fold({
            _personCreditResponse.postValue(it)
        },{
            state.postValue(EventWrapper(PersonDetailsState.PersonCreditsFailure(it.message!!)))
        })
    }

    fun isBookMarked() = viewModelScope.launch {
        _isBookMarked.postValue(movieDbDao.checkForBookmarkedPerson(personDetails.value!!.id))
    }

    fun bookmarkPerson() = viewModelScope.launch {
        movieDbDao.insert(personDetails.value!!)
    }

    fun removeBookmarkedPerson() = viewModelScope.launch {
        movieDbDao.removeBookmarkedPerson(personDetails.value!!.id!!)

    }
}