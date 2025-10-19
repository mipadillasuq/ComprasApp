package com.example.Compras.dto;

public class ImpuestoResponseDTO {
    private Long id;
    private String nombre;
    private Double porcentaje;

    public ImpuestoResponseDTO(Long id, String nombre, Double porcentaje) {
        this.id = id;
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }

    // ====== Getters ======

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }
}

