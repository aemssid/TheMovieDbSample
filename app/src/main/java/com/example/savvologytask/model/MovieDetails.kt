package com.example.savvologytask.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "movie_details_bookmark")
data class MovieDetails(

    @ColumnInfo(name = "adult")
    @SerializedName("adult")
    var adult: Boolean? = false,

    @ColumnInfo(name = "backdropPath")
    @SerializedName("backdrop_path")
    var backdropPath: String? = "",

    @Ignore
    @SerializedName("budget")
    var budget: Long? = 0,

    @Ignore
    @SerializedName("genres")
    var genres: List<Genre?>? = listOf(),

    @PrimaryKey
    @SerializedName("id")
    var id: Long? = 0,

    @Ignore
    @SerializedName("imdb_id")
    var imdbId: String? = "",

    @Ignore
    @SerializedName("original_language")
    var originalLanguage: String? = "",

    @ColumnInfo(name = "originalTitle")
    @SerializedName("original_title")
    var originalTitle: String? = "",

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    var overview: String? = "",

    @Ignore
    @SerializedName("popularity")
    var popularity: Double? = 0.0,


    @ColumnInfo(name = "posterPath")
    @SerializedName("poster_path")
    var posterPath: String? = "",

    @Ignore
    @SerializedName("production_companies")
    var productionCompanies: List<ProductionCompany?>? = listOf(),

    @Ignore
    @SerializedName("production_countries")
    var productionCountries: List<ProductionCountry?>? = listOf(),

    @ColumnInfo(name = "releaseDate")
    @SerializedName("release_date")
    var releaseDate: String? = "",

    @Ignore
    @SerializedName("revenue")
    var revenue: Int? = 0,

    @ColumnInfo(name = "runtime")
    @SerializedName("runtime")
    var runtime: Int? = 0,

    @Ignore
    @SerializedName("spoken_languages")
    var spokenLanguages: List<SpokenLanguage?>? = listOf(),

    @Ignore
    @SerializedName("status")
    var status: String? = "",

    @Ignore
    @SerializedName("tagline")
    var tagline: String? = "",

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String? = "",

    @Ignore
    @SerializedName("video")
    var video: Boolean? = false,

    @Ignore
    @SerializedName("vote_average")
    var voteAverage: Double? = 0.0,

    @Ignore
    @SerializedName("vote_count")
    var voteCount: Int? = 0,

    @Ignore
    @SerializedName("status_code")
    var statusCode: Int? = 0,

    @Ignore
    @SerializedName("status_message")
    var statusMessage: String?= ""

) {
    data class Genre(
        @SerializedName("id")
        var id: Int? = 0,
        @SerializedName("name")
        var name: String? = ""
    )

    data class ProductionCompany(
        @SerializedName("id")
        var id: Int? = 0,
        @SerializedName("logo_path")
        var logoPath: String? = "",
        @SerializedName("name")
        var name: String? = "",
        @SerializedName("origin_country")
        var originCountry: String? = ""
    )

    data class ProductionCountry(
        @SerializedName("iso_3166_1")
        var iso31661: String? = "",
        @SerializedName("name")
        var name: String? = ""
    )

    data class SpokenLanguage(
        @SerializedName("iso_639_1")
        var iso6391: String? = "",
        @SerializedName("name")
        var name: String? = ""
    )
}