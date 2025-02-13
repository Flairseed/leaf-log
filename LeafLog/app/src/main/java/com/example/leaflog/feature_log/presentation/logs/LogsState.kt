package com.example.leaflog.feature_log.presentation.logs

import com.example.leaflog.feature_log.data.model.Log

data class LogsState(
    val logs: List<Log> = listOf(),
    val isLoading: Boolean = false
)