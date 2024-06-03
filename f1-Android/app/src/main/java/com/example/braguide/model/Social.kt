package com.example.braguide.model

import android.health.connect.datatypes.AppInfo
import com.google.gson.annotations.SerializedName
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

@Entity(
    tableName = "social",
    foreignKeys = [ForeignKey(
        entity = AppData::class,
        parentColumns = arrayOf("app_name"),
        childColumns = arrayOf("social_app"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["social_name"], unique = true)]
)
data class Social (
    @PrimaryKey
    @ColumnInfo(name = "social_name")
    @SerializedName("social_name")
    var socialName: String,

    @ColumnInfo(name = "social_url")
    @SerializedName("social_url")
    var socialUrl: String,

    @ColumnInfo(name = "social_share_link")
    @SerializedName("social_share_link")
    var socialShareLink: String,

    // Constructor, getters and setters
    @ColumnInfo(name = "social_app")
    @SerializedName("social_app")
    var socialApp: String
)