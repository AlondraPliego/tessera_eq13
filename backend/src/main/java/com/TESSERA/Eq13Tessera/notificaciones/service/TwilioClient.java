package com.TESSERA.Eq13Tessera.notificaciones.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Cliente chiquito para hablar con la API de Twilio (la usamos tanto para
 * SMS como para WhatsApp, porque Twilio maneja ambos con el mismo endpoint).
 * No usamos ninguna librería extra, solo el HttpClient que ya trae Java.
 */
@Component
public class TwilioClient {

    private static final Logger log = LoggerFactory.getLogger(TwilioClient.class);

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public boolean credencialesConfiguradas() {
        return StringUtils.hasText(accountSid) && StringUtils.hasText(authToken);
    }

    // from y to ya deben venir con el formato que pide Twilio
    // (para WhatsApp: "whatsapp:+521..."; para SMS: "+521...")
    public void enviarMensaje(String from, String to, String mensaje, String tipo) {
        if (!credencialesConfiguradas()) {
            // Todavía no configuraron su cuenta de Twilio: solo lo dejamos en consola
            // para que puedan seguir probando el resto de la app sin problema.
            log.info("[{} SIMULADO] Para: {} | Mensaje: {}", tipo, to, mensaje);
            return;
        }

        try {
            String url = "https://api.twilio.com/2010-04-01/Accounts/" + accountSid + "/Messages.json";

            String cuerpoForm = "To=" + URLEncoder.encode(to, StandardCharsets.UTF_8)
                    + "&From=" + URLEncoder.encode(from, StandardCharsets.UTF_8)
                    + "&Body=" + URLEncoder.encode(mensaje, StandardCharsets.UTF_8);

            String credenciales = Base64.getEncoder()
                    .encodeToString((accountSid + ":" + authToken).getBytes(StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Basic " + credenciales)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(cuerpoForm))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("{} enviado a {}", tipo, to);
            } else {
                log.error("Twilio respondió {} al mandar {} a {}: {}", response.statusCode(), tipo, to, response.body());
            }
        } catch (Exception e) {
            log.error("No se pudo enviar el {} a {}: {}", tipo, to, e.getMessage());
        }
    }
}
