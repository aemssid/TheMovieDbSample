package com.example.savvologytask.data.remote

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "bookmarked_person")
data class SearchResultPersonDetails(

    @ColumnInfo(name = "adult")
    var adult: Boolean? = false,

    @ColumnInfo(name = "gender")
    var gender: Int? = null,

    @PrimaryKey
    var id: Long? = null,

    @ColumnInfo(name = "known_for")
    @TypeConverters(KnownForTypeConverter::class)
    var known_for: List<JsonObject?>? = null,

    @Ignore
    var known_parsed : ArrayList<MediaTypes>? = arrayListOf(),

    @ColumnInfo(name = "known_for_department")
    var known_for_department: String? = null,

    @ColumnInfo(name = "media_type")
    var media_type: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "popularity")
    var popularity: Double? = null,

    @ColumnInfo(name = "profile_path")
    var profile_path: String? = null
)

sealed class MediaTypes{
    data class SearchResultMovieDetails(
        val backdrop_path: String? = null,
        val first_air_date: String? = null,
        val genre_ids: List<Long?>? = null,
        val id: Long? = null,
        val media_type: String? = null,
        val name: String? = null,
        val origin_country: List<String?>? = null,
        val original_language: String? = null,
        val original_name: String? = null,
        val overview: String? = null,
        val popularity: Double? = null,
        val poster_path: String? = null,
        val vote_average: Int? = null,
        val vote_count: Long? = null
    ) : MediaTypes()

    data class SearchResultTvShowDetails(
        val backdrop_path: String? = null,
        val first_air_date: String? = null,
        val genre_ids: List<Int?>? = null,
        val id: Int? = null,
        val media_type: String? = null,
        val name: String? = null,
        val origin_country: List<String?>? = null,
        val original_language: String? = null,
        val original_name: String? = null,
        val overview: String? = null,
        val popularity: Double? = null,
        val poster_path: String? = null,
        val vote_average: Double? = null,
        val vote_count: Int? = null
    ) : MediaTypes()
}


object KnownForTypeConverter{
    val gson : Gson = Gson()
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<JsonObject?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<JsonObject?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<JsonObject?>?): String? {
        return gson.toJson(someObjects)
    }
}

object KnownParsedTypeConverter{
    val gson : Gson = Gson()
    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<MediaTypes?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<MediaTypes?>?>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<MediaTypes?>?): String? {
        return gson.toJson(someObjects)
    }
}