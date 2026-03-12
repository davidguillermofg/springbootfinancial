package com.example.cuenta.service;

import com.example.cuenta.domain.Cuenta;
import com.example.cuenta.domain.Movimiento;
import com.example.cuenta.dto.MovimientoRequest;
import com.example.cuenta.dto.MovimientoResponse;
import com.example.cuenta.exception.MovimientoNotFoundException;
import com.example.cuenta.exception.SaldoNoDisponibleException;
import com.example.cuenta.repository.CuentaRepository;
import com.example.cuenta.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaService cuentaService;
    private final CuentaRepository cuentaRepository;

    @Transactional(readOnly = true)
    public List<MovimientoResponse> findByCuentaId(Long cuentaId) {
        return movimientoRepository.findByCuentaIdOrderByFechaDesc(cuentaId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovimientoResponse findById(Long id) {
        return toResponse(movimientoRepository.findById(id)
                .orElseThrow(() -> new MovimientoNotFoundException(id)));
    }

    @Transactional
    public MovimientoResponse registrar(MovimientoRequest request) {
        Cuenta cuenta = cuentaService.findEntity(request.getCuentaId());
        BigDecimal valor = request.getValor();
        BigDecimal nuevoSaldo = cuenta.getSaldoDisponible().add(valor);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoNoDisponibleException();
        }
        cuenta.setSaldoDisponible(nuevoSaldo);
        cuentaRepository.save(cuenta);
        Movimiento mov = new Movimiento(
                cuenta.getId(),
                LocalDateTime.now(),
                request.getTipoMovimiento(),
                valor,
                nuevoSaldo
        );
        mov = movimientoRepository.save(mov);
        return toResponse(mov);
    }

    @Transactional
    public MovimientoResponse update(Long id, MovimientoRequest request) {
        Movimiento mov = movimientoRepository.findById(id).orElseThrow(() -> new MovimientoNotFoundException(id));
        mov.setTipoMovimiento(request.getTipoMovimiento());
        mov.setValor(request.getValor());
        return toResponse(movimientoRepository.save(mov));
    }

    private MovimientoResponse toResponse(Movimiento m) {
        return MovimientoResponse.builder()
                .id(m.getId())
                .cuentaId(m.getCuentaId())
                .fecha(m.getFecha())
                .tipoMovimiento(m.getTipoMovimiento())
                .valor(m.getValor())
                .saldo(m.getSaldo())
                .build();
    }
}
