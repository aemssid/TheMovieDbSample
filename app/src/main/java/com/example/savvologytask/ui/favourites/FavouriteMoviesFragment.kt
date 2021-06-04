package com.example.savvologytask.ui.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import certif.id.app.base.BaseFragment
import com.example.savvologytask.R
import com.example.savvologytask.data.local.MovieDbDao
import com.example.savvologytask.databinding.FragmentFavouriteMoviesBinding
import com.example.savvologytask.extensions.setAnimatedClickListener
import com.example.savvologytask.helper.NetInfo
import com.example.savvologytask.model.MovieDetails
import com.example.savvologytask.ui.favourites.adapter.FavouriteMoviesAdapter
import kotlinx.android.synthetic.main.fragment_favourite_movies.*
import kotlinx.android.synthetic.main.view_no_result.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class FavouriteMoviesFragment : BaseFragment<FavouritesViewModel,FragmentFavouriteMoviesBinding>(R.layout.fragment_favourite_movies) {
    override val viewModel: FavouritesViewModel by sharedViewModel()

    val favMovieList = arrayListOf<MovieDetails>()
    var mAdapter : FavouriteMoviesAdapter? = null

    override fun setViewModel() {
        binding.viewModel = viewModel
    }

    val movieDbDao : MovieDbDao by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setListeners()

    }

    override fun initUi() {
        mAdapter = FavouriteMoviesAdapter(
            favMovieList
        ) {
            if(NetInfo.isInternetOn()) {
                val bundle = bundleOf("movie_name" to it.title, "movie_id" to it.id)
                findNavController().navigate(
                    R.id.action_favouriteMoviesFragment_to_movieDetailsFragment,
                    bundle
                )
            }else{
                toast(getString(R.string.no_internet_message))
            }
        }

        rv_fav_movie_list.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        movieDbDao.getBookmarkedMovies().observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it.isNotEmpty()){
                    rv_fav_movie_list.visibility = View.VISIBLE
                    grp_no_fav_movie.visibility = View.GONE
                    favMovieList.clear()
                    favMovieList.addAll(it)
                    mAdapter?.notifyDataSetChanged()
                }else{
                    favMovieList.clear()
                    rv_fav_movie_list.visibility = View.GONE
                    grp_no_fav_movie.visibility = View.VISIBLE
                    iv_no_result.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_no_result_available))
                    tv_no_result_title.text = "Likes Nothing?"
                    tv_no_result_desc.text = "Still you can search for anything, Who knows you will fine what you're looking for?"
                }
            }
        })


    }

    override fun setListeners() {
        bt_search_fav_movie.setOnClickListener {
            if(NetInfo.isInternetOn()) {
                it.setAnimatedClickListener {
                    findNavController().navigate(R.id.action_favouriteMoviesFragment_to_searchMovieFragment)
                }
            }else{
                toast(getString(R.string.no_internet_message))
            }
        }
    }


}