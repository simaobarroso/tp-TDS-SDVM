package com.example.braguide.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName


@Entity(tableName = "app_data", indices = [Index(value = ["app_name"], unique = true)])
@TypeConverters(
    TypeConverter::class
)
data class AppData(
    @SerializedName("app_name")
    @ColumnInfo(name = "app_name")
    @PrimaryKey
    var appName: String,

    @SerializedName("socials")
    var socials: List<Social>,

    @SerializedName("contacts")
    var contacts: List<Contact>,

    @SerializedName("partners")
    var partners: List<Partner>,

    @ColumnInfo(name = "app_desc")
    @SerializedName("app_desc")
    var appDesc: String,

    @ColumnInfo(name = "app_landing_page_text")
    @SerializedName("app_landing_page_text")
    var appLandingPageText: String
)