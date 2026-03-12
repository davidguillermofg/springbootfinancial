package com.example.cliente.service;

import com.example.cliente.domain.Cliente;
import com.example.cliente.dto.ClienteRequest;
import com.example.cliente.exception.ClienteNotFoundException;
import com.example.cliente.messaging.ClienteEventPublisher;
import com.example.cliente.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @Mock
    private ClienteEventPublisher eventPublisher;

    @InjectMocks
    private ClienteService service;

    @Test
    void findById_debeRetornarCliente_cuandoExiste() {
        Cliente cliente = new Cliente("Jose Lema", "M", 30, "123", "Otavalo", "098254785", "1234", true);
        cliente.setClienteId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        var response = service.findById(1L);

        assertThat(response.getClienteId()).isEqualTo(1L);
        assertThat(response.getNombre()).isEqualTo("Jose Lema");
        assertThat(response.getEstado()).isTrue();
    }

    @Test
    void findById_debeLanzarClienteNotFoundException_cuandoNoExiste() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(ClienteNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void create_debeGuardarYPublicarEvento() {
        ClienteRequest request = ClienteRequest.builder()
                .nombre("Jose Lema")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .contrasena("1234")
                .estado(true)
                .build();
        Cliente saved = new Cliente(request.getNombre(), null, null, null, request.getDireccion(),
                request.getTelefono(), request.getContrasena(), request.getEstado());
        saved.setClienteId(1L);
        when(repository.save(any(Cliente.class))).thenReturn(saved);

        var response = service.create(request);

        assertThat(response.getClienteId()).isEqualTo(1L);
        assertThat(response.getNombre()).isEqualTo("Jose Lema");
        verify(eventPublisher).publishClienteCreado(1L, "Jose Lema");
    }
}
