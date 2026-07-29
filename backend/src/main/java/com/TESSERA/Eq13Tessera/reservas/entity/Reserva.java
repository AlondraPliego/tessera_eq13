package com.TESSERA.Eq13Tessera.reservas.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// Un apartado temporal de N boletos de UN boleto_evento, mientras el
// cliente decide si compra. Si expira_en pasa sin que se vuelva compra,
// el scheduler de ReservaService la libera y regresa el inventario.
@Entity
@Table(name = "reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "boleto_evento_id", nullable = false)
    private Long boletoEventoId;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "creada_en", insertable = false, updatable = false)
    private LocalDateTime creadaEn;

    @Column(name = "expira_en", nullable = false)
    private LocalDateTime expiraEn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getBoletoEventoId() { return boletoEventoId; }
    public void setBoletoEventoId(Long boletoEventoId) { this.boletoEventoId = boletoEventoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public LocalDateTime getCreadaEn() { return creadaEn; }

    public LocalDateTime getExpiraEn() { return expiraEn; }
    public void setExpiraEn(LocalDateTime expiraEn) { this.expiraEn = expiraEn; }
}
