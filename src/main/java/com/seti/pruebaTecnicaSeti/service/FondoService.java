package com.seti.pruebaTecnicaSeti.service;

import com.seti.pruebaTecnicaSeti.dto.FondoSuscripcionCancelacionRequest;
import com.seti.pruebaTecnicaSeti.dto.TransaccionResponse;

import java.util.List;

public interface FondoService {

    TransaccionResponse suscribirFondo(FondoSuscripcionCancelacionRequest request);
    TransaccionResponse cancelarSuscripcion(FondoSuscripcionCancelacionRequest request);
    List<TransaccionResponse> obtenerHistorialTransacciones(String clienteId);
}
