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

    // Crear usuario
    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {
        usuarioRepository.findByEmailIgnoreCase(request.getEmail())
                .ifPresent(u -> { throw new IllegalArgumentException("Ya existe un usuario con ese correo electrónico."); });

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setPassword(request.getPassword());
        usuario.setEstado((byte) 1);

        Usuario guardado = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getEmail(),
                guardado.getTelefono(),
                guardado.getEstado()
        );
    }

    // Listar todos
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(u -> new UsuarioResponseDTO(
                        u.getId(), u.getNombre(), u.getEmail(), u.getTelefono(), u.getEstado()))
                .collect(Collectors.toList());
    }

    // Buscar por ID
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con id " + id));

        return new UsuarioResponseDTO(
                usuario.getId(), usuario.getNombre(), usuario.getEmail(),
                usuario.getTelefono(), usuario.getEstado()
        );
    }

    // Actualizar usuario
    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con id " + id));

        usuarioRepository.findByEmailIgnoreCase(request.getEmail())
                .filter(u -> !u.getId().equals(id))
                .ifPresent(u -> { throw new IllegalArgumentException("Ya existe otro usuario con ese correo."); });

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setTelefono(request.getTelefono());
        usuario.setPassword(request.getPassword());

        Usuario actualizado = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(
                actualizado.getId(), actualizado.getNombre(), actualizado.getEmail(),
                actualizado.getTelefono(), actualizado.getEstado()
        );
    }

    // Cambiar estado (activar / desactivar)
    @Transactional
    public void cambiarEstado(Long id, Byte nuevoEstado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el usuario con id " + id));
        usuario.setEstado(nuevoEstado);
        usuarioRepository.save(usuario);
    }

    // Eliminar usuario
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un usuario con id " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
