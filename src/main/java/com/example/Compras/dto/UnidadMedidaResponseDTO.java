package com.example.Compras.dto;

public class UnidadMedidaResponseDTO {
    private Long id;
    private String nombre;

    public UnidadMedidaResponseDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
}
