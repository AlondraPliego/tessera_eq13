package com.TESSERA.Eq13Tessera.notificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificacionPruebaRequest {

    @NotBlank(message = "El destino (correo o teléfono) es obligatorio")
    private String destino;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
}
