package com.TESSERA.Eq13Tessera.compras.repository;

import com.TESSERA.Eq13Tessera.compras.entity.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    Page<Compra> findByClienteId(Long clienteId, Pageable pageable);
}
