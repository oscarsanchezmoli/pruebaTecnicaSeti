package com.seti.pruebaTecnicaSeti.service.impl;

import com.seti.pruebaTecnicaSeti.entity.Cliente;
import com.seti.pruebaTecnicaSeti.entity.Fondo;
import com.seti.pruebaTecnicaSeti.enums.PreferenciaNotificacion;
import com.seti.pruebaTecnicaSeti.service.NotificationService;
import com.seti.pruebaTecnicaSeti.utils.EnviarEmail;
import com.seti.pruebaTecnicaSeti.utils.SmsInfobip;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final EnviarEmail enviarEmail;
    private final SmsInfobip smsInfobip;

    @Override
    public void enviarNotificacionSuscripcion(Cliente cliente, Fondo fondo) {
        log.info("Enviando notificación de suscripción para cliente: {}, fondo: {}",
                cliente.getId(), fondo.getNombre());

        String mensaje = String.format(
                "¡Felicitaciones! Se ha suscrito exitosamente al fondo %s por un monto de $%s COP",
                fondo.getNombre(), fondo.getMontoMinimo()
        );

        try {
            if (cliente.getPreferenciaNotificacion() == PreferenciaNotificacion.EMAIL) {
                enviarEmail.enviarEmail(cliente.getEmail(), "Suscripción a Fondo - BTG Pactual", mensaje);
            } else if (cliente.getPreferenciaNotificacion() == PreferenciaNotificacion.SMS) {
                smsInfobip.enviarSms(cliente.getTelefono(), mensaje);
            }

            log.info("Notificación enviada exitosamente");
        } catch (Exception e) {
            log.error("Error enviando notificación: {}", e.getMessage(), e);
            throw e;
        }
    }
}
