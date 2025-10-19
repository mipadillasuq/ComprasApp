package com.example.Compras.controller;

import com.example.Compras.dto.CategoriaRequestDTO;
import com.example.Compras.dto.CategoriaResponseDTO;
import com.example.Compras.services.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoriaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoriaController).build();
        objectMapper = new ObjectMapper();
    }

    // 🧩 POST - Crear
    @Test
    void crearCategoria_DeberiaRetornar200YCategoria() throws Exception {
        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Lácteos");

        CategoriaResponseDTO response = new CategoriaResponseDTO();
        response.setId(1L);
        response.setNombre("Lácteos");

        when(categoriaService.crearCategoria(any(CategoriaRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Lácteos"));

        verify(categoriaService).crearCategoria(any(CategoriaRequestDTO.class));
    }

    // 🧩 GET - Listar
    @Test
    void listarCategorias_DeberiaRetornarListaDeCategorias() throws Exception {
        CategoriaResponseDTO c1 = new CategoriaResponseDTO();
        c1.setId(1L);
        c1.setNombre("Lácteos");

        CategoriaResponseDTO c2 = new CategoriaResponseDTO();
        c2.setId(2L);
        c2.setNombre("Carnes");

        List<CategoriaResponseDTO> lista = Arrays.asList(c1, c2);

        when(categoriaService.listarCategorias()).thenReturn(lista);

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].nombre").value("Carnes"));

        verify(categoriaService).listarCategorias();
    }

    // 🧩 PUT - Actualizar
    @Test
    void actualizarCategoria_DeberiaRetornar200YCategoriaActualizada() throws Exception {
        CategoriaRequestDTO request = new CategoriaRequestDTO();
        request.setNombre("Frutas");

        CategoriaResponseDTO response = new CategoriaResponseDTO();
        response.setId(1L);
        response.setNombre("Frutas");

        when(categoriaService.actualizarCategoria(eq(1L), any(CategoriaRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Frutas"));

        verify(categoriaService).actualizarCategoria(eq(1L), any(CategoriaRequestDTO.class));
    }

    // 🧩 DELETE - Eliminar
    @Test
    void eliminarCategoria_DeberiaRetornar204() throws Exception {
        doNothing().when(categoriaService).eliminarCategoria(1L);

        mockMvc.perform(delete("/categorias/1"))
                .andExpect(status().isNoContent());

        verify(categoriaService).eliminarCategoria(1L);
    }
}

