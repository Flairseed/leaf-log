package com.example.leaflog.core.data.remote

import android.content.Context
import android.net.Uri
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object HttpHandler {
    const val WEATHER_URL = "https://api.openweathermap.org/data/2.5"
    const val BACKEND_URL = "https://s5m5el1nih.execute-api.us-east-1.amazonaws.com"

    const val GET: String = "GET"
    const val POST: String = "POST"
    const val PUT: String = "PUT"
    const val DELETE: String = "DELETE"

    const val FORBIDDEN: String = "Forbidden"

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

    fun setData(url: String, payload: JSONObject, update: Boolean = false): String? {
        val urlObject = URL(url)
        val con = urlObject.openConnection() as HttpURLConnection
        con.requestMethod = if (update) PUT else POST
        con.setRequestProperty("Content-Type", "application/json")
        con.doInput = true
        con.doOutput = true

        val writer = BufferedWriter(OutputStreamWriter(con.outputStream))
        writer.write(payload.toString())
        writer.flush()
        writer.close()

        val responseCode = con.responseCode

        return when (responseCode) {
            HttpURLConnection.HTTP_OK -> {
                val reader = BufferedReader(InputStreamReader(con.inputStream))
                var line: String?
                val response = StringBuffer()
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                con.disconnect()
                response.toString()
            }
            HttpURLConnection.HTTP_FORBIDDEN -> {
                FORBIDDEN
            }
            else -> {
                null
            }
        }
    }

    fun postPicture(url: String, fileUri: String, context: Context): Boolean {
        val uri = Uri.parse(fileUri)
        val file = File(context.cacheDir, "image")

        // Copy uri content to a newly created cached file
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        val urlObject = URL(url)
        val con = urlObject.openConnection() as HttpURLConnection
        con.requestMethod = PUT
        con.doOutput = true
        con.setRequestProperty("Content-Type", "image/png")

        val outputStream = DataOutputStream(con.outputStream)
        val fileInputStream = FileInputStream(file)
        fileInputStream.copyTo(outputStream, 1024 * 1024)

        outputStream.flush()
        outputStream.close()
        fileInputStream.close()

        val responseCode = con.responseCode

        con.disconnect()

        return responseCode == HttpURLConnection.HTTP_OK
    }
    fun deleteData(url: String): String? {
        val urlObject = URL(url)
        val con = urlObject.openConnection() as HttpURLConnection
        con.requestMethod = DELETE

        val responseCode = con.responseCode

        return when (responseCode) {
            HttpURLConnection.HTTP_OK -> {
                val reader = BufferedReader(InputStreamReader(con.inputStream))
                var line: String?
                val response = StringBuffer()
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                con.disconnect()
                response.toString()
            }
            HttpURLConnection.HTTP_FORBIDDEN -> {
                FORBIDDEN
            }
            else -> {
                null
            }
        }
    }

}