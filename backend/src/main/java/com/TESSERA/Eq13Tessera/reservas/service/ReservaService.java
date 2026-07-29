package com.TESSERA.Eq13Tessera.reservas.service;

import com.TESSERA.Eq13Tessera.common.exception.OperacionNoPermitidaException;
import com.TESSERA.Eq13Tessera.common.exception.ResourceNotFoundException;
import com.TESSERA.Eq13Tessera.common.exception.StockInsuficienteException;
import com.TESSERA.Eq13Tessera.eventos.entity.BoletoEvento;
import com.TESSERA.Eq13Tessera.eventos.repository.BoletoEventoRepository;
import com.TESSERA.Eq13Tessera.reservas.dto.ReservaRequest;
import com.TESSERA.Eq13Tessera.reservas.dto.ReservaResponse;
import com.TESSERA.Eq13Tessera.reservas.entity.Reserva;
import com.TESSERA.Eq13Tessera.reservas.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final BoletoEventoRepository boletoEventoRepository;

    // Minutos que dura una reserva antes de liberarse sola.
    // Configurable en application.properties como reserva.minutos-expiracion=5
    @Value("${reserva.minutos-expiracion:5}")
    private int minutosExpiracion;

    public ReservaService(ReservaRepository reservaRepository,
                           BoletoEventoRepository boletoEventoRepository) {
        this.reservaRepository = reservaRepository;
        this.boletoEventoRepository = boletoEventoRepository;
    }

    // --- CREAR RESERVA: aparta el inventario YA, antes de que el cliente pague ---
    @Transactional
    public ReservaResponse crear(ReservaRequest dto, Long clienteId) {
        BoletoEvento boleto = boletoEventoRepository.findById(dto.getBoletoEventoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe el boleto con id " + dto.getBoletoEventoId()));

        if (boleto.getCantidadDisponible() < dto.getCantidad()) {
            throw new StockInsuficienteException(
                    "Ya no hay suficientes boletos disponibles para esta zona. Disponibles: "
                            + boleto.getCantidadDisponible());
        }

        boleto.setCantidadDisponible(boleto.getCantidadDisponible() - dto.getCantidad());
        boletoEventoRepository.save(boleto);

        Reserva reserva = new Reserva();
        reserva.setClienteId(clienteId);
        reserva.setBoletoEventoId(boleto.getId());
        reserva.setCantidad(dto.getCantidad());
        reserva.setExpiraEn(LocalDateTime.now().plusMinutes(minutosExpiracion));
        reserva = reservaRepository.save(reserva);

        return toResponse(reserva, boleto.getPrecio());
    }

    // --- LIBERAR RESERVA A MANO (el cliente quita el asiento del carrito, o cancela) ---
    @Transactional
    public void liberar(Long reservaId, Long clienteId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la reserva con id " + reservaId));

        if (!reserva.getClienteId().equals(clienteId)) {
            throw new OperacionNoPermitidaException("Esta reserva no te pertenece");
        }

        devolverInventario(reserva);
        reservaRepository.delete(reserva);
    }

    // --- USADO POR CompraService: valida y "consume" la reserva al confirmar el pago ---
    // No vuelve a tocar el inventario (ya se descontó al reservar), solo valida
    // que la reserva exista, sea del cliente correcto, no haya expirado, y que
    // coincida con lo que se está comprando. La borra al final (ya se convirtió en compra).
    @Transactional
    public Reserva consumir(Long reservaId, Long clienteId, Long boletoEventoIdEsperado, Integer cantidadEsperada) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la reserva con id " + reservaId));

        if (!reserva.getClienteId().equals(clienteId)) {
            throw new OperacionNoPermitidaException("Esta reserva no te pertenece");
        }
        if (reserva.getExpiraEn().isBefore(LocalDateTime.now())) {
            reservaRepository.delete(reserva); // ya expiró, el scheduler no ha pasado todavía: la limpiamos aquí
            throw new OperacionNoPermitidaException(
                    "Tu reserva expiró. Vuelve a seleccionar el asiento e intenta de nuevo.");
        }
        if (!reserva.getBoletoEventoId().equals(boletoEventoIdEsperado)
                || !reserva.getCantidad().equals(cantidadEsperada)) {
            throw new OperacionNoPermitidaException("La reserva no coincide con lo que intentas comprar");
        }

        reservaRepository.delete(reserva);
        return reserva;
    }

    // --- SCHEDULER: corre cada minuto, libera reservas vencidas que nadie pagó ---
    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void liberarReservasVencidas() {
        List<Reserva> vencidas = reservaRepository.findByExpiraEnBefore(LocalDateTime.now());
        for (Reserva reserva : vencidas) {
            devolverInventario(reserva);
            reservaRepository.delete(reserva);
        }
    }

    private void devolverInventario(Reserva reserva) {
        boletoEventoRepository.findById(reserva.getBoletoEventoId()).ifPresent(boleto -> {
            boleto.setCantidadDisponible(boleto.getCantidadDisponible() + reserva.getCantidad());
            boletoEventoRepository.save(boleto);
        });
    }

    private ReservaResponse toResponse(Reserva r, java.math.BigDecimal precioUnitario) {
        return new ReservaResponse(r.getId(), r.getBoletoEventoId(), r.getCantidad(), precioUnitario, r.getExpiraEn());
    }
}
