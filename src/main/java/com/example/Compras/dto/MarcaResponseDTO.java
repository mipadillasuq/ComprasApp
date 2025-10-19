package com.example.Compras.dto;

public class MarcaResponseDTO {

    private Long id;
    private String nombre;

    public MarcaResponseDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // ====== Getters ======

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
