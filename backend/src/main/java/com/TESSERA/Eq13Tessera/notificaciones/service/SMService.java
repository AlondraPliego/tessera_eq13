package com.TESSERA.Eq13Tessera.notificaciones.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMService {

    private final TwilioClient twilioClient;

    @Value("${twilio.sms-from}")
    private String numeroOrigen;

    public SMService(TwilioClient twilioClient) {
        this.twilioClient = twilioClient;
    }

    // telefono debe incluir el código de país, ej: "+525512345678"
    public void enviarSms(String telefono, String mensaje) {
        twilioClient.enviarMensaje(numeroOrigen, telefono, mensaje, "SMS");
    }
}
