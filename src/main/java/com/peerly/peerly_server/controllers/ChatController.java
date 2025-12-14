package com.peerly.peerly_server.controllers;

import com.peerly.peerly_server.models.Message;
import com.peerly.peerly_server.models.dto.MessageDto;
import com.peerly.peerly_server.models.dto.SendMessageRequest;
import com.peerly.peerly_server.repositories.MessageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {

    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public List<MessageDto> getMessagesBySession(@PathVariable String sessionId) {
        return messageRepository
                .findBySessionIdOrderByCreatedAtAsc(sessionId)
                .stream()
                .map(MessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable String sessionId,
            @RequestBody SendMessageRequest body
    ) {
        try {
            Message m = new Message();
            m.setSessionId(sessionId);
            m.setSenderId(body.getSenderId());
            m.setType(body.getType());
            m.setContent(body.getContent());

            Message saved = messageRepository.save(m);
            return ResponseEntity.ok(MessageDto.fromEntity(saved));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
    