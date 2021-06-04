package com.example.savvologytask.ui.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import certif.id.app.base.BaseFragment
import com.example.savvologytask.R
import com.example.savvologytask.databinding.FragmentFavouritesHomeBinding
import com.example.savvologytask.extensions.setAnimatedClickListener
import kotlinx.android.synthetic.main.fragment_favourites_home.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class FavouritesHomeFragment : BaseFragment<FavouritesViewModel,FragmentFavouritesHomeBinding>(R.layout.fragment_favourites_home) {
    override val viewModel: FavouritesViewModel by sharedViewModel()

    override fun setViewModel() {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setListeners()
    }

    override fun initUi() {

    }

    override fun setListeners() {
        mcv_fav_movies.setOnClickListener {
            it.setAnimatedClickListener {
                findNavController().navigate(R.id.action_favouritesHomeFragment_to_favouriteMoviesFragment)
            }
        }

        mcv_fav_artists.setOnClickListener {
            it.setAnimatedClickListener {
                findNavController().navigate(R.id.action_favouritesHomeFragment_to_favouriteArtistFragment)
            }
        }
    }


}