package com.example.braguide.model


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface TrailDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cats: List<Trail>)

    @get:Query("SELECT DISTINCT * FROM trail")
    val trails: Flow<List<Trail>>

    @Query("SELECT * FROM trail WHERE id = :id")
    fun getTrailById(id: Int): Flow<Trail>

    @Query("SELECT * FROM trail WHERE id IN (:ids)")
    fun getTrailsById(ids: List<Int>): Flow<List<Trail>>

    @Query("DELETE FROM trail")
    fun deleteAll()
}