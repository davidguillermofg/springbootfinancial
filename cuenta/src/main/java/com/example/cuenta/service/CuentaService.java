package com.example.cuenta.service;

import com.example.cuenta.domain.Cuenta;
import com.example.cuenta.dto.CuentaRequest;
import com.example.cuenta.dto.CuentaResponse;
import com.example.cuenta.exception.CuentaNotFoundException;
import com.example.cuenta.exception.CuentaYaExisteException;
import com.example.cuenta.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository repository;

    @Transactional(readOnly = true)
    public List<CuentaResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CuentaResponse findById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional(readOnly = true)
    public CuentaResponse findByNumeroCuenta(String numeroCuenta) {
        return toResponse(repository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNotFoundException(numeroCuenta)));
    }

    @Transactional
    public CuentaResponse create(CuentaRequest request) {
        if (repository.findByNumeroCuenta(request.getNumeroCuenta()).isPresent()) {
            throw new CuentaYaExisteException(request.getNumeroCuenta());
        }
        Cuenta entity = new Cuenta(
                request.getNumeroCuenta(),
                request.getTipoCuenta(),
                request.getSaldoInicial(),
                request.getEstado(),
                request.getClienteId()
        );
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CuentaResponse update(Long id, CuentaRequest request) {
        Cuenta entity = findEntity(id);
        entity.setNumeroCuenta(request.getNumeroCuenta());
        entity.setTipoCuenta(request.getTipoCuenta());
        entity.setSaldoInicial(request.getSaldoInicial());
        entity.setEstado(request.getEstado());
        return toResponse(repository.save(entity));
    }

    Cuenta findEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new CuentaNotFoundException(id));
    }

    Cuenta findEntityByNumero(String numeroCuenta) {
        return repository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNotFoundException(numeroCuenta));
    }

    private CuentaResponse toResponse(Cuenta c) {
        return CuentaResponse.builder()
                .id(c.getId())
                .numeroCuenta(c.getNumeroCuenta())
                .tipoCuenta(c.getTipoCuenta())
                .saldoInicial(c.getSaldoInicial())
                .saldoDisponible(c.getSaldoDisponible())
                .estado(c.getEstado())
                .clienteId(c.getClienteId())
                .build();
    }
}
