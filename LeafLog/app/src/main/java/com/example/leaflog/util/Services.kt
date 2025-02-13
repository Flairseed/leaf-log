package com.example.leaflog.util

import android.content.Context
import androidx.room.Room
import com.example.leaflog.core.data.local.LocalDataBase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

object Services {
    lateinit var localDb: LocalDataBase
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    fun initLocalDb(context: Context) {
        localDb = Room.databaseBuilder(
            context,
            LocalDataBase::class.java,
            "local-database",
        ).build()
    }

    fun initFusedLocationProvideClient(context: Context) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }
}