package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

import ar.edu.utn.frbb.tup.controller.Dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.exception.TransferenciaException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.MovimientosDaoImpl;
import ar.edu.utn.frbb.tup.service.exception.CuentaServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TransferenciaServiceTest {

    @Mock
    private CuentaDaoImpl cuentaDao;

    @Mock
    private MovimientosDaoImpl movimientosDao;

    @InjectMocks
    private TransferenciaService transferenciaService;

    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;

    @BeforeEach
    void setUp() {
        // Inicializamos los mocks
        MockitoAnnotations.openMocks(this);

        // Creamos las cuentas para las pruebas
        cuentaOrigen = new Cuenta();
        cuentaOrigen.setNumeroCuenta(1);
        cuentaOrigen.setBalance(2000.0);
        cuentaOrigen.setMoneda(TipoMoneda.ARS);

        cuentaDestino = new Cuenta();
        cuentaDestino.setNumeroCuenta(2);
        cuentaDestino.setBalance(1000.0);
        cuentaDestino.setMoneda(TipoMoneda.ARS);
    }

    @Test
    void testTransferenciaExitosa() throws Exception {
        // Arrange
        TransferenciaDto dto = new TransferenciaDto();
        dto.setIdOrigen("1");
        dto.setIdDestino("2");
        dto.setImporte("500");
        dto.setFechaOperacion("2022-02-02");
        dto.setTipoMoneda("ARS");

        when(cuentaDao.obtenerCuentaPorId(1)).thenReturn(cuentaOrigen);
        when(cuentaDao.obtenerCuentaPorId(2)).thenReturn(cuentaDestino);

        // Act
        transferenciaService.validacionTransferencia(dto);

        // Assert
        verify(cuentaDao).actualizarBalance(1, 1500.0);  // 2000 - 500
        verify(cuentaDao).actualizarBalance(2, 1500.0);  // 1000 + 500
    }

}