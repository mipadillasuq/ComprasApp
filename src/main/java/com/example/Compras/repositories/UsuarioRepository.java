package com.example.Compras.repositories;

import com.example.Compras.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailAndPassword(String email, String password);

    Optional<Usuario> findByEmailIgnoreCase(String email);
}




