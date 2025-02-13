package com.example.leaflog.feature_log.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.leaflog.feature_log.data.model.Log

@Dao
interface LogService {
    @Query("SELECT * FROM log WHERE journal_id=:id")
    suspend fun getAllLogsByJournalId(id: Int): List<Log>

    @Query("SELECT * FROM log WHERE id=:id")
    suspend fun getLogById(id: Int): List<Log>

    @Insert
    suspend fun createLog(log: Log)

    @Update
    suspend fun updateLog(log: Log)

    @Delete
    suspend fun deleteLog(log: Log)
}