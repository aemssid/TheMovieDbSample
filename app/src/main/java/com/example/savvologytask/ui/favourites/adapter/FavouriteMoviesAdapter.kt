package com.example.savvologytask.ui.favourites.adapter

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
import com.example.savvologytask.model.MovieDetails
import com.example.savvologytask.ui.movielist.MovieListAdapter
import kotlinx.android.synthetic.main.item_layout_fav_movies.view.*

class FavouriteMoviesAdapter(
    var favMovieList: ArrayList<MovieDetails>,
    var onItemClickListener: (MovieDetails) -> Unit
) : RecyclerView.Adapter<FavouriteMoviesAdapter.FavouriteMovieViewHolder>() {


    class FavouriteMovieViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: MovieDetails, onItemClickListener: (MovieDetails) -> Unit) {
            if (movie != null) {
                itemView.iv_adult_movie.visibility = if(movie.adult!!) View.VISIBLE else View.GONE
                itemView.tv_fav_movie_name.text = movie.title
                itemView.tv_fav_movie_release_date.text = """Released on ${movie.releaseDate}"""
                Glide.with(itemView.context)
                    .load(MovieListAdapter.IMAGE_BASE_URL + movie.backdropPath)
                    .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_movie_banner_placeholder)
                    .error(R.drawable.ic_movie_banner_placeholder)
                    .into(itemView.iv_poster_fav_movie)
                itemView.setOnClickListener {
                    it.setAnimatedClickListener {
                        onItemClickListener(movie)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_fav_movies, parent, false)
        return FavouriteMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteMovieViewHolder, position: Int) {
        val movieDetails = favMovieList[position]
        holder.bind(movieDetails,onItemClickListener)
    }

    override fun getItemCount(): Int {
        return favMovieList.size
    }
}