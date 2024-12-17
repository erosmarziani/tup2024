package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.exception.ClienteServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteValidator clienteValidator;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearClienteSuccess() throws ErrorArchivoException, ClienteServiceException {
        // Arrange
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = getCliente();

        doNothing().when(clienteValidator).validarCliente(clienteDto);
        when(clienteService.darDeAltaCliente(clienteDto)).thenReturn(cliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.crearCliente(clienteDto);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cliente, response.getBody());

        verify(clienteValidator, times(1)).validarCliente(clienteDto);
        verify(clienteService, times(1)).darDeAltaCliente(clienteDto);
    }

    @Test
    public void testCrearClienteFail() throws ErrorArchivoException, ClienteServiceException {
        // Arrange
        ClienteDto clienteDto = getClienteDto();

        doThrow(new ClienteServiceException("Error al crear cliente"))
                .when(clienteService).darDeAltaCliente(clienteDto);

        // Act & Assert
        assertThrows(ClienteServiceException.class, () -> clienteController.crearCliente(clienteDto));

        verify(clienteValidator, times(1)).validarCliente(clienteDto);
        verify(clienteService, times(1)).darDeAltaCliente(clienteDto);
    }

    @Test
    public void testObtenerClienteSuccess() throws ErrorArchivoException {
        // Arrange
        long dni = 12345678L;
        Cliente cliente = getCliente();

        when(clienteService.obtenerCliente(dni)).thenReturn(cliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.obtenerCliente(dni);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cliente, response.getBody());

        verify(clienteService, times(1)).obtenerCliente(dni);
    }

    @Test
    public void testObtenerClienteFail() throws ErrorArchivoException {
        // Arrange
        long dni = 12345678L;

        doThrow(new ErrorArchivoException("Cliente no encontrado"))
                .when(clienteService).obtenerCliente(dni);

        // Act & Assert
        assertThrows(ErrorArchivoException.class, () -> clienteController.obtenerCliente(dni));

        verify(clienteService, times(1)).obtenerCliente(dni);
    }

    @Test
    public void testGetClientesSuccess() throws ErrorArchivoException {
        // Arrange
        List<Cliente> clientes = Arrays.asList(getCliente(), getCliente());

        when(clienteService.mostrarListaCliente()).thenReturn(clientes);

        // Act
        ResponseEntity<List<Cliente>> response = clienteController.getClientes();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(clientes, response.getBody());

        verify(clienteService, times(1)).mostrarListaCliente();
    }

    @Test
    public void testGetClientesFail() throws ErrorArchivoException {
        // Arrange
        doThrow(new ErrorArchivoException("Error al obtener lista de clientes"))
                .when(clienteService).mostrarListaCliente();

        // Act & Assert
        assertThrows(ErrorArchivoException.class, () -> clienteController.getClientes());

        verify(clienteService, times(1)).mostrarListaCliente();
    }

    @Test
    public void testDeleteClienteSuccess() throws ErrorArchivoException, ClienteServiceException {
        // Arrange
        long dni = 12345678L;
        Cliente cliente = getCliente();

        when(clienteService.eliminarCliente(dni)).thenReturn(cliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.deleteCliente(dni);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cliente, response.getBody());

        verify(clienteService, times(1)).eliminarCliente(dni);
    }

    @Test
    public void testDeleteClienteFail() throws ErrorArchivoException, ClienteServiceException {
        // Arrange
        long dni = 12345678L;

        doThrow(new ClienteServiceException("Error al eliminar cliente"))
                .when(clienteService).eliminarCliente(dni);

        // Act & Assert
        assertThrows(ClienteServiceException.class, () -> clienteController.deleteCliente(dni));

        verify(clienteService, times(1)).eliminarCliente(dni);
    }

    private Cliente getCliente() {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678);
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        cliente.setDireccion("Calle Falsa 123");
        cliente.setTelefono("123456789");
        return cliente;
    }

    private ClienteDto getClienteDto() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni("12345678");
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento(("2000-1-1"));
        clienteDto.setDireccion("Calle Falsa 123");
        clienteDto.setTelefono("123456789");
        return clienteDto;
    }
}
