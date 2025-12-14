package com.peerly.peerly_server.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.peerly.peerly_server.models.dto.GoogleMeetSpaceResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;

@Service
public class GoogleMeetService {

    private static final String SERVICE_ACCOUNT_PATH = "/google/peerly-meet-integration.json";
    private static final String MEET_SCOPE = "https://www.googleapis.com/auth/meetings.space.created";
    private static final String MEET_SPACES_URL = "https://meet.googleapis.com/v2/spaces";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String getAccessToken() throws IOException {
        InputStream in = getClass().getResourceAsStream(SERVICE_ACCOUNT_PATH);
        if (in == null) {
            throw new IllegalStateException("Service account JSON não encontrado em " + SERVICE_ACCOUNT_PATH);
        }

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(in)
                .createScoped(Collections.singletonList(MEET_SCOPE));

        credentials.refreshIfExpired();
        return credentials.getAccessToken().getTokenValue();
    }

    public String createMeetSpace(String title) throws Exception {
        String accessToken = getAccessToken();

        String bodyJson = """
                {
                  "config": {
                    "accessType": "OPEN"
                  }
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MEET_SPACES_URL))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException(
                    "Falha ao criar espaço no Google Meet. HTTP " + response.statusCode() + " - " + response.body()
            );
        }

        GoogleMeetSpaceResponse dto =
                objectMapper.readValue(response.body(), GoogleMeetSpaceResponse.class);

        if (dto.getMeetingUri() == null || dto.getMeetingUri().isBlank()) {
            throw new IllegalStateException("Resposta do Google Meet sem meetingUri: " + response.body());
        }

        return dto.getMeetingUri();
    }
}
