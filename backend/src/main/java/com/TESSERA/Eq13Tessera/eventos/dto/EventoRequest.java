package com.TESSERA.Eq13Tessera.eventos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class EventoRequest {

    @NotBlank(message = "El nombre del evento es obligatorio")
    private String nombre;

    private String descripcion;

    private String flyerPrincipal;
    private String flyerSecundario;
    private String flyerTerciario;

    // Un evento necesita al menos 1 función/fecha
    @NotEmpty(message = "Debes agregar al menos una fecha para el evento")
    @Valid
    private List<FechaEventoRequest> fechas;

    // Y al menos 1 tipo de boleto (precio por zona)
    @NotEmpty(message = "Debes agregar al menos un tipo de boleto")
    @Valid
    private List<BoletoEventoRequest> boletos;
}
