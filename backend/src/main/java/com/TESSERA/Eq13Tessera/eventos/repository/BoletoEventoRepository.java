package com.TESSERA.Eq13Tessera.eventos.repository;

import com.TESSERA.Eq13Tessera.eventos.entity.BoletoEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoletoEventoRepository extends JpaRepository<BoletoEvento, Long> {
    List<BoletoEvento> findByEventoId(Long eventoId);
    void deleteByEventoId(Long eventoId);
}
