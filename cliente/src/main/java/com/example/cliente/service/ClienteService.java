package com.example.cliente.service;

import com.example.cliente.domain.Cliente;
import com.example.cliente.dto.ClienteRequest;
import com.example.cliente.dto.ClienteResponse;
import com.example.cliente.exception.ClienteNotFoundException;
import com.example.cliente.messaging.ClienteEventPublisher;
import com.example.cliente.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<ClienteResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteResponse findById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional
    public ClienteResponse create(ClienteRequest request) {
        Cliente entity = new Cliente(
                request.getNombre(), request.getGenero(), request.getEdad(),
                request.getIdentificacion(), request.getDireccion(), request.getTelefono(),
                request.getContrasena(), request.getEstado()
        );
        entity = repository.save(entity);
        eventPublisher.publishClienteCreado(entity.getClienteId(), entity.getNombre());
        return toResponse(entity);
    }

    @Transactional
    public ClienteResponse update(Long id, ClienteRequest request) {
        Cliente entity = findEntity(id);
        entity.setNombre(request.getNombre());
        entity.setGenero(request.getGenero());
        entity.setEdad(request.getEdad());
        entity.setIdentificacion(request.getIdentificacion());
        entity.setDireccion(request.getDireccion());
        entity.setTelefono(request.getTelefono());
        entity.setContrasena(request.getContrasena());
        entity.setEstado(request.getEstado());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repository.existsById(id)) throw new ClienteNotFoundException(id);
        repository.deleteById(id);
    }

    private Cliente findEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ClienteNotFoundException(id));
    }

    private ClienteResponse toResponse(Cliente c) {
        return ClienteResponse.builder()
                .clienteId(c.getClienteId())
                .nombre(c.getNombre())
                .genero(c.getGenero())
                .edad(c.getEdad())
                .identificacion(c.getIdentificacion())
                .direccion(c.getDireccion())
                .telefono(c.getTelefono())
                .estado(c.getEstado())
                .build();
    }
}
