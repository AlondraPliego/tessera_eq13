package com.TESSERA.Eq13Tessera.eventos.repository;

import com.TESSERA.Eq13Tessera.eventos.entity.Recinto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecintoRepository extends JpaRepository<Recinto, Long> {

    // Para listar solo los recintos de una empresa (paginado)
    Page<Recinto> findByEmpresaId(Long empresaId, Pageable pageable);

    // Para el filtro de búsqueda por nombre (paginado)
    Page<Recinto> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
