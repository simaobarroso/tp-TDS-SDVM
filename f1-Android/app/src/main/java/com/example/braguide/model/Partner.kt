package com.example.braguide.model

import android.health.connect.datatypes.AppInfo
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity
import androidx.room.Index
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "partner",
    foreignKeys = [ForeignKey(
        entity = AppData::class,
        parentColumns = arrayOf("app_name"),
        childColumns = arrayOf("partner_app"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["partner_name"], unique = true)]
)
data class Partner (
    @PrimaryKey
    @ColumnInfo(name = "partner_name")
    @SerializedName("partner_name")
    var partnerName: String,

    @ColumnInfo(name = "partner_phone")
    @SerializedName("partner_phone")
    var partnerPhone: String,

    @ColumnInfo(name = "partner_url")
    @SerializedName("partner_url")
    var partnerUrl: String,

    @ColumnInfo(name = "partner_mail")
    @SerializedName("partner_mail")
    var partnerMail: String,

    @ColumnInfo(name = "partner_desc")
    @SerializedName("partner_desc")
    var partnerDesc: String,

    // Constructor, getters and setters
    @ColumnInfo(name = "partner_app")
    @SerializedName("partner_app")
    var partnerApp: String
)