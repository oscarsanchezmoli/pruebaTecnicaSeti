package com.seti.pruebaTecnicaSeti.service.impl;

import com.seti.pruebaTecnicaSeti.dto.ClienteRequest;
import com.seti.pruebaTecnicaSeti.dto.ClienteResponse;
import com.seti.pruebaTecnicaSeti.entity.Cliente;
import com.seti.pruebaTecnicaSeti.enums.PreferenciaNotificacion;
import com.seti.pruebaTecnicaSeti.enums.Roles;
import com.seti.pruebaTecnicaSeti.exception.NotFoundException;
import com.seti.pruebaTecnicaSeti.repository.ClienteRepository;
import com.seti.pruebaTecnicaSeti.service.ClienteService;
import com.seti.pruebaTecnicaSeti.utils.Constantes;
import com.seti.pruebaTecnicaSeti.utils.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final Util util;

    @Override
    public ClienteResponse crearCliente(ClienteRequest request) {

        log.info("Creando nuevo cliente: {}", request.getNombre());

        Optional<Cliente> clienteExistente = clienteRepository.findByEmail(request.getEmail());
        if (clienteExistente.isPresent()) {
            throw new NotFoundException("Ya existe un cliente con este email");
        }

        Cliente cliente = util.convertTo(request, Cliente.class);
        cliente.setSaldoDisponible(Constantes.SALDO_INICIAL_CLIENTE);
        cliente.setPreferenciaNotificacion(util.validarEnum(PreferenciaNotificacion.class, request.getPreferenciaNotificacion()));
        Cliente clienteGuardado = clienteRepository.save(cliente);
        cliente.getRoles().add(Roles.CLIENTE.name());

        log.info("Cliente creado exitosamente con ID: {}", clienteGuardado.getId());
        return util.convertTo(clienteGuardado, ClienteResponse.class);
    }
}
