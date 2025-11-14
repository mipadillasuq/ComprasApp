package com.example.Compras.services;

import com.example.Compras.dto.ProveedorRequestDTO;
import com.example.Compras.dto.ProveedorResponseDTO;
import com.example.Compras.dto.TelefonoRequestDTO;
import com.example.Compras.entities.*;
import com.example.Compras.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProveedorServiceTest {

    @Mock private ProveedorRepository proveedorRepository;
    @Mock private TelefonoRepository telefonoRepository;
    @Mock private ProveedorTelefonoRepository proveedorTelefonoRepository;
    @Mock private CompraRepository compraRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 🧩 Crear proveedor
    @Test
    void crearProveedor_DeberiaCrearCorrectamente() {
        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        dto.setNombre("Proveedor Uno");
        dto.setCiudadId(10);
        dto.setDireccion("Calle 123");
        dto.setEmail("test@correo.com");
        dto.setEstado(true);

        when(proveedorRepository.existsById(1L)).thenReturn(false);
        when(proveedorRepository.save(any(Proveedor.class))).thenAnswer(inv -> {
            Proveedor p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });
        when(proveedorTelefonoRepository.findByProveedorId(1L)).thenReturn(Collections.emptyList());

        ProveedorResponseDTO result = proveedorService.crearProveedor(dto);

        assertNotNull(result);
        assertEquals("Proveedor Uno", result.getNombre());
        verify(proveedorRepository).save(any(Proveedor.class));
    }

    @Test
    void crearProveedor_DeberiaLanzarErrorSiExiste() {
        ProveedorRequestDTO dto = new ProveedorRequestDTO();
        when(proveedorRepository.existsById(1L)).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> proveedorService.crearProveedor(dto));
    }

    // 🧩 Obtener proveedor
    @Test
    void getProveedor_DeberiaRetornarProveedor() {
        Proveedor p = new Proveedor();
        p.setId(1L);
        p.setNombre("Proveedor A");

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        when(proveedorTelefonoRepository.findByProveedorId(1L)).thenReturn(Collections.emptyList());

        ProveedorResponseDTO result = proveedorService.getProveedor(1L);

        assertEquals("Proveedor A", result.getNombre());
        verify(proveedorRepository).findById(1L);
    }

    @Test
    void getProveedor_DeberiaLanzarErrorSiNoExiste() {
        when(proveedorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> proveedorService.getProveedor(1L));
    }

    // 🧩 Listar todos
    @Test
    void listarTodos_DeberiaRetornarLista() {
        Proveedor p1 = new Proveedor(); p1.setId(1L); p1.setNombre("P1");
        Proveedor p2 = new Proveedor(); p2.setId(2L); p2.setNombre("P2");

        when(proveedorRepository.findAll()).thenReturn(Arrays.asList(p1, p2));
        when(proveedorTelefonoRepository.findByProveedorId(anyLong())).thenReturn(Collections.emptyList());

        List<ProveedorResponseDTO> result = proveedorService.listarTodos();

        assertEquals(2, result.size());
        verify(proveedorRepository).findAll();
    }

    // 🧩 Eliminar proveedor
    @Test
    void eliminarProveedor_DeberiaEliminarSiNoTieneCompras() {
        Proveedor p = new Proveedor(); p.setId(1L);

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        when(compraRepository.findAll()).thenReturn(Collections.emptyList());
        when(proveedorTelefonoRepository.findByProveedorId(1L)).thenReturn(Collections.emptyList());

        proveedorService.eliminarProveedor(1L);

        verify(proveedorRepository).delete(p);
    }

    @Test
    void eliminarProveedor_DeberiaLanzarErrorSiTieneCompras() {
        Proveedor p = new Proveedor(); p.setId(1L);
        Compra c = new Compra(); c.setProveedor(p);

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        when(compraRepository.findAll()).thenReturn(Collections.singletonList(c));

        assertThrows(ResponseStatusException.class, () -> proveedorService.eliminarProveedor(1L));
    }

    // 🧩 Agregar teléfono
    @Test
    void agregarTelefono_DeberiaAgregarNuevoTelefono() {
        TelefonoRequestDTO telDto = new TelefonoRequestDTO();
        telDto.setNumero("12345");

        Proveedor p = new Proveedor(); p.setId(1L);
        Telefono nuevoTel = new Telefono(); nuevoTel.setId(5L); nuevoTel.setNumero("12345");

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        when(telefonoRepository.findByNumero("12345")).thenReturn(Optional.empty());
        when(telefonoRepository.save(any(Telefono.class))).thenReturn(nuevoTel);
        when(proveedorTelefonoRepository.existsByProveedorIdAndTelefonoId(1L, 5L)).thenReturn(false);
        when(proveedorTelefonoRepository.findByProveedorId(1L)).thenReturn(Collections.emptyList());

        ProveedorResponseDTO result = proveedorService.agregarTelefono(1L, telDto);

        assertNotNull(result);
        verify(proveedorTelefonoRepository).save(any(ProveedorTelefono.class));
    }

    @Test
    void agregarTelefono_DeberiaLanzarErrorSiYaExisteRelacion() {
        TelefonoRequestDTO telDto = new TelefonoRequestDTO();
        telDto.setNumero("12345");

        Proveedor p = new Proveedor(); p.setId(1L);
        Telefono tel = new Telefono(); tel.setId(5L); tel.setNumero("12345");

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        when(telefonoRepository.findByNumero("12345")).thenReturn(Optional.of(tel));
        when(proveedorTelefonoRepository.existsByProveedorIdAndTelefonoId(1L, 5L)).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> proveedorService.agregarTelefono(1L, telDto));
    }

    // 🧩 Quitar teléfono
    @Test
    void quitarTelefono_DeberiaEliminarRelacion() {
        Proveedor p = new Proveedor(); p.setId(1L);
        Telefono t = new Telefono(); t.setId(5L); t.setNumero("12345");

        ProveedorTelefono rel = new ProveedorTelefono();
        rel.setProveedor(p);
        rel.setTelefono(t);

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        when(proveedorTelefonoRepository.findByProveedorId(1L)).thenReturn(Collections.singletonList(rel));
        when(proveedorTelefonoRepository.findAll()).thenReturn(Collections.singletonList(rel));

        proveedorService.quitarTelefono(1L, 5L);

        verify(proveedorTelefonoRepository).deleteAll(anyList());
    }

    @Test
    void quitarTelefono_DeberiaLanzarErrorSiNoTieneTelefono() {
        Proveedor p = new Proveedor(); p.setId(1L);

        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(p));
        when(proveedorTelefonoRepository.findByProveedorId(1L)).thenReturn(Collections.emptyList());

        assertThrows(ResponseStatusException.class, () -> proveedorService.quitarTelefono(1L, 5L));
    }
}

