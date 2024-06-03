package com.example.braguide.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(
    tableName = "trail_metrics",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("username"),
        childColumns = arrayOf("username"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["metricId"], unique = true)]
)
data class TrailMetrics (
    @PrimaryKey
    @ColumnInfo(name = "metricId")
    var metricId: Int?,

    @ColumnInfo(name = "username", index = true)
    var username: String,

    @ColumnInfo(name = "trail_id", index = true)
    var trailId: Int,

    @ColumnInfo(name = "completedPercentage")
    var completedPercentage: Float,

    @ColumnInfo(name = "timeTaken")
    var timeTaken: Float,

    @ColumnInfo(name = "visitedPins")
    var visitedPins: String,
) : Serializable {
    fun getPinIdList(): List<Int> {
        val pinIds: MutableList<Int> = mutableListOf()
        val pinArray: List<String> = this.visitedPins.split(";")
        for (pinString in pinArray) {
            if (pinString.isNotEmpty()) {
                pinIds.add(pinString.toInt())
            }
        }
        return pinIds
    }
}