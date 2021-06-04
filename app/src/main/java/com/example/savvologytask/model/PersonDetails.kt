package com.example.savvologytask.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "person_details_bookmark")
data class PersonDetails(
    @ColumnInfo(name = "adult")
    @SerializedName("adult")
    var adult: Boolean? = false,

    @Ignore
    var also_known_as: List<Any?>? = null,

    @ColumnInfo(name = "biography")
    var biography: String? = null,

    @ColumnInfo(name = "birthday")
    var birthday: String? = null,

    @ColumnInfo(name = "deathday")
    var deathday: String? = null,

    @ColumnInfo(name = "gender")
    var gender: Int? = null,

    @ColumnInfo(name = "homepage")
    var homepage: String? = null,

    @PrimaryKey
    var id: Long? = null,

    @ColumnInfo(name = "imdb_id")
    var imdb_id: String? = null,

    @ColumnInfo(name = "known_for_department")
    var known_for_department: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "place_of_birth")
    var place_of_birth: String? = null,

    @ColumnInfo(name = "popularity")
    var popularity: Double? = null,

    @ColumnInfo(name = "profile_path")
    var profile_path: String? = null
)