package com.peerly.peerly_server.models.dto;

import java.time.LocalDateTime;

public record CreateSessionRequest(
        String tutorId,
        String subjectId,
        String title,
        String description,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        Integer maxParticipants,
        Integer priceTotalCents
) {}
