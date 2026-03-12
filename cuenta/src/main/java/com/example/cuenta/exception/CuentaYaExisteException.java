package com.example.cuenta.exception;

public class CuentaYaExisteException extends RuntimeException {

    public CuentaYaExisteException(String numeroCuenta) {
        super("La cuenta con número " + numeroCuenta + " ya existe");
    }
}
