package com.example.braguide.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import androidx.room.TypeConverter

class RelTrailConverter {
    @TypeConverter
    fun fromList(value: List<RelTrail>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<RelTrail> {
        val listType = object : TypeToken<List<RelTrail>>() {}.type
        return Gson().fromJson(value, listType)
    }
}