package com.example.braguide.model

import android.health.connect.datatypes.AppInfo
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "contact",
    foreignKeys = [ForeignKey(
        entity = AppData::class,
        parentColumns = ["app_name"],
        childColumns = ["contact_app"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["contact_name"], unique = true)]
)
data class Contact (
    @PrimaryKey
    @SerializedName("contact_name")
    @ColumnInfo(name = "contact_name")
    var contactName: String,

    @SerializedName("contact_phone")
    @ColumnInfo(name = "contact_phone")
    var contactPhone: String,

    @SerializedName("contact_url")
    @ColumnInfo(name = "contact_url")
    var contactUrl: String,

    @SerializedName("contact_mail")
    @ColumnInfo(name = "contact_mail")
    var contactMail: String,

    @SerializedName("contact_desc")
    @ColumnInfo(name = "contact_desc")
    var contactDesc: String,

    // Constructor, getters and setters
    @SerializedName("contact_app")
    @ColumnInfo(name = "contact_app", index = true)
    var contactApp: String
)