package com.example.savvologytask.di

import com.example.savvologytask.ui.details.MovieDetailsViewModel
import com.example.savvologytask.ui.favourites.FavouritesViewModel
import com.example.savvologytask.ui.movielist.MovieListViewModel
import com.example.savvologytask.ui.person.PersonDetailsViewModel
import com.example.savvologytask.ui.search.SearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val vmModule = module {
    viewModel { MovieListViewModel() }
    viewModel { MovieDetailsViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { FavouritesViewModel(get()) }
    viewModel { PersonDetailsViewModel(get()) }
}