package com.dicoding.asclepius.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.database.local.AppDatabase
import com.dicoding.asclepius.database.local.DetectionHistory
import com.dicoding.asclepius.repository.DetectionHistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DetectionHistoryRepository
    val allHistory: LiveData<List<DetectionHistory>>

    init {
        val dao = AppDatabase.getDatabase(application).detectionHistoryDao()
        repository = DetectionHistoryRepository(dao)
        allHistory = repository.allHistory
    }

    fun insert(history: DetectionHistory) = viewModelScope.launch {
        repository.insert(history)
    }

    fun delete(history: DetectionHistory) = viewModelScope.launch {
        repository.delete(history)
    }
}