package com.example.braguide.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "medium",
    foreignKeys = [ForeignKey(
        entity = EdgeTip::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("media"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["id"], unique = true)]
)
data class Medium (
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int?,

    @ColumnInfo(name = "partner_phone")
    @SerializedName("partner_phone")
    var partnerPhone: String?,

    @ColumnInfo(name = "media_file")
    @SerializedName("media_file")
    var mediaFile: String?,

    @ColumnInfo(name = "media_type")
    @SerializedName("media_type")
    var mediaType: String?,

    @ColumnInfo(name = "media_pin")
    @SerializedName("media_pin")
    var mediaPin: Int?
) : Serializable