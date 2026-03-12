package com.example.cuenta.controller;

import com.example.cuenta.dto.CuentaRequest;
import com.example.cuenta.dto.CuentaResponse;
import com.example.cuenta.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService service;

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> create(@Valid @RequestBody CuentaRequest request) {
        CuentaResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/cuentas/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponse> update(@PathVariable Long id, @Valid @RequestBody CuentaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CuentaResponse> patch(@PathVariable Long id, @RequestBody CuentaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
}
