package com.example.cuenta.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class RabbitConfig {

    @Value("${app.exchange.clientes:clientes.exchange}")
    private String exchangeName;

    @Value("${app.queue.cliente-creado:cuenta.cliente.creado}")
    private String queueName;

    @Value("${app.routing-key.cliente-creado:cliente.creado}")
    private String routingKey;

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange clientesExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue clienteCreadoQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding bindingClienteCreado(Queue clienteCreadoQueue, DirectExchange clientesExchange) {
        return BindingBuilder.bind(clienteCreadoQueue).to(clientesExchange).with(routingKey);
    }
}
