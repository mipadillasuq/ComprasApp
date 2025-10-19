package com.example.Compras.controller;

import com.example.Compras.dto.CiudadRequestDTO;
import com.example.Compras.dto.CiudadResponseDTO;
import com.example.Compras.services.CiudadService;
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

class CiudadControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CiudadService ciudadService;

    @InjectMocks
    private CiudadController ciudadController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ciudadController).build();
        objectMapper = new ObjectMapper();
    }

    // 🧩 POST - Crear ciudad
    @Test
    void crearCiudad_DeberiaRetornar200YCiudad() throws Exception {
        CiudadRequestDTO request = new CiudadRequestDTO();
        request.setNombre("Bogotá");

        CiudadResponseDTO response = new CiudadResponseDTO();
        response.setId(1);
        response.setNombre("Bogotá");

        when(ciudadService.crearCiudad(any(CiudadRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/ciudades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Bogotá"));

        verify(ciudadService).crearCiudad(any(CiudadRequestDTO.class));
    }

    // 🧩 GET - Listar ciudades
    @Test
    void listarCiudades_DeberiaRetornarListaDeCiudades() throws Exception {
        CiudadResponseDTO c1 = new CiudadResponseDTO();
        c1.setId(1);
        c1.setNombre("Bogotá");

        CiudadResponseDTO c2 = new CiudadResponseDTO();
        c2.setId(2);
        c2.setNombre("Medellín");

        List<CiudadResponseDTO> lista = Arrays.asList(c1, c2);

        when(ciudadService.listarCiudades()).thenReturn(lista);

        mockMvc.perform(get("/ciudades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].nombre").value("Medellín"));

        verify(ciudadService).listarCiudades();
    }

    // 🧩 GET - Obtener por ID
    @Test
    void obtenerCiudad_DeberiaRetornarCiudadPorId() throws Exception {
        CiudadResponseDTO response = new CiudadResponseDTO();
        response.setId(1);
        response.setNombre("Bogotá");

        when(ciudadService.obtenerCiudad(1)).thenReturn(response);

        mockMvc.perform(get("/ciudades/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Bogotá"));

        verify(ciudadService).obtenerCiudad(1);
    }

    // 🧩 PUT - Actualizar ciudad
    @Test
    void actualizarCiudad_DeberiaRetornar200YCiudadActualizada() throws Exception {
        CiudadRequestDTO request = new CiudadRequestDTO();
        request.setNombre("Cali");

        CiudadResponseDTO response = new CiudadResponseDTO();
        response.setId(1);
        response.setNombre("Cali");

        when(ciudadService.actualizarCiudad(eq(1), any(CiudadRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/ciudades/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Cali"));

        verify(ciudadService).actualizarCiudad(eq(1), any(CiudadRequestDTO.class));
    }

    // 🧩 DELETE - Eliminar ciudad
    @Test
    void eliminarCiudad_DeberiaRetornar204() throws Exception {
        doNothing().when(ciudadService).eliminarCiudad(1);

        mockMvc.perform(delete("/ciudades/1"))
                .andExpect(status().isNoContent());

        verify(ciudadService).eliminarCiudad(1);
    }
}

