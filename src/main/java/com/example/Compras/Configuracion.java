package com.example.Compras;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Compras",
                version = "1.0",
                description = "Documentación de la API para gestionar proveedores, productos y compras"
        )
)
public class Configuracion {



}
