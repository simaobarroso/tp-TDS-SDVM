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
    tableName = "edge",
    foreignKeys = [ForeignKey(
        entity = Trail::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("edge_trail"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["id"], unique = true),
        Index(value = ["edge_start"], unique = true),
        Index(value = ["edge_end"], unique = true)]
)
@TypeConverters(
    EdgeConverter::class
)
data class Edge (
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int,

    @ColumnInfo(name = "edge_start")
    @SerializedName("edge_start")
    var edgeStart: EdgeTip,

    @ColumnInfo(name = "edge_end")
    @SerializedName("edge_end")
    var edgeEnd: EdgeTip,

    @ColumnInfo(name = "edge_transport")
    @SerializedName("edge_transport")
    var edgeTransport: String,

    @ColumnInfo(name = "edge_duration")
    @SerializedName("edge_duration")
    var edgeDuration: Int,

    @ColumnInfo(name = "edge_desc")
    @SerializedName("edge_desc")
    var edgeDesc: String,

    @ColumnInfo(name = "edge_trail")
    @SerializedName("edge_trail")
    var edgeTrail: Int
) : Serializable