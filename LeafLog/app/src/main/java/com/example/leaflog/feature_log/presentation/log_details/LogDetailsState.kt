package com.example.leaflog.feature_log.presentation.log_details

import com.example.leaflog.feature_log.data.model.Log

data class LogDetailsState(
    val log: Log? = null,
    val isLoading: Boolean = false
)
