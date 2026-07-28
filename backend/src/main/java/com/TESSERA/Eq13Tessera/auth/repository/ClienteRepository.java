package com.TESSERA.Eq13Tessera.auth.repository;

import com.TESSERA.Eq13Tessera.auth.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByNombreUsuario(String nombreUsuario);
}
