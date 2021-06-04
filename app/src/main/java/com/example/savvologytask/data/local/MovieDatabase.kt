package com.example.savvologytask.data.local

import android.content.Context
import androidx.room.*
import com.example.savvologytask.data.remote.*
import com.example.savvologytask.model.MovieDetails
import com.example.savvologytask.model.PersonDetails

@Database(entities = [MovieDetails::class,PersonDetails::class], version = 1, exportSchema = false)
abstract class MovieDatabase : RoomDatabase() {

    abstract val movieDbDao: MovieDbDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "movie_database",
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE =instance
                }

                return instance
            }
        }
    }
}