package com.dicoding.asclepius.database.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DetectionHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: DetectionHistory)

    @Delete
    suspend fun delete(history: DetectionHistory)

    @Query("SELECT * FROM detection_history ORDER BY id DESC")
    fun getAllHistory(): LiveData<List<DetectionHistory>>
}