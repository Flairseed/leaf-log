package com.example.leaflog.feature_journal.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Journal(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val picture: String,
    val created: Date,
)
