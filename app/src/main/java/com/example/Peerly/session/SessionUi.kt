package com.example.Peerly.sessions

import java.time.LocalDateTime
import java.util.UUID
import java.time.Duration

data class SessionUi(
    val id: String = UUID.randomUUID().toString(),
    val tutorId: String? = null,
    val tutorName: String,
    val subject: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val avatarUrl: String?
) {
    val durationMinutes: Long get() = Duration.between(start, end).toMinutes()
}
