package com.TESSERA.Eq13Tessera.eventos.repository;

import com.TESSERA.Eq13Tessera.eventos.entity.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    Page<Evento> findByEmpresaId(Long empresaId, Pageable pageable);

    Page<Evento> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    Page<Evento> findByEstado(String estado, Pageable pageable);

    Page<Evento> findByNombreContainingIgnoreCaseAndEstado(String nombre, String estado, Pageable pageable);
}
