package com.TESSERA.Eq13Tessera.notificaciones.service;

import org.springframework.stereotype.Service;

@Service
public class MailService {

    public void enviarCorreoRecuperacion(String destinatario, String token) {
        System.out.println("Simulando envío de correo a " + destinatario + " con token: " + token);
    }
}