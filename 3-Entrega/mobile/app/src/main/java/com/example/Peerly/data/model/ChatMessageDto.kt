package com.example.Peerly.data.model

data class ChatMessageDto(
    val id: String,
    val sessionId: String,
    val senderId: String?,
    val type: String,
    val content: String,
    val createdAt: String
)
