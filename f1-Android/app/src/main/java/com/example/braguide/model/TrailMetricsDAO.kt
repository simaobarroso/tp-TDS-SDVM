package com.example.braguide.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TrailMetricsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trailMetrics: TrailMetrics)

    @get:Query("SELECT DISTINCT * FROM trail_metrics")
    val allMetrics: LiveData<List<TrailMetrics>>

    @Query("SELECT * FROM trail_metrics WHERE metricId = :id")
    fun getMetricsById(id: Int): LiveData<TrailMetrics>

    @Query("SELECT * FROM trail_metrics WHERE username = :username")
    fun getMetricsByUsername(username: String): LiveData<List<TrailMetrics>>

    @Query("DELETE FROM trail_metrics")
    fun deleteAll()
}