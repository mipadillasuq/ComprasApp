package com.example.Compras.services;

import com.example.Compras.dto.UsuarioRequestDTO;
import com.example.Compras.dto.UsuarioResponseDTO;
import com.example.Compras.entities.Usuario;
import com.example.Compras.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 🧩 Crear usuario
    @Test
    void crearUsuario_DeberiaCrearCorrectamente() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNombre("Juan");
        request.setEmail("juan@test.com");
        request.setTelefono("3214567890");
        request.setPassword("1234");

        when(usuarioRepository.findByEmailIgnoreCase("juan@test.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioResponseDTO result = usuarioService.crearUsuario(request);

        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("juan@test.com", result.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_DeberiaLanzarErrorSiCorreoYaExiste() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setEmail("repetido@test.com");

        when(usuarioRepository.findByEmailIgnoreCase("repetido@test.com"))
                .thenReturn(Optional.of(new Usuario()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.crearUsuario(request));

        assertTrue(ex.getMessage().contains("correo electrónico"));
        verify(usuarioRepository, never()).save(any());
    }

    // 🧩 Listar todos
    @Test
    void listarTodos_DeberiaRetornarListaDeUsuarios() {
        Usuario u1 = new Usuario(); u1.setId(1L); u1.setNombre("Juan");
        Usuario u2 = new Usuario(); u2.setId(2L); u2.setNombre("Pedro");

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u1, u2));

        List<UsuarioResponseDTO> result = usuarioService.listarTodos();

        assertEquals(2, result.size());
        assertEquals("Pedro", result.get(1).getNombre());
        verify(usuarioRepository).findAll();
    }

    // 🧩 Buscar por ID
    @Test
    void buscarPorId_DeberiaRetornarUsuarioSiExiste() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Carlos");
        usuario.setEmail("carlos@test.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioResponseDTO result = usuarioService.buscarPorId(1L);

        assertEquals("Carlos", result.getNombre());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void buscarPorId_DeberiaLanzarErrorSiNoExiste() {
        when(usuarioRepository.findById(5L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.buscarPorId(5L));

        assertTrue(ex.getMessage().contains("No se encontró"));
    }

    // 🧩 Actualizar usuario
    @Test
    void actualizarUsuario_DeberiaActualizarCorrectamente() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNombre("Mario");
        request.setEmail("mario@test.com");
        request.setTelefono("123");
        request.setPassword("abc");

        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNombre("Viejo");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByEmailIgnoreCase("mario@test.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(existente);

        UsuarioResponseDTO result = usuarioService.actualizarUsuario(1L, request);

        assertEquals("Mario", result.getNombre());
        assertEquals("mario@test.com", result.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_DeberiaLanzarErrorSiNoExiste() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setEmail("nuevo@test.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.actualizarUsuario(1L, request));

        assertTrue(ex.getMessage().contains("No se encontró"));
    }

    @Test
    void actualizarUsuario_DeberiaLanzarErrorSiCorreoDuplicado() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setEmail("repetido@test.com");

        Usuario existente = new Usuario();
        existente.setId(1L);

        Usuario otro = new Usuario();
        otro.setId(2L);
        otro.setEmail("repetido@test.com");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.findByEmailIgnoreCase("repetido@test.com")).thenReturn(Optional.of(otro));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.actualizarUsuario(1L, request));

        assertTrue(ex.getMessage().contains("otro usuario"));
    }

    // 🧩 Cambiar estado
    @Test
    void cambiarEstado_DeberiaActualizarEstado() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEstado((byte) 1);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuarioService.cambiarEstado(1L, (byte) 0);

        assertEquals((byte) 0, usuario.getEstado());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void cambiarEstado_DeberiaLanzarErrorSiNoExiste() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.cambiarEstado(10L, (byte) 1));

        assertTrue(ex.getMessage().contains("No se encontró"));
    }

    // 🧩 Eliminar usuario
    @Test
    void eliminarUsuario_DeberiaEliminarCorrectamente() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void eliminarUsuario_DeberiaLanzarErrorSiNoExiste() {
        when(usuarioRepository.existsById(10L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.eliminarUsuario(10L));

        assertTrue(ex.getMessage().contains("No existe un usuario"));
    }
}

