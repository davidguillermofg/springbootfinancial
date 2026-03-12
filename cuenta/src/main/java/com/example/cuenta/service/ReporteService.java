package com.example.cuenta.service;

import com.example.cuenta.domain.Cuenta;
import com.example.cuenta.domain.Movimiento;
import com.example.cuenta.dto.ReporteResponse;
import com.example.cuenta.repository.CuentaRepository;
import com.example.cuenta.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final ClienteCache clienteCache;

    @Transactional(readOnly = true)
    public ReporteResponse estadoDeCuenta(Long clienteId, String nombreCliente, LocalDate fechaDesde, LocalDate fechaHasta) {
        String nombre = nombreCliente != null ? nombreCliente : clienteCache.getNombre(clienteId);
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        LocalDateTime desde = fechaDesde != null ? fechaDesde.atStartOfDay() : LocalDateTime.MIN;
        LocalDateTime hasta = fechaHasta != null ? fechaHasta.atTime(LocalTime.MAX) : LocalDateTime.MAX;

        List<ReporteResponse.CuentaReporteItem> items = cuentas.stream()
                .map(c -> {
                    List<Movimiento> movs = movimientoRepository.findByClienteIdAndFechaBetween(clienteId, desde, hasta).stream()
                            .filter(m -> m.getCuentaId().equals(c.getId()))
                            .collect(Collectors.toList());
                    return ReporteResponse.CuentaReporteItem.builder()
                            .numeroCuenta(c.getNumeroCuenta())
                            .tipoCuenta(c.getTipoCuenta())
                            .saldoInicial(c.getSaldoInicial())
                            .saldoDisponible(c.getSaldoDisponible())
                            .estado(c.getEstado())
                            .movimientos(movs.stream()
                                    .map(m -> ReporteResponse.MovimientoReporteItem.builder()
                                            .fecha(m.getFecha())
                                            .tipoMovimiento(m.getTipoMovimiento())
                                            .valor(m.getValor())
                                            .saldo(m.getSaldo())
                                            .build())
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());

        return ReporteResponse.builder()
                .cliente(nombre != null ? nombre : "Cliente " + clienteId)
                .clienteId(clienteId)
                .cuentas(items)
                .build();
    }
}
