package com.seti.pruebaTecnicaSeti.service;

import com.seti.pruebaTecnicaSeti.entity.Cliente;
import com.seti.pruebaTecnicaSeti.entity.Fondo;

public interface NotificationService {
    void enviarNotificacionSuscripcion(Cliente cliente, Fondo fondo);
}
