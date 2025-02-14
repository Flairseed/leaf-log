package com.example.leaflog.feature_online_post.data.model

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GetPostModel(
    val id: Int,
    val userId: Int,
    val name: String,
    val title: String,
    val description: String,
    val height: Float,
    val water: Int,
    val lightLevel: Float?,
    val relativeHumidity: Int?,
    val temperature: Int?,
    val picture: String,
    val created: Date,
    val timeStamp: Date
)

fun jsonToPost(postJson: JSONObject): GetPostModel {
    val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    return GetPostModel(
        id = postJson["id"] as Int,
        userId = postJson["user_id"] as Int,
        name = postJson["name"] as String,
        title = postJson["title"] as String,
        description = postJson["description"] as String,
        height = (postJson["height"] as String).toFloat(),
        water = postJson["water"] as Int,
        lightLevel = (postJson["light_level"] as String).toFloat(),
        relativeHumidity = postJson["relative_humidity"] as Int,
        temperature = postJson["temperature"] as Int,
        picture = postJson["picture"] as String,
        created = dateTimeFormatter.parse(postJson["created"] as String) ?: Date(),
        timeStamp = dateTimeFormatter.parse(postJson["timestamp"] as String) ?: Date()
    )
}
