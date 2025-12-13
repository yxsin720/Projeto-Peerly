package com.peerly.peerly_server.controllers;

import com.peerly.peerly_server.models.Session;
import com.peerly.peerly_server.models.dto.MeetLinkResponse;
import com.peerly.peerly_server.services.GoogleMeetService;
import com.peerly.peerly_server.services.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/meet")
@CrossOrigin
public class MeetController {

    private final GoogleMeetService meetService;
    private final SessionService sessionService;

    public MeetController(GoogleMeetService meetService,
                          SessionService sessionService) {
        this.meetService = meetService;
        this.sessionService = sessionService;
    }

    @PostMapping("/sessions/{sessionId}")
    public ResponseEntity<MeetLinkResponse> createMeetForSession(@PathVariable String sessionId) {
        Session s = sessionService.findById(sessionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sessão não encontrada"));

        try {
            String title = s.getTitle() != null && !s.getTitle().isBlank()
                    ? s.getTitle()
                    : "Sessão Peerly";

            String joinUrl = meetService.createMeetSpace(title);

            MeetLinkResponse dto = new MeetLinkResponse();
            dto.setSessionId(sessionId);
            dto.setMeetUrl(joinUrl);

            return ResponseEntity.ok(dto);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro ao criar sala no Google Meet: " + ex.getMessage(),
                    ex
            );
        }
    }
}
