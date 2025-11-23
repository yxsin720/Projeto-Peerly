package com.example.Peerly.data.model

import com.example.Peerly.sessions.SessionUi
import java.time.LocalDateTime

data class TutorSlimDto(
    val id: String?,
    val name: String?,
    val avatarUrl: String?
)

data class SessionDto(
    val id: String?,
    val tutorId: String?,
    val subjectId: String?,
    val title: String?,
    val description: String?,
    val startsAt: String?,
    val endsAt: String?,
    val status: String?,
    val maxParticipants: Int?,
    val priceTotalCents: Int?,
    val createdAt: String?,
    val tutor: TutorSlimDto?
)

/**
 * Ex.: "Sessão com Pedro Almeida — MATEMÁTICA"
 * devolve "MATEMÁTICA"
 */
private fun extractSubjectFromTitle(title: String): String? {
    val dashIndex = title.lastIndexOf('—')
    if (dashIndex != -1 && dashIndex + 1 < title.length) {
        return title.substring(dashIndex + 1)
            .trim()
            .takeIf { it.isNotEmpty() }
    }
    return null
}

fun SessionDto.toUi(): SessionUi {
    val startTime = startsAt?.let { LocalDateTime.parse(it) } ?: LocalDateTime.now()
    val endTime = endsAt?.let { LocalDateTime.parse(it) } ?: startTime.plusMinutes(60)
    val createdAtTime = createdAt?.let { LocalDateTime.parse(it) } ?: startTime

    val uiTutorName = tutor?.name?.takeIf { !it.isNullOrBlank() } ?: "Tutor(a)"
    val uiAvatar = tutor?.avatarUrl

    val uiSubject = when {
        !title.isNullOrBlank() -> extractSubjectFromTitle(title) ?: "Sessão"
        else -> "Sessão"
    }

    val uiStatus = status ?: "scheduled"

    return SessionUi(
        id = id ?: "",
        tutorId = tutorId,
        tutorName = uiTutorName,
        subject = uiSubject,
        start = startTime,
        end = endTime,
        status = uiStatus,
        description = description,
        priceTotalCents = priceTotalCents,
        createdAt = createdAtTime,
        avatarUrl = uiAvatar
    )
}
