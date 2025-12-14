package com.example.Peerly.data.model

data class CreateSessionRequest(
    val tutorId: String,
    val subjectId: String? = null,
    val title: String,
    val description: String? = null,
    val startsAt: String,
    val endsAt: String,
    val maxParticipants: Int? = 1,
    val priceTotalCents: Int? = null,
    val studentId: String? = null
)
