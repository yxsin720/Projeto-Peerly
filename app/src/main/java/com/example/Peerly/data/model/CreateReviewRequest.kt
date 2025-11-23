package com.example.Peerly.data.model

data class CreateReviewRequest(
    val sessionId: String,
    val reviewerId: String,
    val tutorId: String,
    val rating: Int,
    val comment: String?
)
