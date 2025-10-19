package com.example.Compras.entities;

import jakarta.persistence.*;

    @Entity
    @Table(name = "proveedores_telefonos")
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

        public ProveedorTelefono(Long id, Proveedor proveedor, Telefono telefono) {
            this.id = id;
            this.proveedor = proveedor;
            this.telefono = telefono;
        }

        public ProveedorTelefono() {
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Proveedor getProveedor() { return proveedor; }
        public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

        public Telefono getTelefono() { return telefono; }
        public void setTelefono(Telefono telefono) { this.telefono = telefono; }
    }

