package com.jvdev.com

@kotlinx.serialization.Serializable
data class ApiResponse(
    val status: String,
    val echo: Map<String, String>
)