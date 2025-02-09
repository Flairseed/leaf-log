package com.example.leaflog.feature_journal.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.leaflog.feature_journal.data.model.Journal

@Dao
interface JournalService {
    @Query("SELECT * FROM journal")
    suspend fun getAllJournals(): List<Journal>

    @Query("SELECT * FROM journal WHERE id=:id")
    suspend fun getJournalById(id: Int): List<Journal>

    @Insert
    suspend fun createJournal(journal: Journal)

    @Update
    suspend fun updateJournal(journal: Journal)

    @Delete
    suspend fun deleteJournal(journal: Journal)
}