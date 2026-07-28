package com.TESSERA.Eq13Tessera.auth.repository;

import com.TESSERA.Eq13Tessera.auth.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    boolean existsByRfc(String rfc);
}
