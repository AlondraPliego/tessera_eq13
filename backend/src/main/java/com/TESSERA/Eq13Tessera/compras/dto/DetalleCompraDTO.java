package com.TESSERA.Eq13Tessera.compras.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

// Se usa TANTO para pedir la compra COMO para responder qué se compró
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompraDTO {

    @NotNull(message = "Debes indicar qué boleto quieres comprar")
    private Long boletoEventoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debes comprar al menos 1 boleto")
    private Integer cantidad;


    private Long reservaId;

    private BigDecimal subtotal;
}
