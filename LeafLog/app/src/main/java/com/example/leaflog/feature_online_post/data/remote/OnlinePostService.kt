package com.example.leaflog.feature_online_post.data.remote

import android.content.Context
import com.example.leaflog.core.data.remote.HttpHandler
import com.example.leaflog.feature_authentication.data.remote.AuthService
import com.example.leaflog.feature_online_post.data.model.GetPostModel
import com.example.leaflog.feature_online_post.data.model.SetPostModel
import com.example.leaflog.feature_online_post.data.model.jsonToPost
import com.example.leaflog.feature_online_post.data.model.toJson
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

    fun setPost(post: SetPostModel, postId: Int?, context: Context): String? {
        val presignedUrlResponse = HttpHandler.getData("${HttpHandler.BACKEND_URL}/get-presigned-url?name=${AuthService.userName}")
            ?: return null
        val url = (JSONObject(presignedUrlResponse)["body"] as JSONObject)["url"] as String
        val imagePath = (JSONObject(presignedUrlResponse)["body"] as JSONObject)["imagePath"] as String

        val imageUploaded = HttpHandler.postPicture(
            url = url,
            fileUri = post.picture,
            context = context
        )

        if (!imageUploaded) {
            return null
        }

        val newPayload = post.copy(
            picture = imagePath
        )
        val postUrl = if (postId == null) "${HttpHandler.BACKEND_URL}/posts" else "${HttpHandler.BACKEND_URL}/posts/$postId"
        val postResponse = HttpHandler.setData(
            url = postUrl,
            payload = newPayload.toJson(),
            update = postId != null
        )
        return postResponse
    }

    fun deletePost(userId: Int, postId: Int): String? {
        val response = HttpHandler.deleteData("${HttpHandler.BACKEND_URL}/posts/$postId?userId=$userId")
        return response
    }
}