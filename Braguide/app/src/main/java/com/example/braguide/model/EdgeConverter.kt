package com.example.braguide.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.room.TypeConverter


class EdgeConverter {
    private val gson = Gson()
    @TypeConverter
    fun fromList(value: List<Edge>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<Edge> {
        val listType = object : TypeToken<List<Edge>>() {}.type
        return Gson().fromJson(value, listType)
    }
}