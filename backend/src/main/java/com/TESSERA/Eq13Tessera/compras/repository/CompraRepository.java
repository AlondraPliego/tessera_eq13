package com.TESSERA.Eq13Tessera.compras.repository;

import com.TESSERA.Eq13Tessera.compras.entity.Compra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    Page<Compra> findByClienteId(Long clienteId, Pageable pageable);

    // Usado por BoletoClienteService para armar "mis boletos" (sin paginar,
    // ya que se necesitan todas las compras del cliente para separar activos/historial).
    List<Compra> findByClienteId(Long clienteId);
}
