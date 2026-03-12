package com.example.cuenta.dto;

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
public class MovimientoRequest {

    @NotNull
    private Long cuentaId;
    @NotNull
    private String tipoMovimiento;
    @NotNull
    private BigDecimal valor;
}
