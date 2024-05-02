package com.example.braguide.model


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface AppDataDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(app: AppData)

    @get:Query("SELECT DISTINCT * FROM app_data")
    val appData: Flow<AppData>

    @Query("SELECT * FROM contact WHERE contact_app = :appName")
    fun getContacts(appName: String): Flow<List<Contact>>

    @Query("DELETE FROM app_data")
    fun deleteAll()
}