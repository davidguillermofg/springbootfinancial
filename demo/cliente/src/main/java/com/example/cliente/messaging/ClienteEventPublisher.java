package com.example.cliente.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public ClienteEventPublisher(
            @org.springframework.beans.factory.annotation.Autowired(required = false) RabbitTemplate rabbitTemplate,
            @Value("${app.exchange.clientes:clientes.exchange}") String exchange,
            @Value("${app.routing-key.cliente-creado:cliente.creado}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publishClienteCreado(Long clienteId, String nombre) {
        if (rabbitTemplate != null) {
            rabbitTemplate.convertAndSend(exchange, routingKey, new ClienteCreadoEvent(clienteId, nombre));
        }
    }
}
