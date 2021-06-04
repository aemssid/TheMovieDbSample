package com.example.savvologytask.ui.person

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.savvologytask.R
import com.example.savvologytask.data.remote.PersonCreditResponse
import com.example.savvologytask.extensions.setAnimatedClickListener
import com.example.savvologytask.helper.DrawableAlwaysCrossFadeFactory
import com.example.savvologytask.ui.movielist.MovieListAdapter
import kotlinx.android.synthetic.main.similar_movie_item.view.*

class PersonAsCrewAdapter(var movieList : List<PersonCreditResponse.CrewDetails>,
                          var onItemClick: (PersonCreditResponse.CrewDetails) -> Unit) : RecyclerView.Adapter<PersonAsCrewAdapter.PersonAsCrewViewHolder>() {


    class PersonAsCrewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            movie: PersonCreditResponse.CrewDetails,
            onItemClick: (PersonCreditResponse.CrewDetails) -> Unit
        ) {
            movie.poster_path.let {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonAsCrewViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.similar_movie_item,parent,false)
        return PersonAsCrewViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonAsCrewViewHolder, position: Int) {
        val item = movieList[position]
        holder.bind(item,onItemClick)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}