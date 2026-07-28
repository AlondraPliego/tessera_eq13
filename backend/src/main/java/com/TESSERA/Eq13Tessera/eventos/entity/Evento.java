package com.TESSERA.Eq13Tessera.eventos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    // La fecha, hora, ciudad y recinto de cada función viven en la tabla
    // "fechas_eventos" (un evento puede tener varias funciones/fechas).

    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "flyer_principal")
    private String flyerPrincipal;

    @Column(name = "flyer_secundario")
    private String flyerSecundario;

    @Column(name = "flyer_terciario")
    private String flyerTerciario;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFlyerPrincipal() { return flyerPrincipal; }
    public void setFlyerPrincipal(String flyerPrincipal) { this.flyerPrincipal = flyerPrincipal; }

    public String getFlyerSecundario() { return flyerSecundario; }
    public void setFlyerSecundario(String flyerSecundario) { this.flyerSecundario = flyerSecundario; }

    public String getFlyerTerciario() { return flyerTerciario; }
    public void setFlyerTerciario(String flyerTerciario) { this.flyerTerciario = flyerTerciario; }
}