package com.example.leaflog.feature_charts.presentation.analytics

import com.example.leaflog.feature_journal.data.model.Journal
import com.example.leaflog.feature_log.data.model.Log

data class AnalyticsState(
    val journals: List<Journal> = listOf(),
    val logPages: List<List<Log>> = listOf(),
    val currentJournalIndex: Int = 0,
    val currentLogsPage: Int = 0,
    val isLoading: Boolean = false
)
