package com.example.Compras.services;

import com.example.Compras.dto.TelefonoRequestDTO;
import com.example.Compras.dto.TelefonoResponseDTO;
import com.example.Compras.entities.Telefono;
import com.example.Compras.repositories.TelefonoRepository;
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

class TelefonoServiceTest {

    @Mock
    private TelefonoRepository telefonoRepository;

    @InjectMocks
    private TelefonoService telefonoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 🧩 Crear teléfono
    @Test
    void crearTelefono_DeberiaCrearCorrectamente() {
        TelefonoRequestDTO request = new TelefonoRequestDTO();
        request.setNumero("3214567890");

        when(telefonoRepository.findByNumero("3214567890")).thenReturn(Optional.empty());
        when(telefonoRepository.save(any(Telefono.class))).thenAnswer(inv -> {
            Telefono t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        TelefonoResponseDTO result = telefonoService.crearTelefono(request);

        assertNotNull(result);
        assertEquals("3214567890", result.getNumero());
        verify(telefonoRepository).save(any(Telefono.class));
    }

    @Test
    void crearTelefono_DeberiaLanzarErrorSiNumeroYaExiste() {
        TelefonoRequestDTO request = new TelefonoRequestDTO();
        request.setNumero("3001234567");

        when(telefonoRepository.findByNumero("3001234567"))
                .thenReturn(Optional.of(new Telefono()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> telefonoService.crearTelefono(request));

        assertTrue(ex.getMessage().contains("Ya existe un teléfono"));
        verify(telefonoRepository, never()).save(any());
    }

    // 🧩 Listar todos
    @Test
    void listarTodos_DeberiaRetornarListaDeTelefonos() {
        Telefono t1 = new Telefono(); t1.setId(1L); t1.setNumero("1111");
        Telefono t2 = new Telefono(); t2.setId(2L); t2.setNumero("2222");

        when(telefonoRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<TelefonoResponseDTO> result = telefonoService.listarTodos();

        assertEquals(2, result.size());
        assertEquals("2222", result.get(1).getNumero());
        verify(telefonoRepository).findAll();
    }

    // 🧩 Buscar por ID
    @Test
    void buscarPorId_DeberiaRetornarTelefonoSiExiste() {
        Telefono t = new Telefono(); t.setId(1L); t.setNumero("5555");
        when(telefonoRepository.findById(1L)).thenReturn(Optional.of(t));

        TelefonoResponseDTO result = telefonoService.buscarPorId(1L);

        assertEquals("5555", result.getNumero());
        verify(telefonoRepository).findById(1L);
    }

    @Test
    void buscarPorId_DeberiaLanzarErrorSiNoExiste() {
        when(telefonoRepository.findById(10L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> telefonoService.buscarPorId(10L));

        assertTrue(ex.getMessage().contains("No se encontró"));
    }

    // 🧩 Actualizar teléfono
    @Test
    void actualizarTelefono_DeberiaActualizarCorrectamente() {
        TelefonoRequestDTO request = new TelefonoRequestDTO();
        request.setNumero("987654321");

        Telefono existente = new Telefono();
        existente.setId(1L);
        existente.setNumero("123456789");

        when(telefonoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(telefonoRepository.findByNumero("987654321")).thenReturn(Optional.empty());
        when(telefonoRepository.save(any(Telefono.class))).thenReturn(existente);

        TelefonoResponseDTO result = telefonoService.actualizarTelefono(1L, request);

        assertEquals("987654321", result.getNumero());
        verify(telefonoRepository).save(any(Telefono.class));
    }

    @Test
    void actualizarTelefono_DeberiaLanzarErrorSiNoExiste() {
        TelefonoRequestDTO request = new TelefonoRequestDTO();
        request.setNumero("987654321");

        when(telefonoRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> telefonoService.actualizarTelefono(1L, request));

        assertTrue(ex.getMessage().contains("No se encontró"));
    }

    @Test
    void actualizarTelefono_DeberiaLanzarErrorSiNumeroDuplicado() {
        TelefonoRequestDTO request = new TelefonoRequestDTO();
        request.setNumero("5555");

        Telefono existente = new Telefono();
        existente.setId(1L);

        Telefono otro = new Telefono();
        otro.setId(2L);
        otro.setNumero("5555");

        when(telefonoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(telefonoRepository.findByNumero("5555")).thenReturn(Optional.of(otro));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> telefonoService.actualizarTelefono(1L, request));

        assertTrue(ex.getMessage().contains("Ya existe otro teléfono"));
    }

    // 🧩 Eliminar teléfono
    @Test
    void eliminarTelefono_DeberiaEliminarCorrectamente() {
        when(telefonoRepository.existsById(1L)).thenReturn(true);

        telefonoService.eliminarTelefono(1L);

        verify(telefonoRepository).deleteById(1L);
    }

    @Test
    void eliminarTelefono_DeberiaLanzarErrorSiNoExiste() {
        when(telefonoRepository.existsById(10L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> telefonoService.eliminarTelefono(10L));

        assertTrue(ex.getMessage().contains("No existe un teléfono"));
    }
}

