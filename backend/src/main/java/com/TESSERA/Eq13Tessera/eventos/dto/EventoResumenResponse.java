package com.TESSERA.Eq13Tessera.eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Versión "ligera" del evento, para listas/paginación (sin cargar fechas ni boletos)
@Data
@AllArgsConstructor
public class EventoResumenResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Long empresaId;
    private String estado;
    private String flyerPrincipal;
}
