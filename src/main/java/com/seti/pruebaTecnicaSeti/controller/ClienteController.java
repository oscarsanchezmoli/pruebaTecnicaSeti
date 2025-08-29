package com.seti.pruebaTecnicaSeti.controller;

import com.seti.pruebaTecnicaSeti.dto.ApiResponse;
import com.seti.pruebaTecnicaSeti.dto.ClienteRequest;
import com.seti.pruebaTecnicaSeti.dto.ClienteResponse;
import com.seti.pruebaTecnicaSeti.entity.Cliente;
import com.seti.pruebaTecnicaSeti.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Fondos", description = "API para gestión de fondos de inversión")
@Slf4j
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Crear nuevo cliente", description = "Crea un nuevo cliente con saldo inicial de $500.000 COP")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<ApiResponse<ClienteResponse>> crearCliente(@Valid @RequestBody ClienteRequest request) {

        log.info("Solicitud de creación de cliente recibida: {}", request.getNombre());

        ClienteResponse cliente = clienteService.crearCliente(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cliente creado exitosamente", cliente));
    }
}
