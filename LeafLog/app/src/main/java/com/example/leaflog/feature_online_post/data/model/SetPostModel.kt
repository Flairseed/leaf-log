package com.example.leaflog.feature_online_post.data.model

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class SetPostModel(
    val userId: Int,
    val title: String,
    val description: String,
    val height: Float,
    val water: Int,
    val lightLevel: Float?,
    val relativeHumidity: Int?,
    val temperature: Int?,
    val picture: String,
    val created: Date
)

fun SetPostModel.toJson(): JSONObject {
    val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    val json = JSONObject()
    json.put("user_id", this.userId)
    json.put("title", this.title)
    json.put("description", this.description)
    json.put("height", this.height)
    json.put("water", this.water)
    json.put("light_level", this.lightLevel)
    json.put("relative_humidity", this.relativeHumidity)
    json.put("temperature", this.temperature)
    json.put("picture", this.picture)
    json.put("created", dateTimeFormatter.format(this.created))

    return json
}
