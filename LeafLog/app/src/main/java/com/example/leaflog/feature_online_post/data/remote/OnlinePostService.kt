package com.example.leaflog.feature_online_post.data.remote

import com.example.leaflog.core.data.remote.HttpHandler
import com.example.leaflog.feature_online_post.data.model.GetPostModel
import com.example.leaflog.feature_online_post.data.model.jsonToPost
import org.json.JSONArray
import org.json.JSONObject

object OnlinePostService {
    fun getPosts(): List<GetPostModel>? {
        val response = HttpHandler.getData("${HttpHandler.BACKEND_URL}/posts")
        return if (response == null) {
            null
        } else {
            val postsJson = JSONObject(response)["body"] as JSONArray
            val posts = mutableListOf<GetPostModel>()
            for (i in 0 until postsJson.length()) {
                posts.add(jsonToPost(postsJson.getJSONObject(i)))
            }
            posts
        }
    }
}