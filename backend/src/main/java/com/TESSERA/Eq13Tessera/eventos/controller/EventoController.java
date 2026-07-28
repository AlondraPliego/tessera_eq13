package com.TESSERA.Eq13Tessera.eventos.controller;

import com.TESSERA.Eq13Tessera.auth.entity.Usuario;
import com.TESSERA.Eq13Tessera.eventos.dto.CambiarEstadoRequest;
import com.TESSERA.Eq13Tessera.eventos.dto.EventoRequest;
import com.TESSERA.Eq13Tessera.eventos.dto.EventoResponse;
import com.TESSERA.Eq13Tessera.eventos.dto.EventoResumenResponse;
import com.TESSERA.Eq13Tessera.eventos.dto.FechaSeatmapResponse;
import com.TESSERA.Eq13Tessera.eventos.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    // GET /api/eventos?nombre=rock&estado=PROGRAMADO&page=0&size=10 -> público
    @GetMapping
    public ResponseEntity<Page<EventoResumenResponse>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String estado,
            Pageable pageable) {
        return ResponseEntity.ok(eventoService.listar(nombre, estado, pageable));
    }

    // GET /api/eventos/mios -> solo la empresa dueña ve SUS eventos
    @GetMapping("/mios")
    public ResponseEntity<Page<EventoResumenResponse>> listarMios(Pageable pageable, Authentication authentication) {
        return ResponseEntity.ok(eventoService.listarPorEmpresa(obtenerUsuarioId(authentication), pageable));
    }

    // GET /api/eventos/5 -> detalle público (con fechas y boletos)
    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.obtenerPorId(id));
    }

    // GET /api/eventos/fechas/5/seatmap -> datos para inicializar el mapa interactivo (público)
    @GetMapping("/fechas/{fechaEventoId}/seatmap")
    public ResponseEntity<FechaSeatmapResponse> obtenerSeatmap(@PathVariable Long fechaEventoId) {
        return ResponseEntity.ok(eventoService.obtenerSeatmapDeFecha(fechaEventoId));
    }

    // Solo EMPRESA puede crear eventos (ver SecurityConfig)
    @PostMapping
    public ResponseEntity<EventoResponse> crear(
            @Valid @RequestBody EventoRequest dto, Authentication authentication) {
        EventoResponse creado = eventoService.crear(dto, obtenerUsuarioId(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EventoResponse> cambiarEstado(
            @PathVariable Long id, @Valid @RequestBody CambiarEstadoRequest dto, Authentication authentication) {
        return ResponseEntity.ok(eventoService.cambiarEstado(id, dto.getEstado(), obtenerUsuarioId(authentication)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication authentication) {
        eventoService.eliminar(id, obtenerUsuarioId(authentication));
        return ResponseEntity.noContent().build();
    }

    private Long obtenerUsuarioId(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuario.getUsuarioId();
    }
}
