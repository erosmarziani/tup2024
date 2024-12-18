package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;
import ar.edu.utn.frbb.tup.persistence.implementation.ClienteDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.service.exception.ClienteServiceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteDaoImpl clienteDao;

    @Mock
    private CuentaDaoImpl cuentaDao;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDarDeAltaClienteExitoso() throws  ErrorArchivoException, ClienteServiceException {
        // Arrange
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setDni("12345678");
        clienteDto.setFechaNacimiento("1990-01-01");
        clienteDto.setDireccion("Calle Falsa 123");

        Cliente cliente = new Cliente(clienteDto);

        when(clienteDao.obtenerClientePorDNI(cliente.getDni())).thenReturn(null);

        // Act
        Cliente resultado = clienteService.darDeAltaCliente(clienteDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(cliente.getDni(), resultado.getDni());
        verify(clienteDao, times(1)).guardarCliente(argThat(clienteGuardado ->
         clienteGuardado.getDni() == Long.parseLong(clienteDto.getDni()) &&
        clienteGuardado.getNombre().equals(clienteDto.getNombre()) &&
        clienteGuardado.getApellido().equals(clienteDto.getApellido())
        //Verifico que los atributos sean iguales
));    }

    @Test
    void testDarDeAltaClienteYaExistente() throws ErrorArchivoException {
        // Arrange
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setDni("12345678");
        clienteDto.setFechaNacimiento("1990-01-01");
        clienteDto.setDireccion("Calle Falsa 123");

        Cliente clienteExistente = new Cliente(clienteDto);

        when(clienteDao.obtenerClientePorDNI(clienteExistente.getDni())).thenReturn(clienteExistente);

        // Act & Assert
        ClienteServiceException exception = assertThrows(ClienteServiceException.class, () -> {
            clienteService.darDeAltaCliente(clienteDto);
        });

        assertEquals("Ya existe un cliente con DNI 12345678", exception.getMessage());
        verify(clienteDao, never()).guardarCliente(any());
    }

    @Test
    void testEliminarClienteExitoso() throws ErrorArchivoException, ClienteServiceException {
        // Arrange
        long dni = 12345678L;
    
        Cliente cliente = new Cliente();
        cliente.setDni(dni);
    
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        List<Cuenta> cuentas = List.of(cuenta);
    
        when(clienteDao.obtenerClientePorDNI(dni)).thenReturn(cliente);
        when(cuentaDao.obtenerCuentasDelCliente(dni)).thenReturn(cuentas);
    
        // Act
        Cliente resultado = clienteService.eliminarCliente(dni);
    
        // Assert
        assertNotNull(resultado);
        assertEquals(dni, resultado.getDni());
        verify(cuentaDao, times(1)).eliminarCuenta(1L);
        verify(clienteDao, times(1)).eliminarCliente(dni);
    }
    
    @Test
    void testEliminarClienteNoExistente() throws ErrorArchivoException {
        // Arrange
        long dni = 12345678L;
        when(clienteDao.obtenerClientePorDNI(dni)).thenReturn(null);

        // Act & Assert
        ClienteServiceException exception = assertThrows(ClienteServiceException.class, () -> {
            clienteService.eliminarCliente(dni);
        });

        assertEquals("El cliente no ha sido encontrado en la base de datos", exception.getMessage());
        verify(clienteDao, never()).eliminarCliente(dni);
    }

    @Test
    void testMostrarListaClienteExitoso() throws ErrorArchivoException {
        // Arrange
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente());

        when(clienteDao.obtenerListaClientes()).thenReturn(clientes);

        // Act
        List<Cliente> resultado = clienteService.mostrarListaCliente();

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(clienteDao, times(1)).obtenerListaClientes();
    }

    @Test
    void testMostrarListaClienteVacia() throws ErrorArchivoException {
        // Arrange
        when(clienteDao.obtenerListaClientes()).thenReturn(new ArrayList<>());

        // Act & Assert
        ErrorArchivoException exception = assertThrows(ErrorArchivoException.class, () -> {
            clienteService.mostrarListaCliente();
        });

        assertEquals("No se encontraron clientes en la base de datos", exception.getMessage());
        verify(clienteDao, times(1)).obtenerListaClientes();
    }
}
