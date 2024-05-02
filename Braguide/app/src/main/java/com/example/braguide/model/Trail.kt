package com.example.braguide.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.function.Supplier
import java.util.stream.Collectors
import java.util.stream.Stream


@Entity(tableName = "trail",indices = [Index(value = ["id"],unique = true)])
@TypeConverters(
    com.example.braguide.model.TypeConverter::class
)
data class Trail(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id : Int,

    @SerializedName("trail_img")
    @ColumnInfo(name = "trail_img")
    val imageUrl : String,

    @SerializedName("trail_name")
    @ColumnInfo(name = "trail_name")
    val trailName : String,

    @SerializedName("trail_desc")
    @ColumnInfo(name = "trail_desc")
    val trailDescription : String?,

    @SerializedName("trail_dur")
    @ColumnInfo(name = "trail_dur")
    val trailDuration : String?,

    @SerializedName("rel_trail")
    val relTrail : List<RelTrail>,

    @SerializedName("edges")
    val edges : List<Edge>
): Serializable {
    fun getRoute(): MutableList<EdgeTip> {
        val list = edges.flatMap { listOf(it.edgeStart, it.edgeEnd) }
            .distinctBy { it.getLocationString() }
        val res : MutableList<EdgeTip> = mutableListOf()
        res.addAll(list)
        return res
    }

}