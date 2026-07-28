package com.TESSERA.Eq13Tessera.eventos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CambiarEstadoRequest {
    @NotBlank(message = "El estado es obligatorio")
    private String estado; // PROGRAMADO, AGOTADO, CANCELADO, FINALIZADO
}
