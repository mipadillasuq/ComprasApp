package com.example.Compras.services;

import com.example.Compras.dto.*;
import com.example.Compras.entities.*;
import com.example.Compras.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompraServiceTest {

    @Mock
    private CompraRepository compraRepository;
    @Mock
    private ProveedorRepository proveedorRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private DetalleCompraRepository detalleCompraRepository;

    @InjectMocks
    private CompraService compraService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearCompra_DeberiaCrearCompraYDetallesCorrectamente() {
        CompraRequestDTO request = new CompraRequestDTO();
        request.setNumFactura("F001");
        request.setFecha(LocalDate.now());
        request.setIdProveedor(1L);
        request.setIdUsuario(1L);

        DetalleCompraRequestDTO detalleDTO = new DetalleCompraRequestDTO();
        detalleDTO.setIdProducto(10L);
        detalleDTO.setCantidad(5);
        request.setDetalles(List.of(detalleDTO));

        Proveedor proveedor = new Proveedor();
        proveedor.setId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Producto producto = new Producto();
        producto.setId(10L);

        Compra compraGuardada = new Compra();
        compraGuardada.setId(100L);
        compraGuardada.setNumFactura("F001");
        compraGuardada.setProveedor(proveedor);
        compraGuardada.setUsuario(usuario);

        when(compraRepository.findByNumFactura("F001")).thenReturn(Optional.empty());
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(productoRepository.findById(10L)).thenReturn(Optional.of(producto));
        when(compraRepository.save(any(Compra.class))).thenReturn(compraGuardada);
        when(detalleCompraRepository.findByCompraId(100L)).thenReturn(List.of());

        CompraResponseDTO result = compraService.crearCompra(request);

        assertNotNull(result);
        assertEquals("F001", result.getNumFactura());
        verify(compraRepository, times(1)).save(any(Compra.class));
        verify(detalleCompraRepository, atLeastOnce()).save(any(DetalleCompra.class));
    }

    @Test
    void crearCompra_DeberiaLanzarErrorSiFacturaYaExiste() {
        CompraRequestDTO request = new CompraRequestDTO();
        request.setNumFactura("F002");

        when(compraRepository.findByNumFactura("F002")).thenReturn(Optional.of(new Compra()));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> compraService.crearCompra(request));

        assertTrue(exception.getMessage().contains("Ya existe una compra"));
    }

    @Test
    void getCompraById_DeberiaRetornarCompraConDetalles() {
        Compra compra = new Compra();
        compra.setId(10L);
        compra.setNumFactura("F100");

        DetalleCompra detalle = new DetalleCompra();
        detalle.setIdDetalleCompra(1L);
        detalle.setCantidad(3);
        detalle.setCompra(compra);
        detalle.setProducto(new Producto());

        when(compraRepository.findById(10L)).thenReturn(Optional.of(compra));
        when(detalleCompraRepository.findByCompraId(10L)).thenReturn(List.of(detalle));

        CompraResponseDTO dto = compraService.getCompraById(10L);

        assertEquals("F100", dto.getNumFactura());
        assertNotNull(dto.getDetalles());
        verify(compraRepository).findById(10L);
        verify(detalleCompraRepository).findByCompraId(10L);
    }

    @Test
    void getCompraById_DeberiaLanzarErrorSiNoExiste() {
        when(compraRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> compraService.getCompraById(99L));

        assertTrue(ex.getMessage().contains("Compra no encontrada"));
    }

    @Test
    void getAllCompras_DeberiaRetornarListaConDetalles() {
        Compra c1 = new Compra();
        c1.setId(1L);
        c1.setNumFactura("A1");
        Compra c2 = new Compra();
        c2.setId(2L);
        c2.setNumFactura("B2");

        when(compraRepository.findAll()).thenReturn(List.of(c1, c2));
        when(detalleCompraRepository.findByCompraId(anyLong())).thenReturn(List.of());

        List<CompraResponseDTO> result = compraService.getAllCompras();

        assertEquals(2, result.size());
        verify(compraRepository).findAll();
    }

    @Test
    void eliminarCompra_DeberiaEliminarCompraYDetalles() {
        Compra compra = new Compra();
        compra.setId(1L);

        DetalleCompra detalle = new DetalleCompra();
        detalle.setIdDetalleCompra(10L);

        when(compraRepository.findById(1L)).thenReturn(Optional.of(compra));
        when(detalleCompraRepository.findByCompraId(1L)).thenReturn(List.of(detalle));

        compraService.eliminarCompra(1L);

        verify(detalleCompraRepository).deleteAll(anyList());
        verify(compraRepository).delete(compra);
    }

    @Test
    void eliminarCompra_DeberiaLanzarErrorSiNoExiste() {
        when(compraRepository.findById(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> compraService.eliminarCompra(5L));

        assertTrue(ex.getMessage().contains("Compra no encontrada"));
    }
}
