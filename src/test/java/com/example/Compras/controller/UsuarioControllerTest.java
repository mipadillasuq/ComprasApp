package com.example.Compras.controller;

import com.example.Compras.dto.UsuarioRequestDTO;
import com.example.Compras.dto.UsuarioResponseDTO;
import com.example.Compras.services.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtAuthFilter jwtAuthFilter;

    @SuppressWarnings("removal")
    @MockBean
    private com.example.Compras.security.JwtUtil jwtUtil;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private UsuarioRequestDTO requestDTO;
    private UsuarioResponseDTO responseDTO1;
    private UsuarioResponseDTO responseDTO2;

    @BeforeEach
    void setUp() {
        requestDTO = new UsuarioRequestDTO();
        requestDTO.setNombre("Juan Pérez");
        requestDTO.setEmail("juan@example.com");
        requestDTO.setTelefono("3001234567");
        requestDTO.setPassword("secreto123");

        responseDTO1 = new UsuarioResponseDTO(1L, "Juan Pérez", "juan@example.com", "3001234567", (byte) 1);
        responseDTO2 = new UsuarioResponseDTO(2L, "Ana López", "ana@example.com", "3009876543", (byte) 0);
    }

    @Test
    void crearUsuario_DeberiaRetornar200YUsuarioCreado() throws Exception {
        Mockito.when(usuarioService.crearUsuario(Mockito.any(UsuarioRequestDTO.class)))
                .thenReturn(responseDTO1);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.email", is("juan@example.com")))
                .andExpect(jsonPath("$.telefono", is("3001234567")))
                .andExpect(jsonPath("$.estado", is(1)));
    }

    @Test
    void listarTodos_DeberiaRetornar200YListaDeUsuarios() throws Exception {
        List<UsuarioResponseDTO> lista = Arrays.asList(responseDTO1, responseDTO2);
        Mockito.when(usuarioService.listarTodos()).thenReturn(lista);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$[1].nombre", is("Ana López")));
    }

    @Test
    void buscarPorId_DeberiaRetornar200YUsuario() throws Exception {
        Mockito.when(usuarioService.buscarPorId(1L)).thenReturn(responseDTO1);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.email", is("juan@example.com")));
    }

    @Test
    void actualizarUsuario_DeberiaRetornar200YUsuarioActualizado() throws Exception {
        UsuarioResponseDTO actualizado = new UsuarioResponseDTO(1L, "Juan Actualizado", "nuevo@example.com", "3011112233", (byte) 1);

        Mockito.when(usuarioService.actualizarUsuario(Mockito.eq(1L), Mockito.any(UsuarioRequestDTO.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan Actualizado")))
                .andExpect(jsonPath("$.email", is("nuevo@example.com")));
    }

    @Test
    void cambiarEstado_DeberiaRetornar204() throws Exception {
        mockMvc.perform(patch("/usuarios/1/estado")
                        .param("estado", "0"))
                .andExpect(status().isNoContent());

        Mockito.verify(usuarioService).cambiarEstado(1L, (byte) 0);
    }

    @Test
    void eliminarUsuario_DeberiaRetornar204() throws Exception {
        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(usuarioService).eliminarUsuario(1L);
    }
}


