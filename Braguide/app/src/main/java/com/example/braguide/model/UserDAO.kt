package com.example.braguide.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @get:Query("SELECT DISTINCT * FROM user")
    val allUsers: Flow<List<User>>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getUserByUsername(username: String?): Flow<User>

    @Query("DELETE FROM user")
    suspend fun deleteAll()

}