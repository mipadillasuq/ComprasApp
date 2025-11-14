package com.example.Compras.dto;

public class ProductoRequestDTO {
    private String nombre;
    private Double precio;
    private Integer stock;
    private Boolean estado;
    private Integer cantidadUnidadesMedida;
    private Long categoriaId;
    private Long marcaId;
    private Long unidadMedidaId;
    private Long impuestoId;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getCantidadUnidadesMedida() {
        return cantidadUnidadesMedida;
    }

    public void setCantidadUnidadesMedida(Integer cantidadUnidadesMedida) {
        this.cantidadUnidadesMedida = cantidadUnidadesMedida;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Long getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(Long marcaId) {
        this.marcaId = marcaId;
    }

    public Long getUnidadMedidaId() {
        return unidadMedidaId;
    }

    public void setUnidadMedidaId(Long unidadMedidaId) {
        this.unidadMedidaId = unidadMedidaId;
    }

    public Long getImpuestoId() {
        return impuestoId;
    }

    public void setImpuestoId(Long impuestoId) {
        this.impuestoId = impuestoId;
    }
}
