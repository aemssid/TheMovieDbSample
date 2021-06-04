package com.example.savvologytask.data.remote

import com.example.savvologytask.model.Movie
import com.google.gson.JsonElement
import com.google.gson.JsonObject

data class MultiSearchResponse (
    val page: Int,
    val total_results: Int,
    val total_pages :Int,
    val results: List<JsonObject>
)


