package com.example.Compras.controller;

import com.example.Compras.dto.LoginRequestDTO;
import com.example.Compras.dto.LoginResponseDTO;
import com.example.Compras.entities.Usuario;
import com.example.Compras.repositories.UsuarioRepository;
import com.example.Compras.security.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Auth")
public class AuthController {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthController(UsuarioRepository repo, PasswordEncoder encoder, JwtUtil jwt) {
        this.repo = repo; this.encoder = encoder; this.jwt = jwt;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO req) {
        Usuario u = repo.findByEmailIgnoreCase(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (!encoder.matches(req.getPassword(), u.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        String role = "ROLE_" + u.getRol().name();
        String token = jwt.generateToken(u.getEmail(), role);
        return ResponseEntity.ok(new LoginResponseDTO(token, u.getRol().name()));
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario nuevo) {
        if (repo.findByEmailIgnoreCase(nuevo.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // Encriptar la contraseña
        nuevo.setPassword(encoder.encode(nuevo.getPassword()));

        // Activar el usuario por defecto
        nuevo.setEstado((byte)1);

        // Si no se envía rol, asigna EMPLEADO por defecto
        if (nuevo.getRol() == null) {
            nuevo.setRol(nuevo.getRol());
        }

        Usuario guardado = repo.save(nuevo);
        return ResponseEntity.ok(guardado);
    }
}
