package com.TESSERA.Eq13Tessera.eventos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Lo que el frontend necesita para inicializar el Renderer de seatmap.pro
// en la página de venta de una función/fecha específica.
@Data
@AllArgsConstructor
public class FechaSeatmapResponse {
    private String seatmapEventId; // "eventId" que pide el SDK @seatmap.pro/renderer
    private String publicKey;      // "publicKey" que pide el SDK (no es secreta)
}
