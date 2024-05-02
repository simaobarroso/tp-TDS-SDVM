package com.example.braguide.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "relTrail",
    foreignKeys = [ForeignKey(
        entity = Trail::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("rel_trail"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["id"], unique = true)]
)
@TypeConverters(
    RelTrailConverter::class
)
data class RelTrail (
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int,

    @ColumnInfo(name = "value")
    @SerializedName("value")
    var value: String,

    @ColumnInfo(name = "attrib")
    @SerializedName("attrib")
    var attrib: String,

    @ColumnInfo(name = "trail")
    @SerializedName("trail")
    var trail: String
) : Serializable