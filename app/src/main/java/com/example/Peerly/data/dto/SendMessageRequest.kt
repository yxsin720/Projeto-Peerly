package com.example.Peerly.data.model

data class SendMessageRequest(
    val senderId: String?,
    val content: String,
    val type: String = "text"
)
