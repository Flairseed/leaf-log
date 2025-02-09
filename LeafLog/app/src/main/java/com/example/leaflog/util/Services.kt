package com.example.leaflog.util

import android.content.Context
import androidx.room.Room
import com.example.leaflog.core.data.local.LocalDataBase

object Services {
    lateinit var localDb: LocalDataBase

    fun initLocalDb(context: Context) {
        localDb = Room.databaseBuilder(
            context,
            LocalDataBase::class.java,
            "local-database",
        ).build()
    }
}