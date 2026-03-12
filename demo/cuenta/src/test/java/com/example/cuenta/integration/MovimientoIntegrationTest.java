package com.example.cuenta.integration;

import com.example.cuenta.dto.CuentaRequest;
import com.example.cuenta.dto.MovimientoRequest;
import com.example.cuenta.exception.SaldoNoDisponibleException;
import com.example.cuenta.service.CuentaService;
import com.example.cuenta.service.MovimientoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class MovimientoIntegrationTest {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private MovimientoService movimientoService;

    @Test
    void registrarMovimiento_actualizaSaldoYRegistraTransaccion() {
        CuentaRequest cuentaReq = CuentaRequest.builder()
                .numeroCuenta("478758")
                .tipoCuenta("Ahorros")
                .saldoInicial(new BigDecimal("2000"))
                .estado(true)
                .clienteId(1L)
                .build();
        var cuenta = cuentaService.create(cuentaReq);
        assertThat(cuenta.getSaldoDisponible()).isEqualByComparingTo("2000");

        MovimientoRequest dep = MovimientoRequest.builder()
                .cuentaId(cuenta.getId())
                .tipoMovimiento("Deposito")
                .valor(new BigDecimal("100"))
                .build();
        var mov1 = movimientoService.registrar(dep);
        assertThat(mov1.getSaldo()).isEqualByComparingTo("2100");

        MovimientoRequest ret = MovimientoRequest.builder()
                .cuentaId(cuenta.getId())
                .tipoMovimiento("Retiro")
                .valor(new BigDecimal("-575"))
                .build();
        var mov2 = movimientoService.registrar(ret);
        assertThat(mov2.getSaldo()).isEqualByComparingTo("1525");

        var cuentaActualizada = cuentaService.findById(cuenta.getId());
        assertThat(cuentaActualizada.getSaldoDisponible()).isEqualByComparingTo("1525");
    }

    @Test
    void registrarRetiro_sinSaldo_lanzaSaldoNoDisponible() {
        CuentaRequest cuentaReq = CuentaRequest.builder()
                .numeroCuenta("495878")
                .tipoCuenta("Ahorros")
                .saldoInicial(BigDecimal.ZERO)
                .estado(true)
                .clienteId(1L)
                .build();
        var cuenta = cuentaService.create(cuentaReq);

        MovimientoRequest ret = MovimientoRequest.builder()
                .cuentaId(cuenta.getId())
                .tipoMovimiento("Retiro")
                .valor(new BigDecimal("-100"))
                .build();

        assertThatThrownBy(() -> movimientoService.registrar(ret))
                .isInstanceOf(SaldoNoDisponibleException.class)
                .hasMessageContaining("Saldo no disponible");
    }
}
