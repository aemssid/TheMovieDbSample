package com.example.savvologytask.ui.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import certif.id.app.base.BaseFragment
import com.example.savvologytask.R
import com.example.savvologytask.data.local.MovieDbDao
import com.example.savvologytask.databinding.FragmentFavouriteArtistBinding
import com.example.savvologytask.extensions.setAnimatedClickListener
import com.example.savvologytask.helper.NetInfo
import com.example.savvologytask.model.MovieDetails
import com.example.savvologytask.model.PersonDetails
import com.example.savvologytask.ui.favourites.adapter.FavouriteMoviesAdapter
import com.example.savvologytask.ui.favourites.adapter.FavouritesArtistAdapter
import kotlinx.android.synthetic.main.fragment_favourite_artist.*
import kotlinx.android.synthetic.main.fragment_favourite_movies.*
import kotlinx.android.synthetic.main.view_no_result.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel


class FavouriteArtistFragment : BaseFragment<FavouritesViewModel,FragmentFavouriteArtistBinding>(R.layout.fragment_favourite_artist) {
    override val viewModel: FavouritesViewModel by sharedViewModel()

    val favArtistList = arrayListOf<PersonDetails>()
    var mAdapter : FavouritesArtistAdapter? = null

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
        mAdapter = FavouritesArtistAdapter(favArtistList){
            if(NetInfo.isInternetOn()) {
                val bundle = bundleOf("person_name" to it.name, "person_id" to it.id)
                findNavController().navigate(
                    R.id.action_favouriteArtistFragment_to_personDetailsFragment,
                    bundle
                )
            }else{
                toast(getString(R.string.no_internet_message))
            }
        }

        rv_fav_artist_list.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        movieDbDao.getBookmarkedArtist().observe(viewLifecycleOwner,{
            it?.let {
                if(it.isNotEmpty()){
                    rv_fav_artist_list.visibility = View.VISIBLE
                    grp_no_fav_artist.visibility = View.GONE
                    favArtistList.clear()
                    favArtistList.addAll(it)
                    mAdapter?.notifyDataSetChanged()
                }else{
                    rv_fav_artist_list.visibility = View.GONE
                    grp_no_fav_artist.visibility = View.VISIBLE
                    iv_no_result.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_no_result_available))
                    tv_no_result_title.text = getString(R.string.no_fav_title)
                    tv_no_result_desc.text = getString(R.string.no_fav_message)
                }
            }
        })
    }

    override fun setListeners() {
        bt_search_fav_artist.setOnClickListener {
            if(NetInfo.isInternetOn()) {
                it.setAnimatedClickListener {
                    findNavController().navigate(R.id.action_favouriteArtistFragment_to_searchMovieFragment)
                }
            }else{
                toast(getString(R.string.no_internet_message))
            }

        }
    }


}