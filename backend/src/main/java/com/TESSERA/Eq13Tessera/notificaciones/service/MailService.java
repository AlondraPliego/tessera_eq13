package com.TESSERA.Eq13Tessera.notificaciones.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    @Value("${mail.remitente}")
    private String remitente;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoRecuperacion(String destinatario, String token) {
        String asunto = "Recupera tu contraseña - Tessera";
        String cuerpo = "Recibimos una solicitud para restablecer tu contraseña.\n\n"
                + "Usa este código para continuar: " + token + "\n\n"
                + "Si tú no pediste esto, puedes ignorar este correo.";
        enviar(destinatario, asunto, cuerpo);
    }

    public void enviarCorreoBienvenida(String destinatario, String nombre) {
        String asunto = "¡Bienvenido a Tessera!";
        String cuerpo = "Hola " + nombre + ",\n\n"
                + "Tu cuenta en Tessera fue creada exitosamente. "
                + "Ya puedes buscar eventos y comprar tus boletos.\n\n"
                + "Equipo Tessera";
        enviar(destinatario, asunto, cuerpo);
    }

    public void enviarCorreoConfirmacionCompra(String destinatario, Long compraId, BigDecimal total) {
        String asunto = "Confirmación de compra #" + compraId + " - Tessera";
        String cuerpo = "¡Gracias por tu compra!\n\n"
                + "Número de compra: " + compraId + "\n"
                + "Total pagado: $" + total + "\n\n"
                + "Puedes ver el detalle de tus boletos en tu cuenta de Tessera.";
        enviar(destinatario, asunto, cuerpo);
    }

    // Método genérico: arma el correo y lo manda con JavaMailSender (que usa Postfix)
    private void enviar(String destinatario, String asunto, String cuerpo) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);
            mailSender.send(mensaje);
            log.info("Correo enviado a {}", destinatario);
        } catch (Exception e) {
            // No tumbamos la petición del usuario solo porque el correo falló
            // (por ejemplo, si Postfix no está configurado en tu compu local).
            log.error("No se pudo enviar el correo a {}: {}", destinatario, e.getMessage());
        }
    }
}
