package com.example.Compras.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ImpuestoRequestDTO {
    @NotBlank(message = "El nombre del impuesto es obligatorio.")
    private String nombre;

    @NotNull(message = "El porcentaje del impuesto es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El porcentaje debe ser mayor a 0.")
    @DecimalMax(value = "100", inclusive = true, message = "El porcentaje debe ser menor o igual a 100")
    private Double porcentaje;

    // ====== Getters y Setters ======

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }
}

