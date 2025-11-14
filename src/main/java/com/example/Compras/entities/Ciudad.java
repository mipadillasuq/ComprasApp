package com.example.Compras.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ciudades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Long id;  // ✅ cambiado a Long

    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    public Ciudad(String nombre) {
        this.nombre = nombre;
    }
}
