package com.example.leaflog.feature_online_post.presentation.posts

import com.example.leaflog.feature_online_post.data.model.GetPostModel

data class PostsState(
    val posts: List<GetPostModel> = listOf(),
    val isLoading: Boolean = false
)
