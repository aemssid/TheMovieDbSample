package com.example.savvologytask.data.remote


import com.example.savvologytask.model.MovieReview
import com.google.gson.annotations.SerializedName

data class MovieReviewResponse(
    @field:SerializedName("page")val page: Int,

    @field:SerializedName("total_results")
    val totalResults: Int,

    @field:SerializedName("total_pages")
    val totalPages: Int,

    val results: List<MovieReview>
)