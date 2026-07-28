package com.TESSERA.Eq13Tessera.notificaciones.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    private final TwilioClient twilioClient;

    @Value("${twilio.whatsapp-from}")
    private String numeroOrigen;

    public WhatsAppService(TwilioClient twilioClient) {
        this.twilioClient = twilioClient;
    }

    // telefono debe incluir el código de país, ej: "+525512345678"
    // (nosotros le agregamos el prefijo "whatsapp:" que pide Twilio)
    public void enviarWhatsapp(String telefono, String mensaje) {
        twilioClient.enviarMensaje(numeroOrigen, "whatsapp:" + telefono, mensaje, "WhatsApp");
    }
}
