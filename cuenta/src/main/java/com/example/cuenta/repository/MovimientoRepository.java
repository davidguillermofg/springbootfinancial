package com.example.cuenta.repository;

import com.example.cuenta.domain.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaIdOrderByFechaDesc(Long cuentaId);

    @Query("SELECT m FROM Movimiento m, Cuenta c WHERE m.cuentaId = c.id AND c.clienteId = :clienteId AND m.fecha BETWEEN :desde AND :hasta ORDER BY m.fecha DESC")
    List<Movimiento> findByClienteIdAndFechaBetween(@Param("clienteId") Long clienteId,
                                                    @Param("desde") LocalDateTime desde,
                                                    @Param("hasta") LocalDateTime hasta);
}
