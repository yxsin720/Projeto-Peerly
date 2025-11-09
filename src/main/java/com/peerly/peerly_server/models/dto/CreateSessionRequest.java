package com.peerly.peerly_server.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record CreateSessionRequest(
    String tutorId,
    String subjectId,
    String title,
    String description,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startsAt,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endsAt,
    Integer maxParticipants,
    Integer priceTotalCents,
    String studentId
) {}
