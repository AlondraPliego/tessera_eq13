package com.TESSERA.Eq13Tessera.reservas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservaRequest {

    @NotNull(message = "Debes indicar qué boleto quieres reservar")
    private Long boletoEventoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debes reservar al menos 1 boleto")
    private Integer cantidad;
}
