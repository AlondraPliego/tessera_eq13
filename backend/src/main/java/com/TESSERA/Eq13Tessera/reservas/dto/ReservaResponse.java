package com.TESSERA.Eq13Tessera.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservaResponse {
    private Long id;
    private Long boletoEventoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private LocalDateTime expiraEn;
}
