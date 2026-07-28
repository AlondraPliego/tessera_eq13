package com.TESSERA.Eq13Tessera.eventos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BoletoEventoRequest {

    @NotNull(message = "Debes indicar la zona")
    private Long zonaId;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @NotNull(message = "La cantidad disponible es obligatoria")
    @Min(value = 1, message = "Debe haber al menos 1 boleto disponible")
    private Integer cantidadDisponible;
}
