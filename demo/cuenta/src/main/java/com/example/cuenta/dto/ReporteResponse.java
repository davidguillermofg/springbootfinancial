package com.example.cuenta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResponse {

    private String cliente;
    private Long clienteId;
    private List<CuentaReporteItem> cuentas;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CuentaReporteItem {
        private String numeroCuenta;
        private String tipoCuenta;
        private BigDecimal saldoInicial;
        private BigDecimal saldoDisponible;
        private Boolean estado;
        private List<MovimientoReporteItem> movimientos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovimientoReporteItem {
        private LocalDateTime fecha;
        private String tipoMovimiento;
        private BigDecimal valor;
        private BigDecimal saldo;
    }
}
