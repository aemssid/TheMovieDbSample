package com.example.savvologytask.data.local

import androidx.room.TypeConverter
import com.example.savvologytask.model.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object MovieTypeConverter {

    @TypeConverter
    @JvmStatic
    fun fromList(value: List<Movie>?) = if(value != null) Gson().toJson(value) else null

    @TypeConverter
    @JvmStatic
    fun toList(value: String?) : List<Movie>? {
        return if(value == null){
            null
        }else {
            val listType: Type = object : TypeToken<List<Movie>>() {}.type
            Gson().fromJson(value, listType)
        }
    }
}