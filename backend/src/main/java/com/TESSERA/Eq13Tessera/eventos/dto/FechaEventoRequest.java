package com.TESSERA.Eq13Tessera.eventos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class FechaEventoRequest {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotNull(message = "Debes indicar el recinto de esta función")
    private Long recintoId;
}
