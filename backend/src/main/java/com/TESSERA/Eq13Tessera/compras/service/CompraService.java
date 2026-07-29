package com.TESSERA.Eq13Tessera.compras.service;

import com.TESSERA.Eq13Tessera.auth.entity.Cliente;
import com.TESSERA.Eq13Tessera.auth.entity.Usuario;
import com.TESSERA.Eq13Tessera.auth.repository.ClienteRepository;
import com.TESSERA.Eq13Tessera.auth.repository.UsuarioRepository;
import com.TESSERA.Eq13Tessera.common.exception.OperacionNoPermitidaException;
import com.TESSERA.Eq13Tessera.common.exception.ResourceNotFoundException;
import com.TESSERA.Eq13Tessera.common.exception.StockInsuficienteException;
import com.TESSERA.Eq13Tessera.compras.dto.CompraRequest;
import com.TESSERA.Eq13Tessera.compras.dto.CompraResponse;
import com.TESSERA.Eq13Tessera.compras.dto.DetalleCompraDTO;
import com.TESSERA.Eq13Tessera.compras.entity.Compra;
import com.TESSERA.Eq13Tessera.compras.entity.DetalleCompra;
import com.TESSERA.Eq13Tessera.compras.repository.CompraRepository;
import com.TESSERA.Eq13Tessera.compras.repository.DetalleCompraRepository;
import com.TESSERA.Eq13Tessera.eventos.entity.BoletoEvento;
import com.TESSERA.Eq13Tessera.eventos.repository.BoletoEventoRepository;
import com.TESSERA.Eq13Tessera.notificaciones.service.MailService;
import com.TESSERA.Eq13Tessera.notificaciones.service.SMService;
import com.TESSERA.Eq13Tessera.notificaciones.service.WhatsAppService;
import com.TESSERA.Eq13Tessera.reservas.service.ReservaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final BoletoEventoRepository boletoEventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final MailService mailService;
    private final SMService smService;
    private final WhatsAppService whatsAppService;
    private final ReservaService reservaService;

    public CompraService(CompraRepository compraRepository,
                          DetalleCompraRepository detalleCompraRepository,
                          BoletoEventoRepository boletoEventoRepository,
                          UsuarioRepository usuarioRepository,
                          ClienteRepository clienteRepository,
                          MailService mailService,
                          SMService smService,
                          WhatsAppService whatsAppService,
                          ReservaService reservaService) {
        this.compraRepository = compraRepository;
        this.detalleCompraRepository = detalleCompraRepository;
        this.boletoEventoRepository = boletoEventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
        this.mailService = mailService;
        this.smService = smService;
        this.whatsAppService = whatsAppService;
        this.reservaService = reservaService;
    }

    // --- CREAR COMPRA ---
    // Nota para el equipo: aquí NO conectamos una pasarela de pago real todavía.
    // Simulamos que el pago se aprueba al instante y dejamos la compra en "PAGADA".
    @Transactional
    public CompraResponse crear(CompraRequest dto, Long clienteId) {
        BigDecimal total = BigDecimal.ZERO;

        // 1) Validamos ANTES de tocar la base de datos:
        //    - Si trae reservaId: que la reserva exista, sea del cliente, no haya
        //      expirado y coincida con lo que se está comprando (no vuelve a tocar stock,
        //      ya se descontó cuando se creó la reserva).
        //    - Si NO trae reservaId: comportamiento anterior, valida stock directo.
        for (DetalleCompraDTO d : dto.getDetalles()) {
            if (d.getReservaId() != null) {
                reservaService.consumir(d.getReservaId(), clienteId, d.getBoletoEventoId(), d.getCantidad());
                continue;
            }
            BoletoEvento boleto = boletoEventoRepository.findById(d.getBoletoEventoId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No existe el boleto con id " + d.getBoletoEventoId()));

            if (boleto.getCantidadDisponible() < d.getCantidad()) {
                throw new StockInsuficienteException(
                        "Ya no hay suficientes boletos disponibles para la zona seleccionada (id boleto: "
                                + boleto.getId() + "). Disponibles: " + boleto.getCantidadDisponible());
            }
        }

        // 2) Creamos la compra (todavía sin total, lo llenamos abajo)
        Compra compra = new Compra();
        compra.setClienteId(clienteId);
        compra.setTotal(BigDecimal.ZERO);
        compra.setEstado("PAGADA");
        compra = compraRepository.save(compra);

        // 3) Por cada boleto: calculamos el subtotal y guardamos el detalle.
        //    Solo descontamos inventario aquí si NO venía de una reserva
        //    (si venía de reserva, el inventario ya se descontó al reservar).
        for (DetalleCompraDTO d : dto.getDetalles()) {
            BoletoEvento boleto = boletoEventoRepository.findById(d.getBoletoEventoId()).orElseThrow();

            BigDecimal subtotal = boleto.getPrecio().multiply(BigDecimal.valueOf(d.getCantidad()));
            total = total.add(subtotal);

            if (d.getReservaId() == null) {
                boleto.setCantidadDisponible(boleto.getCantidadDisponible() - d.getCantidad());
                boletoEventoRepository.save(boleto);
            }

            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompraId(compra.getId());
            detalle.setBoletoEventoId(boleto.getId());
            detalle.setCantidad(d.getCantidad());
            detalle.setSubtotal(subtotal);
            detalleCompraRepository.save(detalle);
        }

        compra.setTotal(total);
        compra = compraRepository.save(compra);

        notificarCompra(compra);

        return toResponse(compra);
    }

    // Avisa al cliente que su compra se realizó (correo siempre; SMS/WhatsApp
    // solo si el cliente dejó su teléfono al registrarse)
    private void notificarCompra(Compra compra) {
        Usuario usuario = usuarioRepository.findById(compra.getClienteId()).orElse(null);
        if (usuario == null) return;

        mailService.enviarCorreoConfirmacionCompra(usuario.getEmail(), compra.getId(), compra.getTotal());

        Cliente cliente = clienteRepository.findById(compra.getClienteId()).orElse(null);
        if (cliente != null && cliente.getTelefono() != null && !cliente.getTelefono().isBlank()) {
            String mensaje = "Tessera: tu compra #" + compra.getId() + " por $" + compra.getTotal()
                    + " fue confirmada. ¡Gracias por tu compra!";
            smService.enviarSms(cliente.getTelefono(), mensaje);
            whatsAppService.enviarWhatsapp(cliente.getTelefono(), mensaje);
        }
    }

    // --- LISTAR MIS COMPRAS (cliente autenticado) ---
    public Page<CompraResponse> listarPorCliente(Long clienteId, Pageable pageable) {
        return compraRepository.findByClienteId(clienteId, pageable).map(this::toResponse);
    }

    // --- LISTAR TODAS (solo administrador) ---
    public Page<CompraResponse> listarTodas(Pageable pageable) {
        return compraRepository.findAll(pageable).map(this::toResponse);
    }

    // --- VER DETALLE DE UNA COMPRA ---
    public CompraResponse obtenerPorId(Long id, Long clienteId, boolean esAdmin) {
        Compra compra = buscarOFallar(id);
        if (!esAdmin && !compra.getClienteId().equals(clienteId)) {
            throw new OperacionNoPermitidaException("Esta compra no te pertenece");
        }
        return toResponse(compra);
    }

    // --- CANCELAR COMPRA (regresa el inventario) ---
    @Transactional
    public CompraResponse cancelar(Long id, Long clienteId) {
        Compra compra = buscarOFallar(id);
        if (!compra.getClienteId().equals(clienteId)) {
            throw new OperacionNoPermitidaException("Esta compra no te pertenece");
        }
        if (compra.getEstado().equals("CANCELADA")) {
            throw new OperacionNoPermitidaException("Esta compra ya estaba cancelada");
        }

        List<DetalleCompra> detalles = detalleCompraRepository.findByCompraId(id);
        for (DetalleCompra d : detalles) {
            BoletoEvento boleto = boletoEventoRepository.findById(d.getBoletoEventoId()).orElseThrow();
            boleto.setCantidadDisponible(boleto.getCantidadDisponible() + d.getCantidad());
            boletoEventoRepository.save(boleto);
        }

        compra.setEstado("CANCELADA");
        return toResponse(compraRepository.save(compra));
    }

    // --- helpers ---

    private Compra buscarOFallar(Long id) {
        return compraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una compra con id " + id));
    }

    private CompraResponse toResponse(Compra c) {
        List<DetalleCompraDTO> detalles = detalleCompraRepository.findByCompraId(c.getId()).stream()
                .map(d -> new DetalleCompraDTO(d.getBoletoEventoId(), d.getCantidad(), null, d.getSubtotal()))
                .toList();
        return new CompraResponse(c.getId(), c.getClienteId(), c.getFecha(), c.getTotal(), c.getEstado(), detalles);
    }
}
