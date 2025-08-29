package com.seti.pruebaTecnicaSeti.service.impl;

import com.seti.pruebaTecnicaSeti.dto.SuscripcionRequest;
import com.seti.pruebaTecnicaSeti.dto.TransaccionResponse;
import com.seti.pruebaTecnicaSeti.entity.Cliente;
import com.seti.pruebaTecnicaSeti.entity.Fondo;
import com.seti.pruebaTecnicaSeti.entity.Transaccion;
import com.seti.pruebaTecnicaSeti.enums.TipoTransaccion;
import com.seti.pruebaTecnicaSeti.exception.NotFoundException;
import com.seti.pruebaTecnicaSeti.repository.ClienteRepository;
import com.seti.pruebaTecnicaSeti.repository.FondoRepository;
import com.seti.pruebaTecnicaSeti.repository.TransaccionRepository;
import com.seti.pruebaTecnicaSeti.service.FondoService;
import com.seti.pruebaTecnicaSeti.utils.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class FondoServiceImpl implements FondoService {

    private final FondoRepository fondoRepository;
    private final ClienteRepository clienteRepository;
    private final TransaccionRepository transaccionRepository;
    private final Util util;

    @Override
    public TransaccionResponse suscribirFondo(SuscripcionRequest request) {

        log.info("Procesando suscripción de fondo - Cliente: {}, Fondo: {}",
                request.getClienteId(), request.getFondoId());


    Cliente cliente = clienteRepository.findById(request.getClienteId())
            .orElseThrow(() -> new NotFoundException("Cliente no encontrado"));

    Fondo fondo = fondoRepository.findById(request.getFondoId())
            .orElseThrow(() -> new NotFoundException("Fondo no encontrado"));

        // Verificar si ya está suscrito
        if (cliente.getFondosSuscritos() != null &&
                cliente.getFondosSuscritos().contains(request.getFondoId())) {
            throw new NotFoundException("El cliente ya está suscrito a este fondo");
        }

        // Verificar saldo suficiente
        if (cliente.getSaldoDisponible().compareTo(fondo.getMontoMinimo()) < 0) {
            throw new NotFoundException(
                    String.format("No tiene saldo disponible para vincularse al fondo %s", fondo.getNombre())
            );
        }

        // Actualizar saldo del cliente
        BigDecimal nuevoSaldo = cliente.getSaldoDisponible().subtract(fondo.getMontoMinimo());
        cliente.setSaldoDisponible(nuevoSaldo);

        // Agregar fondo a la lista de suscritos
        if (cliente.getFondosSuscritos() == null) {
            cliente.setFondosSuscritos(new ArrayList<>());
        }
        cliente.getFondosSuscritos().add(request.getFondoId());

        // Guardar cliente actualizado
        clienteRepository.save(cliente);

        // Crear transacción
        Transaccion transaccionGuardada = transaccionRepository.save(Transaccion
                .builder()
                        .clienteId(cliente.getId())
                        .fondoId(fondo.getId())
                        .monto(fondo.getMontoMinimo())
                        .tipo(TipoTransaccion.APERTURA)
                .build());

        TransaccionResponse transaction = util.convertTo(transaccionGuardada, TransaccionResponse.class);
        transaction.setNombreFondo(fondo.getNombre());

        return transaction;
    }
}
