package com.example.braguide.model

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Objects


@Entity(
    tableName = "tip",
    foreignKeys = [ForeignKey(
        entity = Edge::class,
        parentColumns = arrayOf("edge_start"),
        childColumns = arrayOf("id"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Edge::class,
        parentColumns = arrayOf("edge_end"),
        childColumns = arrayOf("id"),
        onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["id"], unique = true)]
)
@TypeConverters(
    EdgeTipConverter::class
)
data class EdgeTip (
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    var id: Int,

    @SerializedName("rel_pin")
    var relPin: List<RelPin>,

    @SerializedName("media")
    var media: List<Medium>,

    @ColumnInfo(name = "pin_name")
    @SerializedName("pin_name")
    var pinName: String,

    @ColumnInfo(name = "pin_desc")
    @SerializedName("pin_desc")
    var pinDesc: String,

    @ColumnInfo(name = "pin_lat")
    @SerializedName("pin_lat")
    var pinLat: Double,

    @ColumnInfo(name = "pin_lng")
    @SerializedName("pin_lng")
    var pinLng: Double,

    @ColumnInfo(name = "pin_alt")
    @SerializedName("pin_alt")
    var pinAlt: Double
) : Serializable {

    fun getLocation(): Location {
        val location = Location("")
        location.latitude = 37.7749
        location.longitude = -122.4194
        location.setAltitude(0.0)
        return location
    }
    fun getLocationString(): String {
        return "$pinLat,$pinLng"
    }

    fun hasImage(): Boolean {
        return media.stream().map<Any>(Medium::mediaType).anyMatch { e: Any? ->
            Objects.equals(
                e,
                "I"
            )
        }
    }

    fun hasVideo(): Boolean {
        return media.stream().map<Any>(Medium::mediaType).anyMatch { e: Any? ->
            Objects.equals(
                e,
                "V"
            )
        }
    }

    fun hasAudio(): Boolean {
        return media.stream().map<Any>(Medium::mediaType).anyMatch { e: Any? ->
            Objects.equals(
                e,
                "R"
            )
        }
    }

    fun getMapsCoordinate(): LatLng {
        return LatLng(pinLat, pinLng)
    }


}