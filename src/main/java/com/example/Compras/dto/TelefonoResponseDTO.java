package com.example.Compras.dto;

public class TelefonoResponseDTO {
    private Long id;
    private String numero;

    public TelefonoResponseDTO(Long id, String numero) {
        this.id = id;
        this.numero = numero;
    }

    public Long getId() { return id; }
    public String getNumero() { return numero; }

}
