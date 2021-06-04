package com.example.savvologytask.ui.movielist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import certif.id.app.base.BaseFragment
import com.example.savvologytask.R
import com.example.savvologytask.data.local.LanguageDetails
import com.example.savvologytask.data.remote.MovieListResponse
import com.example.savvologytask.databinding.FragmentMovieListBinding
import com.example.savvologytask.extensions.setAnimatedClickListener
import com.example.savvologytask.helper.NetInfo
import com.example.savvologytask.listener.OnLoadMoreListener
import com.example.savvologytask.model.Movie
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.android.synthetic.main.view_no_result.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieListFragment :
    BaseFragment<MovieListViewModel, FragmentMovieListBinding>(R.layout.fragment_movie_list) {
    override val viewModel: MovieListViewModel by viewModel()

    override fun setViewModel() {
        binding.viewModel = viewModel
    }

    private var movieList = ArrayList<Movie?>()
    private var mAdapter: MovieListAdapter? = null

    private var previousSize = 0
    private var totalRecords = 0
    private var mLoading = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setListeners()

        viewModel.fetchNowPlaying()
    }

    override fun initUi() {
        mAdapter = MovieListAdapter(
            movieList, LanguageDetails.initLanguageList(resources),
            rv_movie_list,
            rv_movie_list.layoutManager as LinearLayoutManager
        ) {
            if (NetInfo.isInternetOn()) {
                val bundle = bundleOf("movie_name" to it.title, "movie_id" to it.id)
                findNavController().navigate(
                    R.id.action_movieListFragment_to_movieDetailsFragment,
                    bundle
                )
            }else{
                toast(getString(R.string.no_internet_message))
            }
        }

        mAdapter?.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                loadMoreData()
            }
        })

        rv_movie_list.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        viewModel.state().observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                renderState(it)
            }
        })
    }

    private fun loadMoreData() {
        if (!mLoading) {
            movieList.add(null)
            rv_movie_list.post {
                mAdapter?.notifyItemInserted(movieList.size - 1)
            }
            mLoading = true
            viewModel.pageNumberLiveData.value =
                viewModel.pageNumberLiveData.value!!.plus(1)
            viewModel.fetchNowPlaying()
        } else {
            mAdapter?.setLoaded()
        }
    }


    private fun renderState(state: MovieListState) {
        when (state) {
            MovieListState.Initial -> {

            }
            MovieListState.Loading -> {
                showProgressDialog()
            }
            is MovieListState.MovieListFailure -> {
                toast(state.error)
            }
            is MovieListState.MovieListSuccess -> {
                showMovies(state.response)
            }
            MovieListState.NoInternet -> {
                rv_movie_list.visibility = View.GONE
                grp_no_internet_home.visibility = View.VISIBLE
                iv_no_result.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_no_internet
                    )
                )
                tv_no_result_title.text = getString(R.string.no_internet_title)
                tv_no_result_desc.text = getString(R.string.no_internet_desc)
            }
        }
    }

    private fun showMovies(response: MovieListResponse) {
        mLoading = false

        if (movieList.size > 0 && movieList[movieList.size - 1] == null) {
            movieList.removeAt(movieList.size - 1)
            mAdapter?.notifyItemRemoved(movieList.size)
        }


        previousSize = movieList.size
        if (viewModel.pageNumberLiveData.value == 1) {
            movieList.clear()
        }

        movieList.addAll(response.results)

        totalRecords = response.totalResults
        if (previousSize == 0) {
            mAdapter?.notifyDataSetChanged()
        } else {
            mAdapter?.notifyItemRangeInserted(previousSize, movieList.size)
        }

        mAdapter?.setLoaded()
    }

    override fun setListeners() {
        iv_search_movie.setOnClickListener {
            if (NetInfo.isInternetOn()) {
                findNavController().navigate(R.id.action_movieListFragment_to_searchMovieFragment)
            } else {
                toast(getString(R.string.no_internet_message))
            }
        }

        iv_favourites_movie.setOnClickListener {
            findNavController().navigate(R.id.action_movieListFragment_to_favouritesHomeFragment)
        }

        bt_search_fav_home.setOnClickListener {
            it.setAnimatedClickListener {
                findNavController().navigate(R.id.action_movieListFragment_to_favouritesHomeFragment)
            }
        }
    }

}