package com.example.Compras.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "proveedores_telefonos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorTelefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedores_telefonos")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_proveedor", referencedColumnName = "id_proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_telefono", referencedColumnName = "id_telefono")
    private Telefono telefono;

}

