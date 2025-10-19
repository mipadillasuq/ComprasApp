package com.example.Compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UnidadMedidaRequestDTO {
    @NotBlank(message = "El nombre de la unidad de medida es obligatorio.")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres.")
    private String nombre;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
