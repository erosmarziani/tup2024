package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSportadaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CuentaService cuentaService;

   
    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCuentaYaExistente() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSportadaException {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        long dniTitular = 123456789;

        when(cuentaDao.find(anyLong())).thenReturn(new Cuenta());

        assertThrows(CuentaAlreadyExistsException.class,() -> cuentaService.darDeAltaCuenta(cuenta, dniTitular));

        verify(clienteService, never()).agregarCuenta(any(Cuenta.class), anyLong());
        verify(cuentaDao, never()).save(any(Cuenta.class));
    }



    @Test
    void testTipoCuentaNoSoportada() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSportadaException {

        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setMoneda(TipoMoneda.DOLARES);
        long dniTitular = 123455678;

        //Act & Assert 
        assertThrows(TipoCuentaNoSportadaException.class, () -> cuentaService.darDeAltaCuenta(cuenta, dniTitular));

         verify(clienteService, never()).agregarCuenta(any(Cuenta.class), anyLong());
         verify(cuentaDao, never()).save(any(Cuenta.class));
    }

   @Test
    void testTipoCuentaYaExistente() throws TipoCuentaAlreadyExistsException, TipoCuentaNoSportadaException {
        // Arrange
        Cuenta cuentaExistente = new Cuenta();
        cuentaExistente.setMoneda(TipoMoneda.PESOS);
        cuentaExistente.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cliente cliente = new Cliente();
        cliente.setDni(123456789L);
        cliente.addCuenta(cuentaExistente);

        when(clienteService.buscarClientePorDni(anyLong())).thenReturn(cliente);

        Cuenta cuentaNueva = new Cuenta();
        cuentaNueva.setMoneda(TipoMoneda.PESOS);
        cuentaNueva.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        long dniTitular = 123456789;

        // Act & Assert
        assertThrows(TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaNueva, dniTitular));

        verify(clienteService, times(1)).buscarClientePorDni(dniTitular);
        verify(clienteService, never()).agregarCuenta(any(Cuenta.class), anyLong());
        verify(cuentaDao, never()).save(any(Cuenta.class));
    }

}
