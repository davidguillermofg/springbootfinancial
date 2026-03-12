package com.example.cliente.messaging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreadoEvent implements Serializable {

    private Long clienteId;
    private String nombre;
}
