package com.TESSERA.Eq13Tessera.compras.controller;

import com.TESSERA.Eq13Tessera.auth.entity.Usuario;
import com.TESSERA.Eq13Tessera.compras.dto.CompraRequest;
import com.TESSERA.Eq13Tessera.compras.dto.CompraResponse;
import com.TESSERA.Eq13Tessera.compras.dto.MiBoletoResponse;
import com.TESSERA.Eq13Tessera.compras.service.BoletoClienteService;
import com.TESSERA.Eq13Tessera.compras.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;
    private final BoletoClienteService boletoClienteService;

    public CompraController(CompraService compraService, BoletoClienteService boletoClienteService) {
        this.compraService = compraService;
        this.boletoClienteService = boletoClienteService;
    }

    // Solo un CLIENTE puede comprar boletos (ver SecurityConfig)
    @PostMapping
    public ResponseEntity<CompraResponse> crear(
            @Valid @RequestBody CompraRequest dto, Authentication authentication) {
        CompraResponse creada = compraService.crear(dto, obtenerUsuarioId(authentication));
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // GET /api/compras/mias -> el historial de compras del cliente logueado
    @GetMapping("/mias")
    public ResponseEntity<Page<CompraResponse>> listarMias(Pageable pageable, Authentication authentication) {
        return ResponseEntity.ok(compraService.listarPorCliente(obtenerUsuarioId(authentication), pageable));
    }

    // GET /api/compras/mias/boletos -> "mis boletos" ya resueltos contra evento/zona/fecha,
    // en el formato que necesita el frontend para "Mis boletos" (activos + historial).
    @GetMapping("/mias/boletos")
    public ResponseEntity<List<MiBoletoResponse>> listarMisBoletos(Authentication authentication) {
        return ResponseEntity.ok(boletoClienteService.listarMisBoletos(obtenerUsuarioId(authentication)));
    }

    // GET /api/compras -> TODAS las compras, solo para el administrador
    @GetMapping
    public ResponseEntity<Page<CompraResponse>> listarTodas(Pageable pageable) {
        return ResponseEntity.ok(compraService.listarTodas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponse> obtener(@PathVariable Long id, Authentication authentication) {
        boolean esAdmin = esAdmin(authentication);
        return ResponseEntity.ok(compraService.obtenerPorId(id, obtenerUsuarioId(authentication), esAdmin));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<CompraResponse> cancelar(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(compraService.cancelar(id, obtenerUsuarioId(authentication)));
    }

    private Long obtenerUsuarioId(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return usuario.getUsuarioId();
    }

    private boolean esAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
