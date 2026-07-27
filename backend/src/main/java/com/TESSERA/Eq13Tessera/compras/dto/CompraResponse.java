package com.TESSERA.Eq13Tessera.compras.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CompraResponse {
    private Long id;
    private Long clienteId;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado;
    private List<DetalleCompraDTO> detalles;
}
