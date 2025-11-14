package com.example.Compras.services;

import com.example.Compras.dto.UsuarioRequestDTO;
import com.example.Compras.dto.UsuarioResponseDTO;
import com.example.Compras.entities.Usuario;
import com.example.Compras.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ==========================================================
    // ✅ CREAR USUARIO
    // ==========================================================
    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {
        usuarioRepository.findByEmailIgnoreCase(request.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Ya existe un usuario con ese correo electrónico.");
                });

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setPassword(request.getPassword());
        usuario.setEstado(request.getEstado() != null ? request.getEstado() : (byte) 1);
        usuario.setRol(request.getRol());

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToDTO(guardado);
    }

    // ==========================================================
    // ✅ LISTAR TODOS LOS USUARIOS
    // ==========================================================
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ==========================================================
    // ✅ BUSCAR POR ID
    // ==========================================================
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con id " + id));

        return mapToDTO(usuario);
    }

    // ==========================================================
    // ✅ ACTUALIZAR USUARIO
    // ==========================================================
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con id " + id));

        usuarioRepository.findByEmailIgnoreCase(request.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Ya existe otro usuario con ese correo.");
                });

        // 🔹 Actualizar campos
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(request.getPassword());
        }

        if (request.getEstado() != null) {
            usuario.setEstado(request.getEstado());
        }

        if (request.getRol() != null) {
            usuario.setRol(request.getRol());
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return mapToDTO(actualizado);
    }

    // ==========================================================
    // ✅ CAMBIAR ESTADO (activar / desactivar)
    // ==========================================================
    @Transactional
    public void cambiarEstado(Long id, Byte nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con id " + id));

        usuario.setEstado(nuevoEstado);
        usuarioRepository.save(usuario);
    }

    // ==========================================================
    // ✅ ELIMINAR USUARIO
    // ==========================================================
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un usuario con id " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // ==========================================================
    // 🧩 MÉTODO AUXILIAR PARA MAPEAR ENTIDAD → DTO
    // ==========================================================
    private UsuarioResponseDTO mapToDTO(Usuario u) {
        return new UsuarioResponseDTO(
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getTelefono(),
                u.getEstado(),
                u.getRol() != null ? u.getRol().name() : null
        );
    }
}

