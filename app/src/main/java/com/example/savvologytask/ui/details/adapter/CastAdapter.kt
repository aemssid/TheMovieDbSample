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
import com.example.savvologytask.model.MovieCredits
import kotlinx.android.synthetic.main.cast_item_layout.view.*

class CastAdapter(var castList: List<MovieCredits.Cast?>,
var onItemClick : (MovieCredits.Cast) -> Unit):
    RecyclerView.Adapter<CastAdapter.CastViewHolder>(){

    companion object{
        private const val CAST_IMAGE_BASE_URL = "https://images.tmdb.org/t/p/w300"
    }

    class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(cast: MovieCredits.Cast, onItemClick: (MovieCredits.Cast) -> Unit){
            itemView.tv_cast_real_name.text = cast.name
            itemView.tv_cast_reel_name.text = cast.character
            cast.profilePath?.let {
                val placeHolder = if(cast.gender == 1) R.drawable.ic_female_placeholder else R.drawable.ic_male_placeholder
                Glide.with(itemView.context)
                    .load(CastAdapter.CAST_IMAGE_BASE_URL + cast.profilePath)
                    .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeHolder)
                    .error(placeHolder)
                    .into(itemView.iv_cast)
            }
            itemView.setOnClickListener {
                it.setAnimatedClickListener {
                    onItemClick(cast)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cast_item_layout,parent,false)
        return CastViewHolder(view)
    }

    override fun getItemCount() = castList.count()

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        castList[position]?.let {
            holder.bind(it,onItemClick)
        }
    }
}