package com.seti.pruebaTecnicaSeti.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BTG Pactual - API de Gestión de Fondos")
                        .version("1.0.0")
                        .description("API REST para la gestión de fondos de inversión de BTG Pactual. " +
                                "Permite a los clientes suscribirse a fondos, cancelar suscripciones, " +
                                "y consultar su historial de transacciones.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo BTG Pactual")
                                .email("desarrollo@btgpactual.com"))
                        .license(new License()
                                .name("Uso Interno")
                                .url("https://www.btgpactual.com")));
    }
}
