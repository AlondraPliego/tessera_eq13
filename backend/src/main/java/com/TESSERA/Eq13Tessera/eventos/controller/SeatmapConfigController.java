package com.TESSERA.Eq13Tessera.eventos.controller;

import com.TESSERA.Eq13Tessera.eventos.dto.SeatmapConfigResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Le da al frontend la "public key" de seatmap.pro (no es secreta, solo la
// necesita el SDK del navegador para poder inicializar el mapa).
@RestController
public class SeatmapConfigController {

    @Value("${seatmap.public-key}")
    private String publicKey;

    @GetMapping("/api/seatmap/config")
    public ResponseEntity<SeatmapConfigResponse> obtenerConfig() {
        return ResponseEntity.ok(new SeatmapConfigResponse(publicKey));
    }
}
