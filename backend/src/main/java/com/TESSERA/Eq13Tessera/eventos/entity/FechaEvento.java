package com.TESSERA.Eq13Tessera.eventos.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "fechas_eventos")
public class FechaEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "evento_id", nullable = false)
    private Long eventoId;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    @Column(name = "recinto_id", nullable = false)
    private Long recintoId;

    // Id del "evento" que seatmap.pro crea para esta función/fecha en particular
    @Column(name = "seatmap_event_id")
    private String seatmapEventId;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }

    public Long getRecintoId() { return recintoId; }
    public void setRecintoId(Long recintoId) { this.recintoId = recintoId; }

    public String getSeatmapEventId() { return seatmapEventId; }
    public void setSeatmapEventId(String seatmapEventId) { this.seatmapEventId = seatmapEventId; }
}