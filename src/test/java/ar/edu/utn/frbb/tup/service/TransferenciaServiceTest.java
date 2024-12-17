package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.Dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.exception.TransferenciaException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.MovimientosDaoImpl;
import ar.edu.utn.frbb.tup.service.exception.CuentaServiceException;
import ar.edu.utn.frbb.tup.service.exception.TransferenciaServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferenciaServiceTest {

    @Mock
    private CuentaDaoImpl cuentaDao;

    @Mock
    private MovimientosDaoImpl movimientosDao;

    @Mock
    private BanelcoService banelcoService;

    @InjectMocks
    private TransferenciaService transferenciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidacionTransferenciaMismoBancoExitoso() throws ErrorArchivoException, CuentaServiceException, TransferenciaServiceException, TransferenciaException {
        // Arrange
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdOrigen("1");
        transferenciaDto.setIdDestino("2");
        transferenciaDto.setImporte("1000");
        transferenciaDto.setFechaOperacion("2024-06-29");

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setBalance(2000);
        cuentaOrigen.setMoneda(TipoMoneda.ARS);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(2L);
        cuentaDestino.setBalance(500);
        cuentaDestino.setMoneda(TipoMoneda.ARS);

        when(cuentaDao.obtenerCuentaPorId(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.obtenerCuentaPorId(2L)).thenReturn(cuentaDestino);

        // Act
        transferenciaService.validacionTransferencia(transferenciaDto);

        // Assert
        verify(cuentaDao).actualizarBalance(1L, 1000);
        verify(cuentaDao).actualizarBalance(2L, 1500);
        verify(movimientosDao, times(2)).guardarMovimiento(any(Movimiento.class), anyLong());
    }

    @Test
    void testValidacionTransferenciaDistintosBancosExitoso() throws ErrorArchivoException, CuentaServiceException, TransferenciaServiceException, TransferenciaException {
        // Arrange
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdOrigen("1");
        transferenciaDto.setIdDestino("2");
        transferenciaDto.setImporte("1000");
        transferenciaDto.setFechaOperacion("2024-06-29");

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setBalance(2000);
        cuentaOrigen.setMoneda(TipoMoneda.ARS);

        when(cuentaDao.obtenerCuentaPorId(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.obtenerCuentaPorId(2L)).thenReturn(null);
        when(banelcoService.transferir()).thenReturn(true);

        // Act
        transferenciaService.validacionTransferencia(transferenciaDto);

        // Assert
        verify(cuentaDao).actualizarBalance(1L, 1000);
        verify(movimientosDao).guardarMovimiento(any(Movimiento.class), eq(1L));
    }

    @Test
    void testValidacionTransferenciaSaldoInsuficiente() throws ErrorArchivoException, CuentaServiceException {
        // Arrange
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdOrigen("1");
        transferenciaDto.setImporte("3000");
        transferenciaDto.setFechaOperacion("2024-06-29");

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setBalance(2000);

        when(cuentaDao.obtenerCuentaPorId(1L)).thenReturn(cuentaOrigen);

        // Act & Assert
        TransferenciaException exception = assertThrows(TransferenciaException.class, () -> {
            transferenciaService.validacionTransferencia(transferenciaDto);
        });

        assertEquals("No se puede realizar la transferencia, saldo insuficiente", exception.getMessage());
        verify(cuentaDao, never()).actualizarBalance(anyLong(), anyDouble());
    }

    @Test
    void testValidacionTransferenciaCuentaOrigenNoEncontrada() throws ErrorArchivoException {
        // Arrange
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdOrigen("1");
        transferenciaDto.setFechaOperacion("2024-06-29");

        when(cuentaDao.obtenerCuentaPorId(1L)).thenReturn(null);

        // Act & Assert
        CuentaServiceException exception = assertThrows(CuentaServiceException.class, () -> {
            transferenciaService.validacionTransferencia(transferenciaDto);
        });

        assertEquals("No se ha encontrado la cuenta origen en la base de datos", exception.getMessage());
    }

    @Test
    void testValidacionTransferenciaRechazadaPorBanelco() throws ErrorArchivoException {
        // Arrange
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdOrigen("1");
        transferenciaDto.setIdDestino("2");
        transferenciaDto.setImporte("1000");
        transferenciaDto.setFechaOperacion("2024-06-29");

        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1L);
        cuentaOrigen.setBalance(2000);

        when(cuentaDao.obtenerCuentaPorId(1L)).thenReturn(cuentaOrigen);
        when(cuentaDao.obtenerCuentaPorId(2L)).thenReturn(null);
        when(banelcoService.transferir()).thenReturn(false);

        // Act & Assert
        TransferenciaServiceException exception = assertThrows(TransferenciaServiceException.class, () -> {
            transferenciaService.validacionTransferencia(transferenciaDto);
        });

        assertEquals("La transferencia entre los bancos ha sido rechazada", exception.getMessage());
    }
}
