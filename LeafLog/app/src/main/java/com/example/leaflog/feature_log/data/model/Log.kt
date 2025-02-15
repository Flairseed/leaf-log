package com.example.leaflog.feature_log.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.leaflog.feature_journal.data.model.Journal
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Journal::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("journal_id"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Log(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "journal_id") val journalId: Int,
    val title: String,
    val description: String,
    val height: Float,
    val water: Int,
    @ColumnInfo(name = "light_level") val lightLevel: Float?,
    @ColumnInfo(name = "relative_humidity") val relativeHumidity: Int?,
    val temperature: Int?,
    val picture: String,
    val created: Date,
    val postId: Int? = null
)
