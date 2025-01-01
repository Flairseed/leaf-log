package com.example.leaflog.core.data.local

import androidx.room.Database
import com.example.leaflog.feature_journal.data.local.JournalService
import com.example.leaflog.feature_journal.data.model.Journal

@Database(entities = [Journal::class], version = 1)
abstract class LocalDataBase {
    abstract fun journalService(): JournalService
}