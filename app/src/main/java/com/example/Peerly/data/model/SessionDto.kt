// data/model/SessionDto.kt
package com.example.Peerly.data.model

data class SessionDto(
    val id: String? = null,
    val tutorId: String? = null,
    val subjectId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val startsAt: String? = null,   // já vem como ISO no backend
    val endsAt: String? = null,
    val createdAt: String? = null,
    val status: String? = null,
    val maxParticipants: Int? = null,
    val priceTotalCents: Int? = null,
    val tutor: TutorSlimDto? = null
)

data class TutorSlimDto(
    val id: String?,
    val name: String?,
    val avatarUrl: String?
)
