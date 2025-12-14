package com.example.Peerly.sessions

import com.example.Peerly.data.ApiService
import com.example.Peerly.data.RetrofitInstance
import com.example.Peerly.data.model.CreateSessionRequest
import com.example.Peerly.data.model.SessionDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SessionApiRepository(
    private val api: ApiService = RetrofitInstance.api
) {
    private val ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    suspend fun createSession(
        tutorId: String,
        title: String,
        start: LocalDateTime,
        end: LocalDateTime,
        subjectId: String? = null,
        description: String? = null,
        maxParticipants: Int? = 1,
        priceTotalCents: Int? = null,
        studentId: String? = null
    ): SessionDto {
        val req = CreateSessionRequest(
            tutorId = tutorId,
            subjectId = subjectId,
            title = title,
            description = description,
            startsAt = start.format(ISO),
            endsAt = end.format(ISO),
            maxParticipants = maxParticipants,
            priceTotalCents = priceTotalCents,
            studentId = studentId
        )
        return api.createSession(req)
    }
}
