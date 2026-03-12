package com.example.cuenta.controller;

import com.example.cuenta.dto.ReporteResponse;
import com.example.cuenta.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<ReporteResponse> estadoDeCuenta(
            @RequestParam Long cliente,
            @RequestParam(required = false) String nombreCliente,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {
        ReporteResponse reporte = reporteService.estadoDeCuenta(cliente, nombreCliente, fechaDesde, fechaHasta);
        return ResponseEntity.ok(reporte);
    }
}
