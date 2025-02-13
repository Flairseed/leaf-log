package com.example.leaflog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.leaflog.ui.theme.LeafLogTheme
import com.example.leaflog.util.Navigation
import com.example.leaflog.util.Services

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Services.initLocalDb(this)
        Services.initFusedLocationProvideClient(this)

        setContent {
            LeafLogTheme {
                Navigation()
            }
        }
    }
}