package com.example.cuenta.controller;

import com.example.cuenta.dto.MovimientoRequest;
import com.example.cuenta.dto.MovimientoResponse;
import com.example.cuenta.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService service;

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> findByCuenta(@RequestParam Long cuentaId) {
        return ResponseEntity.ok(service.findByCuentaId(cuentaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> create(@Valid @RequestBody MovimientoRequest request) {
        MovimientoResponse created = service.registrar(request);
        return ResponseEntity.created(URI.create("/movimientos/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoResponse> update(@PathVariable Long id, @Valid @RequestBody MovimientoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovimientoResponse> patch(@PathVariable Long id, @RequestBody MovimientoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }
}
