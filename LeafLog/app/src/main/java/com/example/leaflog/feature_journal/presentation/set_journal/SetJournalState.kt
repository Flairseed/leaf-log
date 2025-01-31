package com.example.leaflog.feature_journal.presentation.set_journal

data class SetJournalState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val picture: String? = null,
    val pictureError: String? = null,
    val isLoading: Boolean = false,
)
