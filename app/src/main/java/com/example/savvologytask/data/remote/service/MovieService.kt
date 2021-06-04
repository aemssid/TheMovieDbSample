package com.example.savvologytask.data.remote.service

import com.example.savvologytask.data.remote.MovieListResponse
import com.example.savvologytask.data.remote.MovieReviewResponse
import com.example.savvologytask.data.remote.MultiSearchResponse
import com.example.savvologytask.data.remote.PersonCreditResponse
import com.example.savvologytask.model.MovieCredits
import com.example.savvologytask.model.MovieDetails
import com.example.savvologytask.model.PersonDetails
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/now_playing")
    suspend fun getMovieList(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): MovieListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id : Long,
        @Query("api_key") apiKey: String
    ): MovieDetails


    /**
     * Get credits information
     * @param apiKey : API key for the movie db
     * @param id : Movie ID
     */
    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") id : Long,
        @Query("api_key") apiKey: String
    ) : MovieCredits

    /**
     * Get review information
     * @param apiKey : API key for the movie db
     * @param id : Movie ID
     */
    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") id : Long,
        @Query("api_key") apiKey: String
    ) : MovieReviewResponse

    /**
     * Get similar movies
     * @param apiKey : API key for the movie db
     * @param id : Movie ID
     */
    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") id : Long,
        @Query("api_key") apiKey: String
    ) : MovieListResponse


    @GET("/3/search/multi")
    suspend fun searchForQuery(
        @Query("query") query : String,
        @Query("api_key") apiKey: String,
        @Query("page") page : Int,
        @Query("adult") adult : Boolean = true
    ) : MultiSearchResponse


    @GET("/3/person/{person_id}")
    suspend fun getPersonDetails(
        @Path("person_id") personId : Long,
        @Query("api_key") apiKey: String
    ) : PersonDetails

    @GET("/3/person/{person_id}/movie_credits")
    suspend fun getPersonCredits(
        @Path("person_id") personId : Long,
        @Query("api_key") apiKey: String
    ) : PersonCreditResponse
}