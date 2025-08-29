package com.seti.pruebaTecnicaSeti.dto;

import com.seti.pruebaTecnicaSeti.enums.PreferenciaNotificacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponse {
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private BigDecimal saldoDisponible;
    private PreferenciaNotificacion preferenciaNotificacion;
    private List<String> fondosSuscritos = new ArrayList<>();
}
