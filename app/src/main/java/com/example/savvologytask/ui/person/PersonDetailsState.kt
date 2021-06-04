package com.example.savvologytask.ui.person

import com.example.savvologytask.model.PersonDetails

sealed class PersonDetailsState {
    object Initial : PersonDetailsState()
    object NoInternet : PersonDetailsState()
    object Loading : PersonDetailsState()

    data class PersonDetailsSuccess(var personDetails: PersonDetails) : PersonDetailsState()
    data class PersonDetailsFailure(var error : String) : PersonDetailsState()
    data class PersonCreditsFailure(var error : String) : PersonDetailsState()
}