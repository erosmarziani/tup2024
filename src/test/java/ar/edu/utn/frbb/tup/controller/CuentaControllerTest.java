package ar.edu.utn.frbb.tup.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import ar.edu.utn.frbb.tup.controller.Dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.Dto.MovimientosResponseDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.exception.ClienteServiceException;
import ar.edu.utn.frbb.tup.service.exception.CuentaServiceException;

@ExtendWith(MockitoExtension.class)
public class CuentaControllerTest {

    @Mock
    private CuentaService cuentaService;

    @Mock
    private CuentaValidator cuentaValidator;

    @InjectMocks
    private CuentaController cuentaController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCuentasSuccess() throws ErrorArchivoException, CuentaServiceException {
        // Arrange
        List<Cuenta> cuentas = Arrays.asList(new Cuenta(), new Cuenta());

        when(cuentaService.obtenerCuentas()).thenReturn(cuentas);

        // Act
        ResponseEntity<List<Cuenta>> response = cuentaController.getCuentas();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cuentas, response.getBody());

        verify(cuentaService, times(1)).obtenerCuentas();
    }

    @Test
    public void testGetCuentaPorIdClienteSuccess() throws ErrorArchivoException, ClienteServiceException {
        // Arrange
        Long dni = 12345678L;
        List<Cuenta> cuentas = Arrays.asList(new Cuenta());

        when(cuentaService.obtenerCuentasPorIdCliente(dni)).thenReturn(cuentas);

        // Act
        ResponseEntity<List<Cuenta>> response = cuentaController.getCuentaPorIdCliente(dni);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cuentas, response.getBody());

        verify(cuentaService, times(1)).obtenerCuentasPorIdCliente(dni);
    }

    @Test
    public void testGetCuentaPorIdClienteError() throws ErrorArchivoException, ClienteServiceException {
        // Arrange
        Long dni = 12345678L;

        doThrow(new ClienteServiceException("Cliente no encontrado")).when(cuentaService).obtenerCuentasPorIdCliente(dni);

        // Act & Assert
        assertThrows(ClienteServiceException.class, () -> cuentaController.getCuentaPorIdCliente(dni));

        verify(cuentaService, times(1)).obtenerCuentasPorIdCliente(dni);
    }

    @Test
    public void testAgregarCuentaSuccess() throws ErrorArchivoException, ClienteServiceException, CuentaServiceException {
        // Arrange
        CuentaDto cuentaDto = new CuentaDto();
        Cuenta cuenta = new Cuenta();

        doNothing().when(cuentaValidator).validarCuenta(cuentaDto);
        when(cuentaService.altaCuenta(cuentaDto)).thenReturn(cuenta);

        // Act
        ResponseEntity<Cuenta> response = cuentaController.agregarCuenta(cuentaDto);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(cuenta, response.getBody());

        verify(cuentaValidator, times(1)).validarCuenta(cuentaDto);
        verify(cuentaService, times(1)).altaCuenta(cuentaDto);
    }

    @Test
    public void testAgregarCuentaError() throws ErrorArchivoException, ClienteServiceException, CuentaServiceException {
        // Arrange
        CuentaDto cuentaDto = new CuentaDto();

        doThrow(new ClienteServiceException("Error al validar el cliente")).when(cuentaValidator).validarCuenta(cuentaDto);

        // Act & Assert
        assertThrows(ClienteServiceException.class, () -> cuentaController.agregarCuenta(cuentaDto));

        verify(cuentaValidator, times(1)).validarCuenta(cuentaDto);
        verify(cuentaService, times(0)).altaCuenta(any());
    }

    @Test
    public void testObtenerTransaccionesSuccess() throws ErrorArchivoException, CuentaServiceException {
        // Arrange
        long idCuenta = 1L;
        MovimientosResponseDto movimientosResponseDto = new MovimientosResponseDto();

        when(cuentaService.obtenerMovimientos(idCuenta)).thenReturn(movimientosResponseDto);

        // Act
        ResponseEntity<MovimientosResponseDto> response = cuentaController.obtenerTransacciones(idCuenta);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(movimientosResponseDto, response.getBody());

        verify(cuentaService, times(1)).obtenerMovimientos(idCuenta);
    }

    @Test
    public void testObtenerTransaccionesError() throws ErrorArchivoException, CuentaServiceException {
        // Arrange
        long idCuenta = 1L;

        doThrow(new CuentaServiceException("Cuenta no encontrada")).when(cuentaService).obtenerMovimientos(idCuenta);

        // Act & Assert
        assertThrows(CuentaServiceException.class, () -> cuentaController.obtenerTransacciones(idCuenta));

        verify(cuentaService, times(1)).obtenerMovimientos(idCuenta);
    }
}
