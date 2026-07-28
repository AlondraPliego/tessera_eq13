package com.TESSERA.Eq13Tessera.eventos.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recinto")
public class Recinto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    // Guardamos aquí el SVG del mapa del recinto (para poder pintarlo en el frontend)
    @Column(name = "mapa_svg", columnDefinition = "TEXT")
    private String mapaSvg;

    // Id de la empresa (usuario con rol EMPRESA) dueña de este recinto
    @Column(name = "empresa_id", nullable = false)
    private Long empresaId;

    // Id del mapa (schema) diseñado en el editor de seatmap.pro para este recinto
    @Column(name = "seatmap_schema_id")
    private Long seatmapSchemaId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getMapaSvg() { return mapaSvg; }
    public void setMapaSvg(String mapaSvg) { this.mapaSvg = mapaSvg; }

    public Long getEmpresaId() { return empresaId; }
    public void setEmpresaId(Long empresaId) { this.empresaId = empresaId; }

    public Long getSeatmapSchemaId() { return seatmapSchemaId; }
    public void setSeatmapSchemaId(Long seatmapSchemaId) { this.seatmapSchemaId = seatmapSchemaId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
