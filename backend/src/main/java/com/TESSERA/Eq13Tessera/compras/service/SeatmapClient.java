package com.TESSERA.Eq13Tessera.compras.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


@Component
public class SeatmapClient {

    private static final Logger log = LoggerFactory.getLogger(SeatmapClient.class);

    @Value("${seatmap.base-url}")
    private String baseUrl;

    @Value("${seatmap.organization-token}")
    private String organizationToken;

    
    @Value("${seatmap.organization-id:}")
    private String organizationId;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public boolean credencialesConfiguradas() {
        return StringUtils.hasText(organizationToken);
    }

    // Manda una petición POST/PUT/DELETE con un cuerpo JSON y regresa el cuerpo de la respuesta (texto JSON)
    public Optional<String> enviar(String metodo, String path, String cuerpoJson) {
        if (!credencialesConfiguradas()) {
            log.info("[SEATMAP SIMULADO] {} {} -> {}", metodo, path, cuerpoJson);
            return Optional.empty();
        }
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .header("Content-Type", "application/json")
                    .header("X-API-Key", organizationToken);

            // X-Organization-ID solo se manda si están usando un tenant token
            if (StringUtils.hasText(organizationId)) {
                builder.header("X-Organization-ID", organizationId);
            }

            HttpRequest.BodyPublisher cuerpo = cuerpoJson == null
                    ? HttpRequest.BodyPublishers.noBody()
                    : HttpRequest.BodyPublishers.ofString(cuerpoJson, StandardCharsets.UTF_8);

            HttpRequest request = switch (metodo) {
                case "POST" -> builder.POST(cuerpo).build();
                case "PUT" -> builder.PUT(cuerpo).build();
                case "DELETE" -> builder.DELETE().build();
                default -> builder.GET().build();
            };

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return Optional.of(response.body());
            }
            log.error("seatmap.pro respondió {} en {} {}: {}", response.statusCode(), metodo, path, response.body());
            return Optional.empty();
        } catch (Exception e) {
            log.error("No se pudo hablar con seatmap.pro ({} {}): {}", metodo, path, e.getMessage());
            return Optional.empty();
        }
    }
}
