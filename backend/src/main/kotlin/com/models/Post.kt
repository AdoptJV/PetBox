package com.jvdev.com.models

import kotlinx.serialization.Serializable

@Serializable
data class Post (
    val postID : Int,
    val ownerID : Int,
    val imgUrl : String,
    val caption : String,
    val timestamp : Long
)