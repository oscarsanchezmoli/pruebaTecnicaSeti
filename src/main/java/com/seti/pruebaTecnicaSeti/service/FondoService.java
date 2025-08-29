package com.seti.pruebaTecnicaSeti.service;

import com.seti.pruebaTecnicaSeti.dto.FondoSuscripcionCancelacionRequest;
import com.seti.pruebaTecnicaSeti.dto.TransaccionResponse;

public interface FondoService {

    TransaccionResponse suscribirFondo(FondoSuscripcionCancelacionRequest request);
    TransaccionResponse cancelarSuscripcion(FondoSuscripcionCancelacionRequest request);
}
