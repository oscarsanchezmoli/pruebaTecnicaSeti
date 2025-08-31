package com.seti.pruebaTecnicaSeti.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seti.pruebaTecnicaSeti.entity.Cliente;
import com.seti.pruebaTecnicaSeti.enums.PreferenciaNotificacion;
import com.seti.pruebaTecnicaSeti.enums.Roles;
import com.seti.pruebaTecnicaSeti.exception.NotFoundException;
import com.seti.pruebaTecnicaSeti.repository.ClienteRepository;
import com.seti.pruebaTecnicaSeti.service.NotificationService;
import com.seti.pruebaTecnicaSeti.utils.Constantes;
import com.seti.pruebaTecnicaSeti.utils.SmsInfobip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class ClienteControllerTest {

    @MockitoBean
    private JavaMailSender mailSender;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private SmsInfobip smsInfobip;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ClienteRepository clienteRepository;

    private MockMvc mockMvc;
    private String baseUrl = "/api/clientes";

    @BeforeEach
    public void setUp() {
        mockMvc  = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void crearCliente_DeberiaLanzarExcepcionSiNotificacionNoExiste() throws Exception {

        clienteRepository.deleteAll();

        String expectedResponse = "{\"success\":false,\"message\":\"Valor inválido. Valores válidos: [EMAIL, SMS]\",\"data\":null}";

        String requestBody = "{\n" +
                "    \"nombre\": \"Oscar Sanchez\",\n" +
                "    \"email\": \"hola@gmail.com\",\n" +
                "    \"telefono\": \"s1234\",\n" +
                "    \"preferenciaNotificacion\": \"WhatsApp\"\n" +
                "}";


        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl +"/" +
                                "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((MockMvcResultMatchers.content().string(containsString(expectedResponse))));

    }

    @Test
    void crearCliente_DeberiaValidarRespuestaExacta() throws Exception {
        clienteRepository.deleteAll();
        String requestBody = "{\n" +
                "    \"nombre\": \"Oscar Sanchez\",\n" +
                "    \"email\": \"hola@gmail.com\",\n" +
                "    \"telefono\": \"s1234\",\n" +
                "    \"preferenciaNotificacion\": \"Email\"\n" +
                "}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        System.out.println("Respuesta: " + responseContent);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseContent);

        assertTrue(json.get("success").asBoolean());
        assertEquals("Cliente creado exitosamente", json.get("message").asText());
        assertEquals(3, json.size(), "El JSON raíz tiene campos extra");

        JsonNode data = json.get("data");
        assertNotNull(data);

        String id = data.get("id").asText();
        assertThat("El id no tiene formato de ObjectId", id, matchesPattern("^[a-f0-9]{24}$"));

        assertEquals("Oscar Sanchez", data.get("nombre").asText());
        assertEquals("hola@gmail.com", data.get("email").asText());
        assertEquals("s1234", data.get("telefono").asText());
        assertEquals(500000, data.get("saldoDisponible").asDouble());
        assertEquals("EMAIL", data.get("preferenciaNotificacion").asText());

        assertTrue(data.get("fondosSuscritos").isArray());
        assertEquals(0, data.get("fondosSuscritos").size());
        assertEquals(7, data.size(), "El objeto data tiene campos extra");
    }

    @Test
    void obtenerHistorialTransacciones_DeberiaLanzarExcepcionSiClienteNoExiste() throws Exception {
        clienteRepository.deleteAll();

        String expectedResponse = "{\"success\":false,\"message\":\"Cliente no encontrado\",\"data\":null}";

        String requestBody = "{\n" +
                "    \"nombre\": \"Oscar Sanchez\",\n" +
                "    \"email\": \"hola@gmail.com\",\n" +
                "    \"telefono\": \"s1234\",\n" +
                "    \"preferenciaNotificacion\": \"WhatsApp\"\n" +
                "}";


        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl +"/1/transacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((MockMvcResultMatchers.content().string(containsString(expectedResponse))));

    }

    @Test
    void obtenerHistorialTransacciones_DeberiaLanzarTransaccionesVacias() throws Exception {
        clienteRepository.deleteAll();

        clienteRepository.save(Cliente
                .builder()
                        .id("1")
                .email("hola@gmail.com")
                .roles(Set.of(Roles.CLIENTE.name()))
                .nombre("Oscar")
                .telefono("1234")
                .saldoDisponible(Constantes.SALDO_INICIAL_CLIENTE)
                .fondosSuscritos(new ArrayList<>())
                .build());

        String expectedResponse = "{\"success\":true,\"message\":\"Historial obtenido exitosamente\",\"data\":[]}";

        String requestBody = "{\n" +
                "    \"nombre\": \"Oscar Sanchez\",\n" +
                "    \"email\": \"hola@gmail.com\",\n" +
                "    \"telefono\": \"s1234\",\n" +
                "    \"preferenciaNotificacion\": \"WhatsApp\"\n" +
                "}";


        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl +"/1/transacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((MockMvcResultMatchers.content().string(containsString(expectedResponse))));

    }
}
