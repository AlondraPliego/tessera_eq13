package com.TESSERA.Eq13Tessera.eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RecintoResponse {
    private Long id;
    private String nombre;
    private String direccion;
    private String mapaSvg;
    private Long empresaId;
    private LocalDateTime createdAt;
    private Long seatmapSchemaId;
}
