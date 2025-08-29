package com.seti.pruebaTecnicaSeti.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seti.pruebaTecnicaSeti.enums.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionResponse {
    private String id;
    private String clienteId;
    private String fondoId;
    private String nombreFondo;
    private TipoTransaccion tipo;
    private BigDecimal monto;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime fechaTransaccion;
}
