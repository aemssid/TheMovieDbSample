package com.example.savvologytask.data.local

import android.content.res.Resources
import com.example.savvologytask.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.util.ArrayList

class LanguageDetails(
    var iso_639_1: String,
    var english_name: String,
    var name: String
) {
    companion object {
        /**
         * Loads a raw JSON at R.raw.products and converts it into a list of ProductEntry objects
         */
        fun initLanguageList(resources: Resources): List<LanguageDetails> {
            val inputStream = resources.openRawResource(R.raw.languages)
            val jsonProductsString = inputStream.bufferedReader().use(BufferedReader::readText)
            val gson = Gson()
            val productListType = object : TypeToken<ArrayList<LanguageDetails>>() {}.type
            return gson.fromJson<List<LanguageDetails>>(jsonProductsString, productListType)
        }
    }
}