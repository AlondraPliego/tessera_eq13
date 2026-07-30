package com.TESSERA.Eq13Tessera.notificaciones.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private static final String SENDGRID_URL = "https://api.sendgrid.com/v3/mail/send";

    private final RestTemplate restTemplate;

    @Value("${mail.remitente}")
    private String remitente;


    @Value("${sendgrid.api.key}")
    private String sendgridApiKey;

    public MailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public void enviarCorreoRecuperacion(String destinatario, String token) {
        String asunto = "Recupera tu contraseña - Tessera";
        String cuerpo = "Recibimos una solicitud para restablecer tu contraseña.\n\n"
                + "Usa este código para continuar: " + token + "\n\n"
                + "Si tú no pediste esto, puedes ignorar este correo.";
        enviar(destinatario, asunto, cuerpo);
    }

    @Async
    public void enviarCorreoBienvenida(String destinatario, String nombre) {
        String asunto = "¡Bienvenido a Tessera!";
        String cuerpo = "Hola " + nombre + ",\n\n"
                + "Tu cuenta en Tessera fue creada exitosamente. "
                + "Ya puedes buscar eventos y comprar tus boletos.\n\n"
                + "Equipo Tessera";
        enviar(destinatario, asunto, cuerpo);
    }

    @Async
    public void enviarCorreoConfirmacionCompra(String destinatario, Long compraId, BigDecimal total) {
        String asunto = "Confirmación de compra #" + compraId + " - Tessera";
        String cuerpo = "¡Gracias por tu compra!\n\n"
                + "Número de compra: " + compraId + "\n"
                + "Total pagado: $" + total + "\n\n"
                + "Puedes ver el detalle de tus boletos en tu cuenta de Tessera.";
        enviar(destinatario, asunto, cuerpo);
    }

    // Arma el JSON que pide la API de SendGrid y lo manda por HTTPS
    private void enviar(String destinatario, String asunto, String cuerpo) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(sendgridApiKey);

            Map<String, Object> to = new HashMap<>();
            to.put("email", destinatario);

            Map<String, Object> personalization = new HashMap<>();
            personalization.put("to", List.of(to));

            Map<String, Object> from = new HashMap<>();
            from.put("email", remitente);

            Map<String, Object> content = new HashMap<>();
            content.put("type", "text/plain");
            content.put("value", cuerpo);

            Map<String, Object> body = new HashMap<>();
            body.put("personalizations", List.of(personalization));
            body.put("from", from);
            body.put("subject", asunto);
            body.put("content", List.of(content));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForEntity(SENDGRID_URL, request, String.class);
            log.info("Correo enviado a {}", destinatario);
        } catch (Exception e) {
            // No tumbamos la petición del usuario solo porque el correo falló
            log.error("No se pudo enviar el correo a {}: {}", destinatario, e.getMessage());
        }
    }
}