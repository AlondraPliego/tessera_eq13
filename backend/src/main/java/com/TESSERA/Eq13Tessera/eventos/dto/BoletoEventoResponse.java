package com.TESSERA.Eq13Tessera.eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BoletoEventoResponse {
    private Long id;
    private Long zonaId;
    private BigDecimal precio;
    private Integer cantidadDisponible;
}
