package com.dicoding.asclepius.repository

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.database.local.DetectionHistory
import com.dicoding.asclepius.database.local.DetectionHistoryDao

class DetectionHistoryRepository(private val dao: DetectionHistoryDao) {
    val allHistory: LiveData<List<DetectionHistory>> = dao.getAllHistory()

    suspend fun insert(history: DetectionHistory) {
        dao.insert(history)
    }

    suspend fun delete(history: DetectionHistory) {
        dao.delete(history)
    }
}