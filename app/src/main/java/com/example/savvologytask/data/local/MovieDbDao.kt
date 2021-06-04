package com.example.savvologytask.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.savvologytask.data.remote.SearchResultMovieDetails
import com.example.savvologytask.data.remote.SearchResultPersonDetails
import com.example.savvologytask.model.MovieDetails
import com.example.savvologytask.model.PersonDetails

@Dao
interface MovieDbDao {

    @Insert
    suspend fun insert(movieDetails: MovieDetails)

    @Query("SELECT * FROM movie_details_bookmark")
    fun getBookmarkedMovies() : LiveData<List<MovieDetails>>

    @Query("DELETE FROM movie_details_bookmark WHERE id = :id")
    suspend fun removeBookmarked(id : Long)

    @Query("SELECT EXISTS (SELECT 1 FROM movie_details_bookmark WHERE id = :id)")
    suspend fun checkForBookmark(id: Long?) : Boolean


    @Insert
    suspend fun insert(personDetails : PersonDetails)

    @Query("SELECT * FROM person_details_bookmark")
    fun getBookmarkedArtist() : LiveData<List<PersonDetails>>

    @Query("DELETE FROM person_details_bookmark WHERE id = :id")
    suspend fun removeBookmarkedPerson(id : Long)

    @Query("SELECT EXISTS (SELECT 1 FROM person_details_bookmark WHERE id = :id)")
    suspend fun checkForBookmarkedPerson(id: Long?) : Boolean


}