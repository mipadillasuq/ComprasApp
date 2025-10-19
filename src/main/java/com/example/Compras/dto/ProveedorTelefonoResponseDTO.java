package com.example.Compras.dto;

public class ProveedorTelefonoResponseDTO {

    private Long id;
    private Long idProveedor;
    private String nombreProveedor;
    private Long idTelefono;
    private String numeroTelefono;

    public ProveedorTelefonoResponseDTO(Long id, Long idProveedor, String nombreProveedor,
                                        Long idTelefono, String numeroTelefono) {
        this.id = id;
        this.idProveedor = idProveedor;
        this.nombreProveedor = nombreProveedor;
        this.idTelefono = idTelefono;
        this.numeroTelefono = numeroTelefono;
    }

    public Long getId() { return id; }
    public Long getIdProveedor() { return idProveedor; }
    public String getNombreProveedor() { return nombreProveedor; }
    public Long getIdTelefono() { return idTelefono; }
    public String getNumeroTelefono() { return numeroTelefono; }
}
