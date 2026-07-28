package com.TESSERA.Eq13Tessera.eventos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "zona")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "recinto_id", nullable = false)
    private Long recintoId;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "capacidad", nullable = false)
    private Integer capacidad;

    // Color con el que se pinta la zona en el mapa (ej. "#33A1FF")
    @Column(name = "color")
    private String color;

    // Coordenadas dentro del SVG del recinto, guardadas como texto (ej. "[10,15]")
    @Column(name = "coordenadas")
    private String coordenadas;

    // Id de esta zona/sección DENTRO del mapa de seatmap.pro (para pintarla con su precio)
    @Column(name = "seatmap_object_id")
    private Long seatmapObjectId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRecintoId() { return recintoId; }
    public void setRecintoId(Long recintoId) { this.recintoId = recintoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getCoordenadas() { return coordenadas; }
    public void setCoordenadas(String coordenadas) { this.coordenadas = coordenadas; }

    public Long getSeatmapObjectId() { return seatmapObjectId; }
    public void setSeatmapObjectId(Long seatmapObjectId) { this.seatmapObjectId = seatmapObjectId; }
}
