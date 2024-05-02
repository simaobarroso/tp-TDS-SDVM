package com.example.braguide.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(
    tableName = "relPin",
    foreignKeys = [ForeignKey(
        entity = EdgeTip::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("rel_pin"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["id"], unique = true)]
)
data class RelPin (
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id : Int,

    @ColumnInfo(name = "value")
    @SerializedName("value")
    var value: String,

    @ColumnInfo(name = "attrib")
    @SerializedName("attrib")
    var attrib: String,

    @ColumnInfo(name = "pin")
    @SerializedName("pin")
    var pin : Int
) : Serializable