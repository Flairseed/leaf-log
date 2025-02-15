package com.example.leaflog.feature_journal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Journal(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val picture: String,
    val created: Date,
    @ColumnInfo(name = "associated_user_id") val associatedUserId: Int? = null
)
