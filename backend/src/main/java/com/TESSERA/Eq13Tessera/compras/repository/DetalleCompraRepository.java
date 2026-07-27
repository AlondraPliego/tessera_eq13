package com.TESSERA.Eq13Tessera.compras.repository;

import com.TESSERA.Eq13Tessera.compras.entity.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
    List<DetalleCompra> findByCompraId(Long compraId);
}
