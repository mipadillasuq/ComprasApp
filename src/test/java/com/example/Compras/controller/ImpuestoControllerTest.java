package com.example.Compras.controller;

import com.example.Compras.dto.ImpuestoRequestDTO;
import com.example.Compras.dto.ImpuestoResponseDTO;
import com.example.Compras.services.ImpuestoService;
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

class ImpuestoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImpuestoService impuestoService;

    @InjectMocks
    private ImpuestoController impuestoController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(impuestoController).build();
        objectMapper = new ObjectMapper();
    }

    // 🧩 POST - Crear un impuesto
    @Test
    void crearImpuesto_DeberiaRetornar200YImpuestoCreado() throws Exception {
        ImpuestoRequestDTO request = new ImpuestoRequestDTO();
        request.setNombre("IVA");
        request.setPorcentaje(19.0);

        ImpuestoResponseDTO response = new ImpuestoResponseDTO(1L, "IVA", 19.0);

        when(impuestoService.crearImpuesto(any(ImpuestoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/impuestos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("IVA"))
                .andExpect(jsonPath("$.porcentaje").value(19.0));

        verify(impuestoService).crearImpuesto(any(ImpuestoRequestDTO.class));
    }

    // 🧩 PUT - Actualizar un impuesto
    @Test
    void actualizarImpuesto_DeberiaRetornar200YImpuestoActualizado() throws Exception {
        ImpuestoRequestDTO request = new ImpuestoRequestDTO();
        request.setNombre("Retefuente");
        request.setPorcentaje(5.0);

        ImpuestoResponseDTO response = new ImpuestoResponseDTO(2L, "Retefuente", 5.0);

        when(impuestoService.actualizarImpuesto(eq(2L), any(ImpuestoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/impuestos/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nombre").value("Retefuente"))
                .andExpect(jsonPath("$.porcentaje").value(5.0));

        verify(impuestoService).actualizarImpuesto(eq(2L), any(ImpuestoRequestDTO.class));
    }

    // 🧩 DELETE - Eliminar impuesto
    @Test
    void eliminarImpuesto_DeberiaRetornar204() throws Exception {
        doNothing().when(impuestoService).eliminarImpuesto(1L);

        mockMvc.perform(delete("/impuestos/1"))
                .andExpect(status().isNoContent());

        verify(impuestoService).eliminarImpuesto(1L);
    }

    // 🧩 GET - Listar todos los impuestos
    @Test
    void listarImpuestos_DeberiaRetornarListaDeImpuestos() throws Exception {
        ImpuestoResponseDTO i1 = new ImpuestoResponseDTO(1L, "IVA", 19.0);
        ImpuestoResponseDTO i2 = new ImpuestoResponseDTO(2L, "Retefuente", 5.0);

        List<ImpuestoResponseDTO> lista = Arrays.asList(i1, i2);

        when(impuestoService.listarImpuestos()).thenReturn(lista);

        mockMvc.perform(get("/impuestos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre").value("IVA"))
                .andExpect(jsonPath("$[1].porcentaje").value(5.0));

        verify(impuestoService).listarImpuestos();
    }

    // 🧩 GET - Obtener impuesto por ID
    @Test
    void obtenerPorId_DeberiaRetornarImpuestoPorId() throws Exception {
        ImpuestoResponseDTO response = new ImpuestoResponseDTO(1L, "IVA", 19.0);
        when(impuestoService.obtenerImpuestoPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/impuestos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("IVA"))
                .andExpect(jsonPath("$.porcentaje").value(19.0));

        verify(impuestoService).obtenerImpuestoPorId(1L);
    }
}


