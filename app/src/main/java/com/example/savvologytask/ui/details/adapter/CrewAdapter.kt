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

class CrewAdapter(var crewList: List<MovieCredits.Crew?>,
                  var onItemClick : (MovieCredits.Crew) -> Unit) :
    RecyclerView.Adapter<CrewAdapter.CrewViewHolder>() {

    companion object {
        private const val CREW_IMAGE_BASE_URL = "https://images.tmdb.org/t/p/w300"
    }

    class CrewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(crew: MovieCredits.Crew, onItemClick: (MovieCredits.Crew) -> Unit) {
            itemView.tv_cast_real_name.text = crew.name
            itemView.tv_cast_reel_name.text = """as ${crew.department}"""
            val placeHolder =
                if (crew.gender == 1) R.drawable.ic_female_placeholder else R.drawable.ic_male_placeholder
            Glide.with(itemView.context)
                .load(CrewAdapter.CREW_IMAGE_BASE_URL + crew.profilePath)
                .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(itemView.iv_cast)
            itemView.setOnClickListener {
                it.setAnimatedClickListener {
                    onItemClick(crew)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cast_item_layout, parent, false)
        return CrewViewHolder(view)
    }

    override fun getItemCount(): Int = crewList.count()

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        crewList[position]?.let {
            holder.bind(it,onItemClick)
        }
    }

}