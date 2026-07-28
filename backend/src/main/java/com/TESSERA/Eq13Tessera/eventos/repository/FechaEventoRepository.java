package com.TESSERA.Eq13Tessera.eventos.repository;

import com.TESSERA.Eq13Tessera.eventos.entity.FechaEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FechaEventoRepository extends JpaRepository<FechaEvento, Long> {
    List<FechaEvento> findByEventoId(Long eventoId);
    void deleteByEventoId(Long eventoId);
}
