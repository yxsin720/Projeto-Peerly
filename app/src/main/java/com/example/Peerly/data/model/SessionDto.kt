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
    val tutor: TutorSlimDto?,
    val studentId: String? = null,
    val studentName: String? = null,
    val studentAvatarUrl: String? = null
)

private fun extractTutorNameFromTitle(title: String): String? {
    val regex = Regex("Sessão com (.+?)(?:\\s*—|$)", RegexOption.IGNORE_CASE)
    val match = regex.find(title) ?: return null
    return match.groupValues.getOrNull(1)?.trim()?.takeIf { it.isNotEmpty() }
}

private fun extractSubjectFromTitle(title: String): String {
    val parts = title.split("—")
    if (parts.size >= 2) {
        val subject = parts.last().trim()
        if (subject.isNotEmpty()) return subject
    }
    return title
}

fun SessionDto.toUi(): SessionUi {
    val startTime = startsAt?.let { LocalDateTime.parse(it) } ?: LocalDateTime.now()
    val endTime = endsAt?.let { LocalDateTime.parse(it) } ?: startTime.plusMinutes(60)
    val createdAtTime = createdAt?.let { LocalDateTime.parse(it) } ?: startTime

    val uiTutorName = when {
        !tutor?.name.isNullOrBlank() -> tutor!!.name!!
        !title.isNullOrBlank() -> extractTutorNameFromTitle(title!!) ?: "Tutor(a)"
        else -> "Tutor(a)"
    }

    val uiSubject = if (!title.isNullOrBlank()) {
        extractSubjectFromTitle(title!!)
    } else {
        "Sessão"
    }

    val uiAvatar = tutor?.avatarUrl
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
        avatarUrl = uiAvatar,
        studentId = studentId,
        studentName = studentName,
        studentAvatarUrl = studentAvatarUrl
    )
}
