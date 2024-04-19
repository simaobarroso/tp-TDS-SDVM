package com.example.braguide.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "trail",indices = [Index(value = ["id"],unique = true)])
data class Trail(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id : String,

    @SerializedName("trail_img")
    @ColumnInfo(name = "trail_img")
    val imageUrl : String)