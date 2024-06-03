package com.example.braguide.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.room.TypeConverter
import java.lang.reflect.Type

class EdgeTipConverter {
    private val gson = Gson()
    @TypeConverter
    fun fromRelPinListString(value: String): List<RelPin> {
        val listType: Type = object : TypeToken<List<RelPin>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toRelPinListString(list: List<RelPin>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromMediumListString(value: String): List<Medium> {
        val listType: Type = object : TypeToken<List<Medium>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toMediumListString(list: List<Medium>): String {
        return gson.toJson(list)
    }
}