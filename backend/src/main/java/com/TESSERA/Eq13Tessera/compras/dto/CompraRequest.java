package com.TESSERA.Eq13Tessera.compras.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CompraRequest {

    // Lista de boletos que el cliente quiere comprar (puede ser de varias zonas)
    @NotEmpty(message = "Debes agregar al menos un boleto a la compra")
    @Valid
    private List<DetalleCompraDTO> detalles;
}
