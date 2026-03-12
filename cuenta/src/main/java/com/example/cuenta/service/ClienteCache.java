package com.example.cuenta.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ClienteCache {

    private final Map<Long, String> clienteNombres = new ConcurrentHashMap<>();

    public void put(Long clienteId, String nombre) {
        clienteNombres.put(clienteId, nombre);
    }

    public String getNombre(Long clienteId) {
        return clienteNombres.get(clienteId);
    }
}
