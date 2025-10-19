package com.example.Compras.services;

import com.example.Compras.dto.ProveedorTelefonoRequestDTO;
import com.example.Compras.dto.ProveedorTelefonoResponseDTO;
import com.example.Compras.entities.Proveedor;
import com.example.Compras.entities.ProveedorTelefono;
import com.example.Compras.entities.Telefono;
import com.example.Compras.repositories.ProveedorRepository;
import com.example.Compras.repositories.ProveedorTelefonoRepository;
import com.example.Compras.repositories.TelefonoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProveedorTelefonoServiceTest {

    @Mock
    private ProveedorTelefonoRepository proveedorTelefonoRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private TelefonoRepository telefonoRepository;

    @InjectMocks
    private ProveedorTelefonoService proveedorTelefonoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 🧩 Crear relación
    @Test
    void crearRelacion_DeberiaCrearCorrectamente() {
        ProveedorTelefonoRequestDTO request = new ProveedorTelefonoRequestDTO();
        request.setIdProveedor(1L);
        request.setIdTelefono(5L);

        Proveedor proveedor = new Proveedor();
        proveedor.setId(1L);
        proveedor.setNombre("Proveedor A");

        Telefono telefono = new Telefono();
        telefono.setId(5L);
        telefono.setNumero("12345");

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(telefonoRepository.findById(5L)).thenReturn(Optional.of(telefono));
        when(proveedorTelefonoRepository.save(any(ProveedorTelefono.class)))
                .thenAnswer(inv -> {
                    ProveedorTelefono rel = inv.getArgument(0);
                    rel.setId(10L);
                    return rel;
                });

        ProveedorTelefonoResponseDTO result = proveedorTelefonoService.crearRelacion(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Proveedor A", result.getNombreProveedor());
        assertEquals("12345", result.getNumeroTelefono());
        verify(proveedorTelefonoRepository).save(any(ProveedorTelefono.class));
    }

    @Test
    void crearRelacion_DeberiaLanzarErrorSiProveedorNoExiste() {
        ProveedorTelefonoRequestDTO request = new ProveedorTelefonoRequestDTO();
        request.setIdProveedor(1L);
        request.setIdTelefono(2L);

        when(proveedorRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> proveedorTelefonoService.crearRelacion(request));

        assertTrue(ex.getMessage().contains("Proveedor no encontrado"));
    }

    @Test
    void crearRelacion_DeberiaLanzarErrorSiTelefonoNoExiste() {
        ProveedorTelefonoRequestDTO request = new ProveedorTelefonoRequestDTO();
        request.setIdProveedor(1L);
        request.setIdTelefono(2L);

        Proveedor proveedor = new Proveedor();
        proveedor.setId(1L);

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(telefonoRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> proveedorTelefonoService.crearRelacion(request));

        assertTrue(ex.getMessage().contains("Teléfono no encontrado"));
    }

    // 🧩 Listar por proveedor
    @Test
    void listarPorProveedor_DeberiaRetornarListaCorrectamente() {
        Proveedor p = new Proveedor();
        p.setId(1L);
        p.setNombre("Proveedor A");

        Telefono t1 = new Telefono();
        t1.setId(10L);
        t1.setNumero("1111");

        Telefono t2 = new Telefono();
        t2.setId(20L);
        t2.setNumero("2222");

        ProveedorTelefono r1 = new ProveedorTelefono();
        r1.setId(100L);
        r1.setProveedor(p);
        r1.setTelefono(t1);

        ProveedorTelefono r2 = new ProveedorTelefono();
        r2.setId(200L);
        r2.setProveedor(p);
        r2.setTelefono(t2);

        when(proveedorTelefonoRepository.findByProveedorId(1L))
                .thenReturn(Arrays.asList(r1, r2));

        List<ProveedorTelefonoResponseDTO> result = proveedorTelefonoService.listarPorProveedor(1L);

        assertEquals(2, result.size());
        assertEquals("2222", result.get(1).getNumeroTelefono());
        verify(proveedorTelefonoRepository).findByProveedorId(1L);
    }

    // 🧩 Eliminar relación
    @Test
    void eliminarRelacion_DeberiaEliminarCorrectamente() {
        when(proveedorTelefonoRepository.existsById(10L)).thenReturn(true);

        proveedorTelefonoService.eliminarRelacion(10L);

        verify(proveedorTelefonoRepository).deleteById(10L);
    }

    @Test
    void eliminarRelacion_DeberiaLanzarErrorSiNoExiste() {
        when(proveedorTelefonoRepository.existsById(10L)).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> proveedorTelefonoService.eliminarRelacion(10L));

        assertTrue(ex.getMessage().contains("No existe una relación"));
    }
}
