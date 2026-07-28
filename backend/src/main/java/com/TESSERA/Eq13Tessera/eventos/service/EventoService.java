package com.TESSERA.Eq13Tessera.eventos.service;

import com.TESSERA.Eq13Tessera.common.exception.OperacionNoPermitidaException;
import com.TESSERA.Eq13Tessera.common.exception.ResourceNotFoundException;
import com.TESSERA.Eq13Tessera.compras.service.SeatmapService;
import com.TESSERA.Eq13Tessera.eventos.dto.*;
import com.TESSERA.Eq13Tessera.eventos.entity.*;
import com.TESSERA.Eq13Tessera.eventos.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final FechaEventoRepository fechaEventoRepository;
    private final BoletoEventoRepository boletoEventoRepository;
    private final RecintoRepository recintoRepository;
    private final ZonaRepository zonaRepository;
    private final SeatmapService seatmapService;

    @Value("${seatmap.public-key}")
    private String seatmapPublicKey;

    public EventoService(EventoRepository eventoRepository,
                          FechaEventoRepository fechaEventoRepository,
                          BoletoEventoRepository boletoEventoRepository,
                          RecintoRepository recintoRepository,
                          ZonaRepository zonaRepository,
                          SeatmapService seatmapService) {
        this.eventoRepository = eventoRepository;
        this.fechaEventoRepository = fechaEventoRepository;
        this.boletoEventoRepository = boletoEventoRepository;
        this.recintoRepository = recintoRepository;
        this.zonaRepository = zonaRepository;
        this.seatmapService = seatmapService;
    }

    // --- LISTAR (resumen, paginado, con filtros opcionales) ---
    public Page<EventoResumenResponse> listar(String nombre, String estado, Pageable pageable) {
        Page<Evento> pagina;
        boolean tieneNombre = StringUtils.hasText(nombre);
        boolean tieneEstado = StringUtils.hasText(estado);

        if (tieneNombre && tieneEstado) {
            pagina = eventoRepository.findByNombreContainingIgnoreCaseAndEstado(nombre, estado.toUpperCase(), pageable);
        } else if (tieneNombre) {
            pagina = eventoRepository.findByNombreContainingIgnoreCase(nombre, pageable);
        } else if (tieneEstado) {
            pagina = eventoRepository.findByEstado(estado.toUpperCase(), pageable);
        } else {
            pagina = eventoRepository.findAll(pageable);
        }
        return pagina.map(this::toResumen);
    }

    public Page<EventoResumenResponse> listarPorEmpresa(Long empresaId, Pageable pageable) {
        return eventoRepository.findByEmpresaId(empresaId, pageable).map(this::toResumen);
    }

    // --- DETALLE (con fechas y boletos) ---
    public EventoResponse obtenerPorId(Long id) {
        Evento evento = buscarOFallar(id);
        return toResponse(evento);
    }

    // --- CREAR ---
    @Transactional
    public EventoResponse crear(EventoRequest dto, Long empresaId) {
        // Validamos que los recintos de cada fecha existan y sean de esta empresa
        // (de paso los guardamos en un mapa para no consultarlos dos veces)
        Map<Long, Recinto> recintosPorId = new HashMap<>();
        for (FechaEventoRequest f : dto.getFechas()) {
            Recinto recinto = recintoRepository.findById(f.getRecintoId())
                    .orElseThrow(() -> new ResourceNotFoundException("No existe el recinto con id " + f.getRecintoId()));
            if (!recinto.getEmpresaId().equals(empresaId)) {
                throw new OperacionNoPermitidaException("Solo puedes usar recintos de tu propia empresa");
            }
            recintosPorId.put(recinto.getId(), recinto);
        }
        // Validamos que las zonas de cada boleto existan (y las guardamos para usarlas después)
        Map<Long, Zona> zonasPorId = new HashMap<>();
        for (BoletoEventoRequest b : dto.getBoletos()) {
            Zona zona = zonaRepository.findById(b.getZonaId())
                    .orElseThrow(() -> new ResourceNotFoundException("No existe la zona con id " + b.getZonaId()));
            zonasPorId.put(zona.getId(), zona);
        }

        Evento evento = new Evento();
        evento.setNombre(dto.getNombre());
        evento.setDescripcion(dto.getDescripcion());
        evento.setEmpresaId(empresaId);
        evento.setEstado("PROGRAMADO");
        evento.setFlyerPrincipal(dto.getFlyerPrincipal());
        evento.setFlyerSecundario(dto.getFlyerSecundario());
        evento.setFlyerTerciario(dto.getFlyerTerciario());
        evento = eventoRepository.save(evento);

        // Por cada fecha: la guardamos Y creamos su "evento" correspondiente en seatmap.pro
        List<FechaEvento> fechasGuardadas = new java.util.ArrayList<>();
        for (FechaEventoRequest f : dto.getFechas()) {
            FechaEvento fecha = new FechaEvento();
            fecha.setEventoId(evento.getId());
            fecha.setFecha(f.getFecha());
            fecha.setHora(f.getHora());
            fecha.setCiudad(f.getCiudad());
            fecha.setRecintoId(f.getRecintoId());

            Recinto recinto = recintosPorId.get(f.getRecintoId());
            seatmapService.crearEventoParaFecha(
                    recinto.getSeatmapSchemaId(), evento.getNombre() + " - " + f.getFecha(), f.getFecha(), f.getHora()
            ).ifPresent(fecha::setSeatmapEventId);

            fechasGuardadas.add(fechaEventoRepository.save(fecha));
        }

        for (BoletoEventoRequest b : dto.getBoletos()) {
            BoletoEvento boleto = new BoletoEvento();
            boleto.setEventoId(evento.getId());
            boleto.setZonaId(b.getZonaId());
            boleto.setPrecio(b.getPrecio());
            boleto.setCantidadDisponible(b.getCantidadDisponible());
            boletoEventoRepository.save(boleto);

            // Pintamos esta zona con su precio en CADA función/fecha que tenga evento en seatmap.pro
            Zona zona = zonasPorId.get(b.getZonaId());
            for (FechaEvento fecha : fechasGuardadas) {
                seatmapService.crearYAsignarPrecio(fecha.getSeatmapEventId(), zona.getSeatmapObjectId(), b.getPrecio());
            }
        }

        return toResponse(evento);
    }

    // --- CAMBIAR ESTADO (ej. PROGRAMADO -> CANCELADO) ---
    public EventoResponse cambiarEstado(Long id, String nuevoEstado, Long empresaId) {
        Evento evento = buscarOFallar(id);
        validarPropietario(evento, empresaId);
        evento.setEstado(nuevoEstado.toUpperCase());
        return toResponse(eventoRepository.save(evento));
    }

    // --- ELIMINAR ---
    @Transactional
    public void eliminar(Long id, Long empresaId) {
        Evento evento = buscarOFallar(id);
        validarPropietario(evento, empresaId);

        // Borramos también los eventos que se crearon en seatmap.pro para cada fecha
        for (FechaEvento fecha : fechaEventoRepository.findByEventoId(id)) {
            seatmapService.eliminarEvento(fecha.getSeatmapEventId());
        }

        boletoEventoRepository.deleteByEventoId(id);
        fechaEventoRepository.deleteByEventoId(id);
        eventoRepository.delete(evento);
    }

    // --- Info que necesita el frontend para mostrar el mapa interactivo de una función ---
    public FechaSeatmapResponse obtenerSeatmapDeFecha(Long fechaEventoId) {
        FechaEvento fecha = fechaEventoRepository.findById(fechaEventoId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la fecha con id " + fechaEventoId));
        return new FechaSeatmapResponse(fecha.getSeatmapEventId(), seatmapPublicKey);
    }

    // --- helpers ---

    private Evento buscarOFallar(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un evento con id " + id));
    }

    private void validarPropietario(Evento evento, Long empresaId) {
        if (!evento.getEmpresaId().equals(empresaId)) {
            throw new OperacionNoPermitidaException("Este evento no pertenece a tu empresa");
        }
    }

    private EventoResumenResponse toResumen(Evento e) {
        return new EventoResumenResponse(
                e.getId(), e.getNombre(), e.getDescripcion(), e.getEmpresaId(), e.getEstado(), e.getFlyerPrincipal());
    }

    private EventoResponse toResponse(Evento e) {
        List<FechaEventoResponse> fechas = fechaEventoRepository.findByEventoId(e.getId()).stream()
                .map(f -> new FechaEventoResponse(
                        f.getId(), f.getFecha(), f.getHora(), f.getCiudad(), f.getRecintoId(), f.getSeatmapEventId()))
                .toList();
        List<BoletoEventoResponse> boletos = boletoEventoRepository.findByEventoId(e.getId()).stream()
                .map(b -> new BoletoEventoResponse(b.getId(), b.getZonaId(), b.getPrecio(), b.getCantidadDisponible()))
                .toList();
        return new EventoResponse(
                e.getId(), e.getNombre(), e.getDescripcion(), e.getEmpresaId(), e.getEstado(),
                e.getFlyerPrincipal(), e.getFlyerSecundario(), e.getFlyerTerciario(), fechas, boletos);
    }
}
