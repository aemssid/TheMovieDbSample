package com.example.savvologytask.ui.search

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import certif.id.app.base.BaseFragment
import com.example.savvologytask.R
import com.example.savvologytask.data.local.LanguageDetails
import com.example.savvologytask.data.local.MovieDatabase
import com.example.savvologytask.data.local.MovieDbDao
import com.example.savvologytask.data.remote.*
import com.example.savvologytask.databinding.FragmentSearchMovieBinding
import com.example.savvologytask.extensions.setAnimatedClickListener
import com.example.savvologytask.helper.NetInfo
import com.example.savvologytask.listener.OnLoadMoreListener
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.android.synthetic.main.fragment_search_movie.*
import kotlinx.android.synthetic.main.view_no_result.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class SearchMovieFragment : BaseFragment<SearchViewModel,FragmentSearchMovieBinding>(R.layout.fragment_search_movie) {
    override val viewModel: SearchViewModel by viewModel()

    override fun setViewModel() {
        binding.viewModel = viewModel
    }

    val searchResults = arrayListOf<Any?>()
    var mAdapter : SearchResultAdapter? = null

    lateinit var movieDbDao: MovieDbDao

    private var previousSize = 0
    private var totalRecords = 0
    private var mLoading = false


    val gson : Gson by inject()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setListeners()

        viewModel.getSearchResult()
    }

    override fun initUi() {
        mAdapter = SearchResultAdapter(
            searchResults,
            LanguageDetails.initLanguageList(resources),
            rv_movie_search_result,
            rv_movie_search_result.layoutManager as LinearLayoutManager){ it ->
            if(NetInfo.isInternetOn()) {
                when (it) {
                    is SearchResultMovieDetails -> {
                        val bundle = bundleOf("movie_name" to it.title, "movie_id" to it.id)
                        findNavController().navigate(
                            R.id.action_searchMovieFragment_to_movieDetailsFragment,
                            bundle
                        )
                    }

                    is SearchResultTvShowDetails -> {
                        toast(getString(R.string.no_tv_show_support))
                    }

                    is SearchResultPersonDetails -> {
                        val bundle = bundleOf("person_id" to it.id, "person_name" to it.name)
                        findNavController().navigate(
                            R.id.action_searchMovieFragment_to_personDetailsFragment,
                            bundle
                        )
                    }
                }
            }else{
                toast(getString(R.string.no_internet_message))
            }
        }


        mAdapter?.setOnLoadMoreListener(object : OnLoadMoreListener{
            override fun onLoadMore() {
                if(totalRecords > searchResults.size) {
                    if(NetInfo.isInternetOn()) {
                        loadMoreData()
                    }else{
                        toast(getString(R.string.no_internet_message))
                    }
                }
            }

        })

        rv_movie_search_result.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }

        viewModel.state().observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                renderState(it)
            }
        })
    }

    private fun renderState(state: SearchScreenState) {
        when(state){
            SearchScreenState.Initial -> {

            }
            SearchScreenState.Loading -> {

            }
            SearchScreenState.NoInternet -> {

            }
            SearchScreenState.SearchResultFailure -> {

            }
            is SearchScreenState.SearchResultSuccess -> {
                convertIntoModels(state.response)
            }
        }
    }

    private fun convertIntoModels(response: MultiSearchResponse) {
        mLoading = false


        if (searchResults.size > 0 && searchResults[searchResults.size - 1] == null) {
            searchResults.removeAt(searchResults.size - 1)
            mAdapter?.notifyItemRemoved(searchResults.size)
        }

        previousSize = searchResults.size
        if (viewModel.pageNumberLiveData.value == 1) {
            searchResults.clear()
            mAdapter?.notifyDataSetChanged()
        }

        response.results.forEach {
            when(it.get("media_type").asString){
                "person" ->{
                    val personDetails = gson.fromJson<SearchResultPersonDetails>(it,SearchResultPersonDetails::class.java)
                    personDetails.known_for?.forEach {  mediaDetails ->
                        when(mediaDetails?.get("media_type")?.asString){
                            "movie" -> {
                                val movieDetails = gson.fromJson<MediaTypes.SearchResultMovieDetails>(mediaDetails,MediaTypes.SearchResultMovieDetails::class.java)
                                personDetails.known_parsed?.add(movieDetails)
                            }

                            "tv" -> {
                                val tvShowDetails = gson.fromJson<MediaTypes.SearchResultTvShowDetails>(mediaDetails,MediaTypes.SearchResultTvShowDetails::class.java)
                                personDetails.known_parsed?.add(tvShowDetails)
                            }
                        }
                    }
                    searchResults.add(personDetails)
                }

                "movie" -> {
                    val movieDetails = gson.fromJson<SearchResultMovieDetails>(it,SearchResultMovieDetails::class.java)
                    searchResults.add(movieDetails)
                }

                "tv" -> {
                    val tvShowDetails = gson.fromJson<SearchResultTvShowDetails>(it,SearchResultTvShowDetails::class.java)
                    searchResults.add(tvShowDetails)
                }
            }
        }

        totalRecords = response.total_results
        if (previousSize == 0) {
            mAdapter?.notifyDataSetChanged()
        } else {
            mAdapter?.notifyItemRangeInserted(previousSize, searchResults.size)
        }

        mAdapter?.setLoaded()

    }

    override fun setListeners() {
        viewModel.searchQuery.observe(viewLifecycleOwner, Observer {
            if(NetInfo.isInternetOn()) {
                grp_no_internet_search.visibility = View.GONE
                rv_movie_search_result.visibility = View.VISIBLE
                if (it.isNotEmpty()) {
                    viewModel.pageNumberLiveData.value = 1
                    searchResults.clear()
                    rv_movie_search_result.visibility = View.VISIBLE
                    viewModel.getSearchResult()
                } else {
                    rv_movie_search_result.visibility = View.GONE
                }
            }else{
                grp_no_internet_search.visibility = View.VISIBLE
                rv_movie_search_result.visibility = View.GONE
                iv_no_result.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_no_internet
                    )
                )
                tv_no_result_title.text = getString(R.string.no_internet_title)
                tv_no_result_desc.text = getString(R.string.no_internet_desc)
            }
        })

        bt_go_to_fav.setOnClickListener {
            it.setAnimatedClickListener {
                findNavController().navigate(R.id.action_searchMovieFragment_to_favouritesHomeFragment)
            }
        }
    }

    private fun loadMoreData() {
        if (!mLoading) {
            searchResults.add(null)
            rv_movie_search_result.post {
                mAdapter?.notifyItemInserted(searchResults.size - 1)
            }
            mLoading = true
            viewModel.pageNumberLiveData.value =
                viewModel.pageNumberLiveData.value!!.plus(1)
            viewModel.getSearchResult()
        } else {
            mAdapter?.setLoaded()
        }
    }

}