package com.example.Compras.services;

import com.example.Compras.dto.CategoriaRequestDTO;
import com.example.Compras.dto.CategoriaResponseDTO;
import com.example.Compras.entities.Categoria;
import com.example.Compras.repositories.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearCategoria_DeberiaCrearNuevaCategoria() {
        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Frutas");

        when(categoriaRepository.findByNombreIgnoreCase("Frutas"))
                .thenReturn(Optional.empty());

        Categoria categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Frutas");

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaResponseDTO response = categoriaService.crearCategoria(request);

        assertNotNull(response);
        assertEquals("Frutas", response.getNombre());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void crearCategoria_DeberiaLanzarExcepcionSiExiste() {
        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Frutas");

        when(categoriaRepository.findByNombreIgnoreCase("Frutas"))
                .thenReturn(Optional.of(new Categoria()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> categoriaService.crearCategoria(request));

        assertEquals("400 BAD_REQUEST \"La categoría ya existe\"", ex.getMessage());
    }

    @Test
    void listarCategorias_DeberiaRetornarListaDeCategorias() {
        Categoria c1 = new Categoria();
        c1.setId(1L);
        c1.setNombre("Frutas");

        Categoria c2 = new Categoria();
        c2.setId(2L);
        c2.setNombre("Verduras");

        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<CategoriaResponseDTO> categorias = categoriaService.listarCategorias();

        assertEquals(2, categorias.size());
        assertEquals("Verduras", categorias.get(1).getNombre());
    }

    @Test
    void actualizarCategoria_DeberiaActualizarCorrectamente() {
        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Lácteos");

        Categoria existente = new Categoria();
        existente.setId(1L);
        existente.setNombre("Frutas");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.findByNombreIgnoreCase("Lácteos")).thenReturn(Optional.empty());
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(existente);

        CategoriaResponseDTO resultado = categoriaService.actualizarCategoria(1L, request);

        assertNotNull(resultado);
        assertEquals("Lácteos", resultado.getNombre());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void actualizarCategoria_DeberiaLanzar404SiNoExiste() {
        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Lácteos");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> categoriaService.actualizarCategoria(1L, request));

        assertTrue(ex.getMessage().contains("Categoría no encontrada"));
    }

    @Test
    void eliminarCategoria_DeberiaEliminarSiExiste() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        categoriaService.eliminarCategoria(1L);

        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarCategoria_DeberiaLanzar404SiNoExiste() {
        when(categoriaRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> categoriaService.eliminarCategoria(1L));

        assertTrue(ex.getMessage().contains("Categoría no encontrada"));
    }
}

