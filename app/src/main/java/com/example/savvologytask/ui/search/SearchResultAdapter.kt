package com.example.savvologytask.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.savvologytask.R
import com.example.savvologytask.data.local.LanguageDetails
import com.example.savvologytask.data.local.MovieDbDao
import com.example.savvologytask.data.remote.SearchResultMovieDetails
import com.example.savvologytask.data.remote.SearchResultPersonDetails
import com.example.savvologytask.data.remote.SearchResultTvShowDetails
import com.example.savvologytask.extensions.setAnimatedClickListener
import com.example.savvologytask.helper.DrawableAlwaysCrossFadeFactory
import com.example.savvologytask.listener.OnLoadMoreListener
import com.example.savvologytask.ui.movielist.MovieListAdapter
import kotlinx.android.synthetic.main.item_layout_movie.view.*
import kotlinx.android.synthetic.main.item_layout_person.view.*
import kotlinx.android.synthetic.main.item_layout_tv_show.view.*
import kotlinx.android.synthetic.main.movie_item.view.tv_movie_name

class SearchResultAdapter(
    private val searchResult: List<Any?>,
    private val languageList: List<LanguageDetails>,
    recyclerView: RecyclerView,
    private val linearLayoutManager: LinearLayoutManager,
    private val onItemClickListener: (Any) -> Unit

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var loading: Boolean = false
    private var rvList: RecyclerView = recyclerView
    private var onLoadMoreListener: OnLoadMoreListener? = null

    companion object{
        val VIEW_TYPE_PERSON = 1
        val VIEW_TYPE_MOVIE = 2
        val VIEW_TYPE_TV_SHOW = 3
    }

    init {
        this.rvList = recyclerView
        rvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int, dy: Int
            ) {
                if (dy > 0) {
                    totalItemCount = linearLayoutManager.getItemCount()
                    lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition()

                    val endHasBeenReached = lastVisibleItem >= (totalItemCount - 1)

                    if ((!loading && totalItemCount > 0 && endHasBeenReached)) {
                        loading = true
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener!!.onLoadMore()
                        }
                    }
                }

            }
        })
    }

    override fun getItemViewType(position: Int): Int {
        if(searchResult[position]!=null) {
            if (searchResult[position] is SearchResultPersonDetails) {
                return VIEW_TYPE_PERSON
            } else if (searchResult[position] is SearchResultMovieDetails) {
                return VIEW_TYPE_MOVIE
            } else {
                return VIEW_TYPE_TV_SHOW
            }
        }else{
            return -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            VIEW_TYPE_PERSON -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_person,parent,false)
                return PersonViewHolder(view)
            }

            VIEW_TYPE_MOVIE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_movie,parent,false)
                return MovieViewHolder(view)
            }

            VIEW_TYPE_TV_SHOW -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_tv_show,parent,false)
                return TvShowViewHolder(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_movie,parent,false)
                return MovieViewHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = searchResult[position]
        if(item is SearchResultPersonDetails && holder is PersonViewHolder){
            holder.bind(item,onItemClickListener)
        }else if(item is SearchResultMovieDetails && holder is MovieViewHolder){
            holder.bind(item,onItemClickListener)
        }else if(item is SearchResultTvShowDetails && holder is TvShowViewHolder){
            holder.bind(item,onItemClickListener)
        }
    }

    override fun getItemCount(): Int {
        return searchResult.size
    }

    class PersonViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        fun bind(personDetails: SearchResultPersonDetails, onItemClickListener: (Any) -> Unit){

            view.tv_person_name.text = personDetails.name
            view.tv_known_for_dept.text = "Artist"

            personDetails.profile_path?.let {
                Glide.with(itemView.context)
                    .load(MovieListAdapter.IMAGE_BASE_URL + personDetails.profile_path)
                    .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(if(personDetails.gender == 1) R.drawable.ic_female_placeholder else R.drawable.ic_male_placeholder)
                    .error(if(personDetails.gender == 1) R.drawable.ic_female_placeholder else R.drawable.ic_male_placeholder)
                    .into(view.iv_person_image)
            } ?: kotlin.run {
                view.iv_person_image.setImageDrawable(ContextCompat.getDrawable(view.context,
                    if(personDetails.gender == 1) R.drawable.ic_female_placeholder else R.drawable.ic_male_placeholder))
            }

            itemView.setOnClickListener {
                it.setAnimatedClickListener {
                    onItemClickListener(personDetails)
                }
            }
        }
    }

    class MovieViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        fun bind(movieDetails: SearchResultMovieDetails, onItemClickListener: (Any) -> Unit) {
            view.tv_movie_name.text = movieDetails.title
            view.tv_media_type.text = "Movie"

            movieDetails.poster_path?.let {
                Glide.with(view.context)
                    .load(MovieListAdapter.IMAGE_BASE_URL + it)
                    .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_movie_poster_placeholder)
                    .error(R.drawable.ic_movie_poster_placeholder)
                    .into(view.iv_movie_image)
            }

            itemView.setOnClickListener {
                onItemClickListener(movieDetails)
            }
        }
    }

    class TvShowViewHolder(var view : View) : RecyclerView.ViewHolder(view){
        fun bind(tvShowDetails: SearchResultTvShowDetails, onItemClickListener: (Any) -> Unit){
            view.tv_show_name.text = tvShowDetails.name
            view.tv_show_type.text = "TV Show"

            tvShowDetails.poster_path?.let {
                Glide.with(view.context)
                    .load(MovieListAdapter.IMAGE_BASE_URL + it)
                    .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_movie_poster_placeholder)
                    .error(R.drawable.ic_movie_poster_placeholder)
                    .into(view.iv_tv_show_image)
            }

            itemView.setOnClickListener {
                onItemClickListener(tvShowDetails)
            }
        }
    }

    fun setLoaded() {
        loading = false
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }
}