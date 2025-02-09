package com.example.leaflog.feature_journal.presentation.journals

import com.example.leaflog.feature_journal.data.model.Journal

data class JournalsState(
    val journals: List<Journal> = listOf(),
    val isLoading: Boolean = false,
)
