package com.example.savvologytask.data.remote

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

@Entity(tableName = "bookmarked_movies")
data class SearchResultMovieDetails(
    @ColumnInfo(name = "backdrop_path")
    val backdrop_path: String? = null,

    @ColumnInfo(name = "first_air_date")
    val first_air_date: String? = null,

    @ColumnInfo(name = "genre_ids")
    @TypeConverters(GenreIdTypeConverter::class)
    val genre_ids: List<Long>?,

    @PrimaryKey
    val id: Long? = null,

    @ColumnInfo(name = "media_type")
    val media_type: String? = null,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "origin_country_json")
    @TypeConverters(CountryTypeConverter::class)
    val origin_country: List<String>?,

    @ColumnInfo(name = "original_language")
    val original_language: String? = null,

    @ColumnInfo(name = "original_name")
    val original_name: String? = null,

    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo(name = "overview")
    val overview: String? = null,

    @ColumnInfo(name = "popularity")
    val popularity: Double? = null,

    @ColumnInfo(name = "poster_path")
    val poster_path: String? = null,

    @ColumnInfo(name = "vote_average")
    val vote_average: Int? = null,

    @ColumnInfo(name = "vote_count")
    val vote_count: Long? = null
)


object CountryTypeConverter{
    @TypeConverter
    @JvmStatic
    fun fromList(value: List<String>?) = if(value != null) Gson().toJson(value) else null

    @TypeConverter
    @JvmStatic
    fun toList(value: String?) : List<String>? {
        return if(value == null){
            null
        }else {
            val listType: Type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(value, listType)
        }
    }
}

object GenreIdTypeConverter{
    val gson : Gson = Gson()
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Long?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<Long?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Long?>?): String? {
        return gson.toJson(someObjects)
    }
}