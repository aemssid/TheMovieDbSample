package com.example.savvologytask.data.remote

import androidx.annotation.Nullable
import androidx.room.*
import com.example.savvologytask.data.local.MovieTypeConverter
import com.example.savvologytask.model.Movie
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tbl_movie_parent")
data class MovieListResponse(
    @PrimaryKey @field:SerializedName("page")val page: Int,

    @field:SerializedName("total_results")
    val totalResults: Int,

    @field:SerializedName("total_pages")
    val totalPages: Int,

    @ColumnInfo(name = "movie_list_json")
    @TypeConverters(MovieTypeConverter::class)
    val results: List<Movie>
)