package com.example.Peerly.data.model

data class CreateSessionRequest(
    val tutorId: String,
    val subjectId: String? = null,
    val title: String,
    val description: String? = null,
    val startsAt: String,   // "yyyy-MM-dd'T'HH:mm:ss"
    val endsAt: String,     // idem
    val maxParticipants: Int? = 1,
    val priceTotalCents: Int? = null,
    val studentId: String? = null
)
