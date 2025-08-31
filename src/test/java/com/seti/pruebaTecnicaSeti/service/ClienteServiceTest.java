package com.seti.pruebaTecnicaSeti.service;

import com.seti.pruebaTecnicaSeti.config.MapperConfig;
import com.seti.pruebaTecnicaSeti.dto.ClienteRequest;
import com.seti.pruebaTecnicaSeti.dto.ClienteResponse;
import com.seti.pruebaTecnicaSeti.entity.Cliente;
import com.seti.pruebaTecnicaSeti.enums.PreferenciaNotificacion;
import com.seti.pruebaTecnicaSeti.enums.Roles;
import com.seti.pruebaTecnicaSeti.exception.NotFoundException;
import com.seti.pruebaTecnicaSeti.repository.ClienteRepository;
import com.seti.pruebaTecnicaSeti.service.impl.ClienteServiceImpl;
import com.seti.pruebaTecnicaSeti.utils.Constantes;
import com.seti.pruebaTecnicaSeti.utils.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@SpringJUnitConfig({ ClienteServiceImpl.class, Util.class})
@Import({MapperConfig.class})
public class ClienteServiceTest {

    @MockitoBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteServiceImpl clienteService;

    private ClienteRequest clienteRequest;
    private Cliente cliente;
    private ClienteResponse clienteResponse;

    @BeforeEach
    void setUp() {

        // Configurar datos de prueba
        clienteRequest = new ClienteRequest();
        clienteRequest.setNombre("Juan Pérez");
        clienteRequest.setEmail("juan.perez@test.com");
        clienteRequest.setTelefono("3001234567");
        clienteRequest.setPreferenciaNotificacion("EMAIL");


        cliente = new Cliente();
        cliente.setId("1");
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan.perez@test.com");
        cliente.setTelefono("3001234567");
        cliente.setPreferenciaNotificacion(PreferenciaNotificacion.EMAIL);
        cliente.setSaldoDisponible(new BigDecimal("500000"));
        cliente.setRoles(new HashSet<>());

        clienteResponse = new ClienteResponse();
        clienteResponse.setId("1");
        clienteResponse.setNombre("Juan Pérez");
        clienteResponse.setEmail("juan.perez@test.com");
        clienteResponse.setTelefono("3001234567");
        clienteResponse.setPreferenciaNotificacion(PreferenciaNotificacion.EMAIL);
        clienteResponse.setSaldoDisponible(new BigDecimal("500000"));
    }


    @Test
    void crearCliente_DeberiaCrearClienteExitosamente() {

        when(clienteRepository.findByEmail(clienteRequest.getEmail()))
                .thenReturn(Optional.empty());

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);


        // When
        ClienteResponse resultado = clienteService.crearCliente(clienteRequest);

        // Then
        assertNotNull(resultado);
        assertEquals(clienteResponse.getId(), resultado.getId());
        assertEquals(clienteResponse.getNombre(), resultado.getNombre());
        assertEquals(clienteResponse.getEmail(), resultado.getEmail());

        // Verificar que se estableció el saldo inicial
        verify(clienteRepository).save(argThat(clienteGuardado ->
                clienteGuardado.getSaldoDisponible().equals(Constantes.SALDO_INICIAL_CLIENTE)
        ));

        // Verificar que se agregó el rol de cliente
        verify(clienteRepository).save(argThat(clienteGuardado ->
                clienteGuardado.getRoles().contains(Roles.CLIENTE.name())
        ));

        // Verificar interacciones
        verify(clienteRepository).findByEmail(clienteRequest.getEmail());
        verify(clienteRepository).save(any(Cliente.class));

        // Then - Verificar resultado completo
        assertNotNull(resultado);
        assertEquals(clienteResponse.getId(), resultado.getId());
        assertEquals(clienteResponse.getNombre(), resultado.getNombre());
        assertEquals(clienteResponse.getEmail(), resultado.getEmail());
        assertEquals(clienteResponse.getTelefono(), resultado.getTelefono());
        assertEquals(clienteResponse.getPreferenciaNotificacion(), resultado.getPreferenciaNotificacion());
        assertEquals(clienteResponse.getSaldoDisponible(), resultado.getSaldoDisponible());
    }

    @Test
    void crearCliente_DeberiaLanzarExcepcion_CuandoEmailYaExiste() {
        // Given
        Cliente clienteExistente = new Cliente();
        clienteExistente.setEmail(clienteRequest.getEmail());

        when(clienteRepository.findByEmail(clienteRequest.getEmail()))
                .thenReturn(Optional.of(clienteExistente));

        // When & Then
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> clienteService.crearCliente(clienteRequest)
        );

        assertEquals("Ya existe un cliente con este email", exception.getMessage());

        // Verificar que no se intentó guardar
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void crearCliente_DeberiaEstablecerPreferenciaNotificacion() {
        // Given
        when(clienteRepository.findByEmail(clienteRequest.getEmail()))
                .thenReturn(Optional.empty());

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);


        // When
        clienteService.crearCliente(clienteRequest);

        // Then
        verify(clienteRepository).save(argThat(clienteGuardado ->
                clienteGuardado.getPreferenciaNotificacion() == PreferenciaNotificacion.EMAIL
        ));
    }

    @Test
    void crearCliente_DeberiaInicializarRolesYAgregarCliente() {
        // Given
        when(clienteRepository.findByEmail(clienteRequest.getEmail()))
                .thenReturn(Optional.empty());

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);

        // When
        clienteService.crearCliente(clienteRequest);

        // Then
        verify(clienteRepository).save(argThat(clienteGuardado -> {
            assertNotNull(clienteGuardado.getRoles());
            assertTrue(clienteGuardado.getRoles().contains(Roles.CLIENTE.name()));
            return true;
        }));
    }
}
