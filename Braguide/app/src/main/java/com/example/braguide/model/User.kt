package com.example.braguide.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "user", indices = [Index(value = ["username"],unique = true)])
data class User(
    @PrimaryKey
    @ColumnInfo(name = "username")
    val username: String,

    @SerializedName("user_type")
    @ColumnInfo(name = "user_type")
    val userType: String,

    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    val firstName: String?,

    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    val lastName: String?,

    @SerializedName("email")
    @ColumnInfo(name = "email")
    val email: String?)