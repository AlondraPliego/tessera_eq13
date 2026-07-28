package com.TESSERA.Eq13Tessera.eventos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RecintoRequest {

    @NotBlank(message = "El nombre del recinto es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    // Opcional: el SVG del mapa se puede agregar/editar después
    private String mapaSvg;

    // Opcional: el ID del schema que diseñaste en el editor de seatmap.pro.
    // Si lo dejas vacío, el recinto funciona igual, solo no tendrá mapa interactivo.
    private Long seatmapSchemaId;
}
