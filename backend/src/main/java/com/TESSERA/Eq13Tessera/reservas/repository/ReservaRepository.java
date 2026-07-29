package com.TESSERA.Eq13Tessera.reservas.repository;

import com.TESSERA.Eq13Tessera.reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByExpiraEnBefore(LocalDateTime ahora);
    List<Reserva> findByClienteId(Long clienteId);
}
