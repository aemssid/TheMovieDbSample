package com.example.bmsassessment.ui.moviedetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.savvologytask.R
import com.example.savvologytask.extensions.setAnimatedClickListener
import com.example.savvologytask.helper.DrawableAlwaysCrossFadeFactory
import com.example.savvologytask.model.Movie
import com.example.savvologytask.ui.movielist.MovieListAdapter
import kotlinx.android.synthetic.main.similar_movie_item.view.*
import kotlinx.android.synthetic.main.view_similar_movies.view.*


class SimilarMovieAdapter(var movieList: List<Movie>, val onItemClick: (Movie) -> Unit) :
    RecyclerView.Adapter<SimilarMovieAdapter.SimilarMovieViewHolder>() {

    class SimilarMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            movie: Movie,
            onItemClick: (Movie) -> Unit
        ) {
            movie.posterPath.let {
                Glide.with(itemView.context)
                    .load(MovieListAdapter.IMAGE_BASE_URL + it)
                    .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_movie_poster_placeholder)
                    .error(R.drawable.ic_movie_poster_placeholder)
                    .into(itemView.iv_similar_movie)
            }
            itemView.tv_similar_movie_name.text = movie.title
            itemView.setOnClickListener {
                it.setAnimatedClickListener {
                    onItemClick(movie)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.similar_movie_item, parent, false)
        return SimilarMovieViewHolder(view)
    }

    override fun getItemCount() = movieList.count()

    override fun onBindViewHolder(holder: SimilarMovieViewHolder, position: Int) {
        holder.bind(movieList[position], onItemClick)
    }

}