package com.example.leaflog.core.data.remote

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object HttpHandler {
    const val WEATHER_URL = "https://api.openweathermap.org/data/2.5"

    const val GET: String = "GET"
    const val POST: String = "POST"
    const val PUT: String = "PUT"
    const val DELETE: String = "DELETE"

    fun getData(url: String): String? {
        val urlObject = URL(url)
        val con = urlObject.openConnection() as HttpURLConnection
        con.requestMethod = GET

        val responseCode = con.responseCode

        return if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(con.inputStream))
            var line: String?
            val response = StringBuffer()
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            con.disconnect()
            response.toString()
        } else {
            return null
        }
    }
}