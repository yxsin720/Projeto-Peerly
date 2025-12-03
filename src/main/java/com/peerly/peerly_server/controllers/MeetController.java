package com.peerly.peerly_server.controllers;

import com.peerly.peerly_server.models.Session;
import com.peerly.peerly_server.models.dto.MeetLinkResponse;
import com.peerly.peerly_server.services.GoogleMeetService;
import com.peerly.peerly_server.services.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<MeetLinkResponse> createMeet(@PathVariable String sessionId) throws Exception {
        Session s = sessionService.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada."));

        String title = s.getTitle() != null ? s.getTitle() : "Peerly Meeting";

        String meetUrl = meetService.createMeetSpace(title);

        MeetLinkResponse dto = new MeetLinkResponse();
        dto.setSessionId(sessionId);
        dto.setMeetUrl(meetUrl);

        return ResponseEntity.ok(dto);
    }
}
