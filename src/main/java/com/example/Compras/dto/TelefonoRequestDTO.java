package com.example.Compras.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class TelefonoRequestDTO {
    @NotBlank(message = "El número de teléfono es obligatorio.")
    @Size(max = 15, message = "El número no puede tener más de 15 caracteres.")
    @Pattern(regexp = "^[0-9+\\-()\\s]+$", message = "El número de teléfono contiene caracteres inválidos.")
    private String numero;

    // === Getters & Setters ===
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
}
