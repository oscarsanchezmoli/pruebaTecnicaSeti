package com.seti.pruebaTecnicaSeti.service;

import com.seti.pruebaTecnicaSeti.dto.ClienteRequest;
import com.seti.pruebaTecnicaSeti.dto.ClienteResponse;

public interface ClienteService {
    public ClienteResponse crearCliente(ClienteRequest request);
}
