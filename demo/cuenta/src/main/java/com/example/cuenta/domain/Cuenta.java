package com.example.cuenta.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cuenta")
@Getter
@Setter
@NoArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_cuenta", unique = true, nullable = false)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta", nullable = false)
    private String tipoCuenta;

    @Column(name = "saldo_inicial", nullable = false)
    private java.math.BigDecimal saldoInicial;

    @Column(name = "saldo_disponible", nullable = false)
    private java.math.BigDecimal saldoDisponible;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    public Cuenta(String numeroCuenta, String tipoCuenta, java.math.BigDecimal saldoInicial, Boolean estado, Long clienteId) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial != null ? saldoInicial : java.math.BigDecimal.ZERO;
        this.saldoDisponible = this.saldoInicial;
        this.estado = estado != null ? estado : true;
        this.clienteId = clienteId;
    }
}
