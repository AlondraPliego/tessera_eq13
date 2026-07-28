package com.TESSERA.Eq13Tessera.eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class FechaEventoResponse {
    private Long id;
    private LocalDate fecha;
    private LocalTime hora;
    private String ciudad;
    private Long recintoId;
    private String seatmapEventId;
}
