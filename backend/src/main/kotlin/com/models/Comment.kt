package com.jvdev.com.models

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Int,
    val post: Int,
    val user: Int,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
