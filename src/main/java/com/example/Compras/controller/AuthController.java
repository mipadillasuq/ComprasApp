package com.example.Compras.controller;

import com.example.Compras.dto.LoginRequestDTO;
import com.example.Compras.entities.Usuario;
import com.example.Compras.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permite peticiones desde Postman o frontend
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndPassword(
                request.getEmail(), request.getPassword()
        );

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            return ResponseEntity.ok("Inicio de sesión exitoso. Bienvenido, " + usuario.getNombre() + "!");
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas.");
        }
    }
}

