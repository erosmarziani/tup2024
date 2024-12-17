package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.Dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.Dto.MovimientosResponseDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.service.exception.CuentaServiceException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;
import ar.edu.utn.frbb.tup.persistence.implementation.ClienteDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.MovimientosDaoImpl;
import ar.edu.utn.frbb.tup.service.exception.ClienteServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CuentaServiceTest {

    @Mock
    private CuentaDaoImpl cuentaDao;

    @Mock
    private ClienteDaoImpl clienteDao;

    @Mock
    private MovimientosDaoImpl movimientoDao;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAltaCuentaExitosa() throws ErrorArchivoException, ClienteServiceException, CuentaServiceException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setIdCuenta("1");
        cuentaDto.setBalance("1000");
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setDniTitular("12345678");
        cuentaDto.setMoneda("ARS");

        Cliente cliente = new Cliente();
        cliente.setDni(12345678);

        when(cuentaDao.obtenerCuentaPorId(1L)).thenReturn(null);
        when(clienteDao.obtenerClientePorDNI(12345678L)).thenReturn(cliente);

        Cuenta resultado = cuentaService.altaCuenta(cuentaDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getNumeroCuenta());
        assertEquals(1000.0, resultado.getBalance());
        verify(cuentaDao, times(1)).guardarCuenta(any(Cuenta.class));
    }

    @Test
    void testAltaCuentaYaExistente() throws ErrorArchivoException {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setIdCuenta("1");

        Cuenta cuentaExistente = new Cuenta();
        when(cuentaDao.obtenerCuentaPorId(1L)).thenReturn(cuentaExistente);

        CuentaServiceException exception = assertThrows(CuentaServiceException.class, () -> {
            cuentaService.altaCuenta(cuentaDto);
        });

        assertEquals("Ya existe una cuenta con el ID proporcionado", exception.getMessage());
        verify(cuentaDao, never()).guardarCuenta(any());
    }

    @Test
    void testEliminarCuentasClienteExitoso() throws ErrorArchivoException, CuentaServiceException {
        long idCliente = 12345678L;
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);

        when(cuentaDao.eliminarCuenta(idCliente)).thenReturn(cuenta);

        Cuenta resultado = cuentaService.eliminarCuentasCliente(idCliente);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getNumeroCuenta());
        verify(cuentaDao, times(1)).eliminarCuenta(idCliente);
    }

    @Test
    void testEliminarCuentasClienteNoExistente() throws ErrorArchivoException {
        long idCliente = 12345678L;
        when(cuentaDao.eliminarCuenta(idCliente)).thenReturn(null);

        CuentaServiceException exception = assertThrows(CuentaServiceException.class, () -> {
            cuentaService.eliminarCuentasCliente(idCliente);
        });

        assertEquals("El cliente no tiene cuentas asociadas en la base de datos.", exception.getMessage());
        verify(cuentaDao, times(1)).eliminarCuenta(idCliente);
    }

    @Test
    void testObtenerCuentasPorIdClienteExitoso() throws ErrorArchivoException, ClienteServiceException {
        long idCliente = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(12345678);

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(new Cuenta());

        when(clienteDao.obtenerClientePorDNI(idCliente)).thenReturn(cliente);
        when(cuentaDao.obtenerCuentasDelCliente(idCliente)).thenReturn(cuentas);

        List<Cuenta> resultado = cuentaService.obtenerCuentasPorIdCliente(idCliente);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(cuentaDao, times(1)).obtenerCuentasDelCliente(idCliente);
    }

    @Test
    void testObtenerMovimientosCuentaExitosa() throws CuentaServiceException, ErrorArchivoException {
        long idCuenta = 1L;
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);

        List<Movimiento> movimientos = new ArrayList<>();
        Movimiento movimiento = new Movimiento();
        movimiento.setFechaOperacion(LocalDate.now());
        movimiento.setTipo(TipoMovimiento.DEBITO);
        movimiento.setDescripcion("Compra supermercado");
        movimiento.setMonto(200.0);
        movimientos.add(movimiento);

        when(cuentaDao.obtenerCuentaPorId(idCuenta)).thenReturn(cuenta);
        when(movimientoDao.obtenerMovimientoPorCuenta(idCuenta)).thenReturn(movimientos);

        MovimientosResponseDto resultado = cuentaService.obtenerMovimientos(idCuenta);

        assertNotNull(resultado);
        assertEquals("1", resultado.getNumeroCuenta());
        assertEquals(1, resultado.getTransacciones().size());
        verify(movimientoDao, times(1)).obtenerMovimientoPorCuenta(idCuenta);
    }

    @Test
    void testObtenerMovimientosCuentaNoExistente() throws ErrorArchivoException {
        long idCuenta = 1L;
        when(cuentaDao.obtenerCuentaPorId(idCuenta)).thenReturn(null);

        CuentaServiceException exception = assertThrows(CuentaServiceException.class, () -> {
            cuentaService.obtenerMovimientos(idCuenta);
        });

        assertEquals("Cuenta no encontrada", exception.getMessage());
        verify(movimientoDao, never()).obtenerMovimientoPorCuenta(idCuenta);
    }
}
