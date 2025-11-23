package com.example.Peerly.sessions

import java.time.Duration
import java.time.LocalDateTime

data class SessionUi(
    val id: String,
    val tutorId: String?,
    val tutorName: String,
    val subject: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val status: String,
    val description: String?,
    val priceTotalCents: Int?,
    val createdAt: LocalDateTime,
    val avatarUrl: String?
) {
    val durationMinutes: Long
        get() = Duration.between(start, end).toMinutes()
}
