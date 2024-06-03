package com.example.braguide.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import androidx.room.TypeConverter

class TypeConverter {
    private val gson = Gson()
    @TypeConverter
    fun contactListFromString(value: String?): List<Contact> {
        val listType: Type = object : TypeToken<List<Contact>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun contactListToString(list: List<Contact>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun partnerListFromString(value: String): List<Partner> {
        val listType: Type = object : TypeToken<List<Partner>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun partnerListToString(list: List<Partner>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun socialListFromString(value: String): List<Social> {
        val listType: Type = object : TypeToken<List<Social>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun socialListToString(list: List<Social>): String {
        return gson.toJson(list)
    }
}