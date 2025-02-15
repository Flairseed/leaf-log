package com.example.leaflog.feature_journal.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.leaflog.feature_authentication.data.remote.AuthService
import com.example.leaflog.feature_journal.data.model.Journal

@Dao
interface JournalService {
    @Query("SELECT * FROM journal WHERE associated_user_id=:userId")
    suspend fun getAllJournals(userId: Int? = AuthService.userId): List<Journal>

    @Query("SELECT * FROM journal WHERE associated_user_id IS NULL")
    suspend fun getAllJournalsWithoutAssociatedUser(): List<Journal>

    @Query("SELECT * FROM journal WHERE id=:id")
    suspend fun getJournalById(id: Int): List<Journal>

    @Query("UPDATE journal SET associated_user_id = :userId WHERE associated_user_id IS NULL")
    suspend fun updateJournalAssociatedUserId(userId: Int? = AuthService.userId)

    @Insert
    suspend fun createJournal(journal: Journal)

    @Update
    suspend fun updateJournal(journal: Journal)

    @Delete
    suspend fun deleteJournal(journal: Journal)
}