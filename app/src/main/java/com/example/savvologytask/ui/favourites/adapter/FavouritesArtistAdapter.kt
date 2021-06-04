package com.example.savvologytask.ui.favourites.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.savvologytask.R
import com.example.savvologytask.extensions.setAnimatedClickListener
import com.example.savvologytask.helper.DrawableAlwaysCrossFadeFactory
import com.example.savvologytask.model.PersonDetails
import com.example.savvologytask.ui.movielist.MovieListAdapter
import kotlinx.android.synthetic.main.item_layout_fav_artists.view.*
import kotlinx.android.synthetic.main.item_layout_person.view.*

class FavouritesArtistAdapter(
    var favArtistList: ArrayList<PersonDetails>,
    var onItemClickListener: (PersonDetails) -> Unit) :  RecyclerView.Adapter<FavouritesArtistAdapter.FavouriteArtistViewHolder>(){


    class FavouriteArtistViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(person: PersonDetails, onItemClickListener: (PersonDetails) -> Unit) {
            if (person != null) {
                itemView.tv_artist_name.text = person.name
                itemView.tv_artist_known_for.text = itemView.context.getString(R.string.known_for,person.known_for_department)
                itemView.tv_artist_dob.text = itemView.context.getString(R.string.dob_artist,person.birthday)
                itemView.tv_artist_popularity.text = itemView.context.getString(R.string.artist_popularity,person.popularity.toString())
                person.profile_path?.let {
                    Glide.with(itemView.context)
                        .load(MovieListAdapter.IMAGE_BASE_URL + person.profile_path)
                        .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(if(person.gender == 1) R.drawable.ic_female_placeholder else R.drawable.ic_male_placeholder)
                        .error(if(person.gender == 1) R.drawable.ic_female_placeholder else R.drawable.ic_male_placeholder)
                        .into(itemView.iv_artist_image)
                } ?: kotlin.run {
                    itemView.iv_person_image.setImageDrawable(
                        ContextCompat.getDrawable(itemView.context,
                        if(person.gender == 1) R.drawable.ic_female_placeholder else R.drawable.ic_male_placeholder))
                }

                itemView.setOnClickListener {
                    it.setAnimatedClickListener {
                        onItemClickListener(person)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_fav_artists,parent,false)
        return FavouriteArtistViewHolder(itemView = view)
    }

    override fun onBindViewHolder(holder: FavouriteArtistViewHolder, position: Int) {
        val item = favArtistList[position]
        holder.bind(item,onItemClickListener)
    }

    override fun getItemCount(): Int = favArtistList.size


}