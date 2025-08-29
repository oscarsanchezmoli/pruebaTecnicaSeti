package com.seti.pruebaTecnicaSeti.service;

import com.seti.pruebaTecnicaSeti.dto.SuscripcionRequest;
import com.seti.pruebaTecnicaSeti.dto.TransaccionResponse;

public interface FondoService {

    public TransaccionResponse suscribirFondo(SuscripcionRequest request);
}
