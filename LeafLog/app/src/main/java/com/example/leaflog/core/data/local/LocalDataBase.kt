package com.example.leaflog.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.leaflog.feature_journal.data.local.JournalService
import com.example.leaflog.feature_journal.data.model.Journal
import com.example.leaflog.util.Converters

@Database(entities = [Journal::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocalDataBase: RoomDatabase() {
    abstract fun journalService(): JournalService
}