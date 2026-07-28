package com.TESSERA.Eq13Tessera.eventos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ZonaRequest {

    @NotBlank(message = "El nombre de la zona es obligatorio")
    private String nombre;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    private Integer capacidad;

    private String color;

    private String coordenadas;

    // Opcional: el ID de esta zona dentro del mapa de seatmap.pro (para que se pinte con su precio)
    private Long seatmapObjectId;
}
