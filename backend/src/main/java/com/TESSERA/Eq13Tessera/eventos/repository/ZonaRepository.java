package com.TESSERA.Eq13Tessera.eventos.repository;

import com.TESSERA.Eq13Tessera.eventos.entity.Zona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZonaRepository extends JpaRepository<Zona, Long> {
    List<Zona> findByRecintoId(Long recintoId);
}
