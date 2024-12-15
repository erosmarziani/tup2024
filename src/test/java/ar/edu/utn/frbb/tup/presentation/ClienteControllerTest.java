/*import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.ClienteController;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorActualizarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarLineaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorManejoArchivoException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.service.exception.CuentaNoEncontradaException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteValidator clienteValidator;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetClientes_Success() throws ErrorArchivoNoEncontradoException, ClienteNoEncontradoException {
        // Arrange
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        when(clienteService.mostrarListaCliente()).thenReturn(clientes);

        // Act
        ResponseEntity<List<Cliente>> response = clienteController.getClientes();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientes, response.getBody());
        verify(clienteService, times(1)).mostrarListaCliente();
    }

    @Test
    public void testObtenerCliente_Success() throws ErrorArchivoNoEncontradoException, ClienteNoEncontradoException {
        // Arrange
        Cliente cliente = new Cliente();
        when(clienteService.obtenerCliente(anyLong())).thenReturn(cliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.obtenerCliente(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteService, times(1)).obtenerCliente(anyLong());
    }

    @Test
    public void testCrearCliente_Success() throws ClienteAlreadyExistsException, ClienteMenorDeEdadException, ErrorArchivoNoEncontradoException, ErrorGuardarClienteException {
        // Arrange
        ClienteDto clienteDto = new ClienteDto();
        Cliente cliente = new Cliente();
        when(clienteService.darDeAltaCliente(clienteDto)).thenReturn(cliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.crearCliente(clienteDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cliente, response.getBody());
        verify(clienteValidator, times(1)).validarCliente(clienteDto);
        verify(clienteService, times(1)).darDeAltaCliente(
*/