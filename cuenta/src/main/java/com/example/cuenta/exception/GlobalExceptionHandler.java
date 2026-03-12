package com.example.cuenta.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SaldoNoDisponibleException.class)
    public ProblemDetail handleSaldoNoDisponible(SaldoNoDisponibleException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, SaldoNoDisponibleException.MENSAJE);
        pd.setType(URI.create("https://api.example.com/problems/saldo-no-disponible"));
        pd.setTitle("Saldo no disponible");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(CuentaYaExisteException.class)
    public ProblemDetail handleCuentaDuplicada(CuentaYaExisteException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        pd.setType(URI.create("https://api.example.com/problems/cuenta-ya-existe"));
        pd.setTitle("Cuenta ya existe");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(CuentaNotFoundException.class)
    public ProblemDetail handleCuentaNotFound(CuentaNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("https://api.example.com/problems/cuenta-not-found"));
        pd.setTitle("Cuenta no encontrada");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(MovimientoNotFoundException.class)
    public ProblemDetail handleMovimientoNotFound(MovimientoNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setType(URI.create("https://api.example.com/problems/movimiento-not-found"));
        pd.setTitle("Movimiento no encontrado");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex) {
        String msg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        String detail = msg != null && msg.contains("cuenta_numero_cuenta_key")
                ? "La cuenta con ese número ya existe."
                : (msg != null ? msg : "Violación de restricción de integridad.");
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, detail);
        pd.setType(URI.create("https://api.example.com/problems/cuenta-ya-existe"));
        pd.setTitle("Cuenta ya existe");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validación fallida");
        pd.setType(URI.create("https://api.example.com/problems/validation-error"));
        pd.setTitle("Error de validación");
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty("violations", errors);
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno");
        pd.setType(URI.create("https://api.example.com/problems/internal-error"));
        pd.setTitle("Error interno del servidor");
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}
