package com.example.savvologytask.ui.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.example.savvologytask.R
import com.example.savvologytask.data.local.LanguageDetails
import com.example.savvologytask.databinding.MovieItemBinding
import com.example.savvologytask.extensions.setAnimatedClickListener
import com.example.savvologytask.helper.DrawableAlwaysCrossFadeFactory
import com.example.savvologytask.listener.OnLoadMoreListener
import com.example.savvologytask.model.Movie
import kotlinx.android.synthetic.main.movie_item.view.*


class MovieListAdapter(
    private val movieList: List<Movie?>,
    private val languageList: List<LanguageDetails>,
    recyclerView: RecyclerView,
    private val linearLayoutManager: LinearLayoutManager,
    private val onItemClickListener: (Movie) -> Unit
) :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var loading: Boolean = false
    private var rvList: RecyclerView = recyclerView
    private var onLoadMoreListener: OnLoadMoreListener? = null

    companion object {
        const val IMAGE_BASE_URL = "https://images.tmdb.org/t/p/w500"
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

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            movie: Movie?,
            languageList: List<LanguageDetails>,
            onItemClickListener: (Movie) -> Unit
        ) {
            val factory =
                DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

            if (movie != null) {
                itemView.tv_movie_name.text = movie.title
                itemView.tv_movie_release_date.text = """Released on ${movie.releaseDate}"""
                itemView.tv_movie_average.text = movie.voteAverage
                itemView.tv_movie_original_lang.text =
                    languageList.find { it.iso_639_1 == movie.originalLanguage }?.english_name
                Glide.with(itemView.context)
                    .load(IMAGE_BASE_URL + movie.backdropPath)
                    .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_movie_banner_placeholder)
                    .error(R.drawable.ic_movie_banner_placeholder)
                    .into(itemView.iv_poster)
                itemView.setOnClickListener {
                    it.setAnimatedClickListener {
                        onItemClickListener(movie)
                    }
                }
                /*itemView.setOnClickListener {
                    it.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).start()
                    *//*onItemClickListener(movie)*//*
                    it.animate().scaleX(0.8f).scaleY(0.8f).setDuration(200).start()
                }*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item,parent,false)
        return MovieViewHolder(view)
    }


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position], languageList, onItemClickListener)
    }

    override fun getItemCount(): Int = movieList.count()

    fun setLoaded() {
        loading = false
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

}
