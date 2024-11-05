package com.dicoding.asclepius.database.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detection_history")
data class DetectionHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val imagePath: String,
    val date: String,
    val cancerInfo: String
)