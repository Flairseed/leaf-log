package com.example.leaflog.feature_journal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// TODO: DO NOT COMMIT THIS FILE. Commit after moving to feature_log
@Entity
data class Log(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "journal_id") val journalId: Int,
    val title: String,
    val description: String,
    val height: Double,
    val water: Int,
    @ColumnInfo(name = "light_level") val lightLevel: Double,
    @ColumnInfo(name = "relative_humidity") val relativeHumidity: Int,
    val temperature: Int,
    val picture: String,
    val created: Date,
)
