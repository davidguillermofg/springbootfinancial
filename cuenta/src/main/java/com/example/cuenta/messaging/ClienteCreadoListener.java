package com.example.cuenta.messaging;

import com.example.cuenta.service.ClienteCache;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class ClienteCreadoListener {

    private final ClienteCache clienteCache;

    @RabbitListener(queues = "${app.queue.cliente-creado:cuenta.cliente.creado}")
    public void onClienteCreado(ClienteCreadoEvent event) {
        clienteCache.put(event.getClienteId(), event.getNombre());
    }
}
