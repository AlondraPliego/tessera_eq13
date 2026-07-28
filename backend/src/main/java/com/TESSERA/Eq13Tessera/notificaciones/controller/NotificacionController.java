package com.TESSERA.Eq13Tessera.notificaciones.controller;

import com.TESSERA.Eq13Tessera.notificaciones.dto.NotificacionPruebaRequest;
import com.TESSERA.Eq13Tessera.notificaciones.service.MailService;
import com.TESSERA.Eq13Tessera.notificaciones.service.SMService;
import com.TESSERA.Eq13Tessera.notificaciones.service.WhatsAppService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

// Endpoints solo para PROBAR que el correo/SMS/WhatsApp sí funcionan.
// Están restringidos a ADMIN en SecurityConfig.
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final MailService mailService;
    private final SMService smService;
    private final WhatsAppService whatsAppService;

    public NotificacionController(MailService mailService, SMService smService, WhatsAppService whatsAppService) {
        this.mailService = mailService;
        this.smService = smService;
        this.whatsAppService = whatsAppService;
    }

    @PostMapping("/prueba-correo")
    public ResponseEntity<String> probarCorreo(@Valid @RequestBody NotificacionPruebaRequest dto) {
        mailService.enviarCorreoConfirmacionCompra(dto.getDestino(), 0L, BigDecimal.ZERO);
        return ResponseEntity.ok("Solicitud de correo enviada (revisa la consola/tu bandeja)");
    }

    @PostMapping("/prueba-sms")
    public ResponseEntity<String> probarSms(@Valid @RequestBody NotificacionPruebaRequest dto) {
        smService.enviarSms(dto.getDestino(), dto.getMensaje());
        return ResponseEntity.ok("Solicitud de SMS enviada (revisa la consola/tu celular)");
    }

    @PostMapping("/prueba-whatsapp")
    public ResponseEntity<String> probarWhatsapp(@Valid @RequestBody NotificacionPruebaRequest dto) {
        whatsAppService.enviarWhatsapp(dto.getDestino(), dto.getMensaje());
        return ResponseEntity.ok("Solicitud de WhatsApp enviada (revisa la consola/tu celular)");
    }
}
