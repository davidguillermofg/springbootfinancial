package com.example.cuenta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaRequest {

    @NotBlank(message = "numeroCuenta es requerido")
    private String numeroCuenta;
    @NotBlank(message = "tipoCuenta es requerido")
    private String tipoCuenta;
    @NotNull
    private BigDecimal saldoInicial;
    @NotNull
    private Boolean estado;
    @NotNull
    private Long clienteId;
}
