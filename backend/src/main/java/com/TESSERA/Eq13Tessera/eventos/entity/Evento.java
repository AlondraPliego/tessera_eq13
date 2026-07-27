package com.TESSERA.Eq13Tessera.eventos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "recinto_id", nullable = false)
    private Long recintoId;

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

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Long getRecintoId() { return recintoId; }
    public void setRecintoId(Long recintoId) { this.recintoId = recintoId; }

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