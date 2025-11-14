package com.example.Compras.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "impuestos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Impuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_impuesto")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    // Se almacena como 0.19, 0.05, etc.
    @Column(name = "porcentaje", nullable = false)
    private Double porcentaje;

}
