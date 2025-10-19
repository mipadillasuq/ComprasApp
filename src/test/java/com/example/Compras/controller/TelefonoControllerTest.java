package com.example.Compras.controller;

import com.example.Compras.dto.TelefonoRequestDTO;
import com.example.Compras.dto.TelefonoResponseDTO;
import com.example.Compras.services.TelefonoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TelefonoController.class)
@AutoConfigureMockMvc(addFilters = false)
class TelefonoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtAuthFilter jwtAuthFilter;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtUtil jwtUtil;

    @MockBean
    private TelefonoService telefonoService;

    @Autowired
    private ObjectMapper objectMapper;

    private TelefonoRequestDTO telefonoRequest;
    private TelefonoResponseDTO telefonoResponse1;
    private TelefonoResponseDTO telefonoResponse2;

    @BeforeEach
    void setUp() {
        telefonoRequest = new TelefonoRequestDTO();
        telefonoRequest.setNumero("3105559999");

        // ✅ Usamos el constructor que sí existe (Long, String)
        telefonoResponse1 = new TelefonoResponseDTO(1L, "3105559999");
        telefonoResponse2 = new TelefonoResponseDTO(2L, "3206668888");
    }

    @Test
    void crear_DeberiaRetornar200YTelefonoCreado() throws Exception {
        Mockito.when(telefonoService.crearTelefono(Mockito.any(TelefonoRequestDTO.class)))
                .thenReturn(telefonoResponse1);

        mockMvc.perform(post("/telefonos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(telefonoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.numero", is("3105559999")));
    }

    @Test
    void listar_DeberiaRetornar200YListaDeTelefonos() throws Exception {
        List<TelefonoResponseDTO> lista = Arrays.asList(telefonoResponse1, telefonoResponse2);
        Mockito.when(telefonoService.listarTodos()).thenReturn(lista);

        mockMvc.perform(get("/telefonos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].numero", is("3105559999")))
                .andExpect(jsonPath("$[1].numero", is("3206668888")));
    }

    @Test
    void obtenerPorId_DeberiaRetornar200YTelefono() throws Exception {
        Mockito.when(telefonoService.buscarPorId(1L)).thenReturn(telefonoResponse1);

        mockMvc.perform(get("/telefonos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero", is("3105559999")));
    }

    @Test
    void actualizar_DeberiaRetornar200YTelefonoActualizado() throws Exception {
        TelefonoResponseDTO actualizado = new TelefonoResponseDTO(1L, "3107774444");

        Mockito.when(telefonoService.actualizarTelefono(Mockito.eq(1L), Mockito.any(TelefonoRequestDTO.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/telefonos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(telefonoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero", is("3107774444")));
    }

    @Test
    void eliminar_DeberiaRetornar204() throws Exception {
        mockMvc.perform(delete("/telefonos/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(telefonoService).eliminarTelefono(1L);
    }
}
