package com.TESSERA.Eq13Tessera.eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ZonaResponse {
    private Long id;
    private Long recintoId;
    private String nombre;
    private Integer capacidad;
    private String color;
    private String coordenadas;
    private Long seatmapObjectId;
}
