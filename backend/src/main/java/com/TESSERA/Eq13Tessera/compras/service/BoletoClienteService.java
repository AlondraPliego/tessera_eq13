package com.TESSERA.Eq13Tessera.compras.service;

import com.TESSERA.Eq13Tessera.compras.dto.MiBoletoResponse;
import com.TESSERA.Eq13Tessera.compras.entity.Compra;
import com.TESSERA.Eq13Tessera.compras.entity.DetalleCompra;
import com.TESSERA.Eq13Tessera.compras.repository.CompraRepository;
import com.TESSERA.Eq13Tessera.compras.repository.DetalleCompraRepository;
import com.TESSERA.Eq13Tessera.eventos.entity.BoletoEvento;
import com.TESSERA.Eq13Tessera.eventos.entity.Evento;
import com.TESSERA.Eq13Tessera.eventos.entity.FechaEvento;
import com.TESSERA.Eq13Tessera.eventos.entity.Zona;
import com.TESSERA.Eq13Tessera.eventos.repository.BoletoEventoRepository;
import com.TESSERA.Eq13Tessera.eventos.repository.EventoRepository;
import com.TESSERA.Eq13Tessera.eventos.repository.FechaEventoRepository;
import com.TESSERA.Eq13Tessera.eventos.repository.ZonaRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Arma la lista de "mis boletos" para el cliente logueado, en base a
// GET /api/compras/mias pero ya resuelto contra evento/zona/fecha,
// tal como lo necesita el frontend (MisBoletos.jsx).
@Service
public class BoletoClienteService {

    private final CompraRepository compraRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final BoletoEventoRepository boletoEventoRepository;
    private final EventoRepository eventoRepository;
    private final ZonaRepository zonaRepository;
    private final FechaEventoRepository fechaEventoRepository;

    public BoletoClienteService(CompraRepository compraRepository,
                                 DetalleCompraRepository detalleCompraRepository,
                                 BoletoEventoRepository boletoEventoRepository,
                                 EventoRepository eventoRepository,
                                 ZonaRepository zonaRepository,
                                 FechaEventoRepository fechaEventoRepository) {
        this.compraRepository = compraRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.boletoEventoRepository = boletoEventoRepository;
        this.eventoRepository = eventoRepository;
        this.zonaRepository = zonaRepository;
        this.fechaEventoRepository = fechaEventoRepository;
    }

    public List<MiBoletoResponse> listarMisBoletos(Long clienteId) {
        List<Compra> compras = compraRepository.findByClienteId(clienteId);

        // Cachés simples para no repetir consultas cuando varias compras comparten evento/zona
        Map<Long, BoletoEvento> boletosPorId = new HashMap<>();
        Map<Long, Evento> eventosPorId = new HashMap<>();
        Map<Long, Zona> zonasPorId = new HashMap<>();
        Map<Long, Optional<FechaEvento>> proximaFechaPorEvento = new HashMap<>();

        return compras.stream()
                .flatMap(compra -> detalleCompraRepository.findByCompraId(compra.getId()).stream()
                        .map(detalle -> mapear(compra, detalle, boletosPorId, eventosPorId, zonasPorId, proximaFechaPorEvento)))
                .filter(java.util.Objects::nonNull)
                .sorted(Comparator.comparing(
                        MiBoletoResponse::getFecha, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    private MiBoletoResponse mapear(Compra compra, DetalleCompra detalle,
                                     Map<Long, BoletoEvento> boletosPorId,
                                     Map<Long, Evento> eventosPorId,
                                     Map<Long, Zona> zonasPorId,
                                     Map<Long, Optional<FechaEvento>> proximaFechaPorEvento) {

        BoletoEvento boleto = boletosPorId.computeIfAbsent(detalle.getBoletoEventoId(),
                id -> boletoEventoRepository.findById(id).orElse(null));
        if (boleto == null) return null; // el tipo de boleto ya no existe (evento borrado, etc.)

        Evento evento = eventosPorId.computeIfAbsent(boleto.getEventoId(),
                id -> eventoRepository.findById(id).orElse(null));
        if (evento == null) return null;

        Zona zona = zonasPorId.computeIfAbsent(boleto.getZonaId(),
                id -> zonaRepository.findById(id).orElse(null));

        Optional<FechaEvento> proximaFecha = proximaFechaPorEvento.computeIfAbsent(evento.getId(),
                id -> fechaEventoRepository.findByEventoId(id).stream()
                        .min(Comparator.comparing(FechaEvento::getFecha).thenComparing(FechaEvento::getHora)));

        return new MiBoletoResponse(
                detalle.getId(),
                compra.getId(),
                compra.getEstado(),
                evento.getId(),
                evento.getNombre(),
                evento.getDescripcion(),
                zona != null ? zona.getNombre() : null,
                detalle.getCantidad(),
                boleto.getPrecio(),
                detalle.getSubtotal(),
                proximaFecha.map(FechaEvento::getFecha).orElse(null),
                proximaFecha.map(FechaEvento::getHora).orElse(null),
                proximaFecha.map(FechaEvento::getCiudad).orElse(null),
                evento.getFlyerPrincipal());
    }
}
