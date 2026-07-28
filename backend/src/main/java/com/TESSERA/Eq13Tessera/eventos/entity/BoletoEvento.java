package com.TESSERA.Eq13Tessera.eventos.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

// Un "tipo de boleto" = el precio y la cantidad disponible de UNA zona para UN evento
@Entity
@Table(name = "boleto_evento")
public class BoletoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "evento_id", nullable = false)
    private Long eventoId;

    @Column(name = "zona_id", nullable = false)
    private Long zonaId;

    @Column(name = "precio", nullable = false)
    private BigDecimal precio;

    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public Long getZonaId() { return zonaId; }
    public void setZonaId(Long zonaId) { this.zonaId = zonaId; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(Integer cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
}
