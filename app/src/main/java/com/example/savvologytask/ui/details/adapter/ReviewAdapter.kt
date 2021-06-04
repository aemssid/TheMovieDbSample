package com.example.bmsassessment.ui.moviedetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.savvologytask.R

import com.example.savvologytask.model.MovieReview
import kotlinx.android.synthetic.main.review_item_layout.view.*

class ReviewAdapter(var reviewList: List<MovieReview>):
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>(){

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(review: MovieReview){
            itemView.tv_content_review.text = review.content
            itemView.tv_author_review.text = "- "+ review.author
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item_layout,parent,false)
        return ReviewViewHolder(view)
    }

    override fun getItemCount(): Int = reviewList.count()

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviewList[position])
    }
}