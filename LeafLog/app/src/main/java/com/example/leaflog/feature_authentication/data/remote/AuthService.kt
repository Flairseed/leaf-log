package com.example.leaflog.feature_authentication.data.remote

import com.example.leaflog.core.data.remote.HttpHandler
import com.example.leaflog.util.Services
import org.json.JSONObject

object AuthService {
    var userName: String? = null
        private set
    var userId: Int? = null
        private set

    suspend fun login(name: String, password: String): String? {
        val payload = JSONObject()
        payload.put("name", name)
        payload.put("password", password)
        val response = HttpHandler.setData("${HttpHandler.BACKEND_URL}/users/login", payload)
        return when (response) {
            null -> {
                "Internal server error"
            }
            HttpHandler.FORBIDDEN -> {
                "Name or password is incorrect"
            }
            else -> {
                userId = (JSONObject(response)["body"] as JSONObject)["id"] as Int
                userName = name
                Services.localDb.journalService().updateJournalAssociatedUserId()
                null
            }
        }
    }

    suspend fun register(name: String, password: String): String? {
        val payload = JSONObject()
        payload.put("name", name)
        payload.put("password", password)
        val response = HttpHandler.setData("${HttpHandler.BACKEND_URL}/users/register", payload)
        return when (response) {
            null -> {
                "Internal server error"
            }
            HttpHandler.FORBIDDEN -> {
                "User with that name is already registered"
            }
            else -> {
                userId = (JSONObject(response)["body"] as JSONObject)["id"] as Int
                userName = name
                Services.localDb.journalService().updateJournalAssociatedUserId()
                null
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return userName != null && userId != null
    }

    fun logOut() {
        userName = null
        userId = null
    }
}