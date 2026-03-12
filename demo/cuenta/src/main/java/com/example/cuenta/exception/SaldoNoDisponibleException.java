package com.example.cuenta.exception;

public class SaldoNoDisponibleException extends RuntimeException {

    public static final String MENSAJE = "Saldo no disponible";

    public SaldoNoDisponibleException() {
        super(MENSAJE);
    }
}
