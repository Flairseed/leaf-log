package com.example.leaflog.feature_log.presentation.set_log

data class SetLogState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val descriptionError: String? = null,
    val height: String = "",
    val heightError: String? = null,
    val water: String = "",
    val waterError: String? = null,
    val picture: String? = null,
    val pictureError: String? = null,
    val lightLevel: Float? = null,
    val relativeHumidity: Int? = null,
    val temperature: Int? = null,
    val isLoading: Boolean = false,
)
