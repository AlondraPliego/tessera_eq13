package com.TESSERA.Eq13Tessera.reservas.controller;

import com.TESSERA.Eq13Tessera.auth.entity.Usuario;
import com.TESSERA.Eq13Tessera.reservas.dto.ReservaRequest;
import com.TESSERA.Eq13Tessera.reservas.dto.ReservaResponse;
import com.TESSERA.Eq13Tessera.reservas.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> crear(
            @Valid @RequestBody ReservaRequest dto, Authentication authentication) {
        ReservaResponse creada = reservaService.crear(dto, obtenerUsuarioId(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // el cliente quita el asiento del carrito, o cancela la reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> liberar(@PathVariable Long id, Authentication authentication) {
        reservaService.liberar(id, obtenerUsuarioId(authentication));
        return ResponseEntity.noContent().build();
    }

    private Long obtenerUsuarioId(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuario.getUsuarioId();
    }
}
