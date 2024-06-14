package ar.edu.utn.frbb.tup.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
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
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
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

        //    1 - cuenta existente
        @Test
    void testCuentaYaExistente() throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSportadaException {
        
        //Configurar la cuenta y el dni del titular de prueba
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        long dniTitular = 123456789;

        //Configurar el comportamiento del mock
        when(cuentaDao.find(anyLong())).thenReturn(new Cuenta());

        // Verificar que se lanza la excepcion CuentaAlreadyExistsException
        assertThrows(CuentaAlreadyExistsException.class,() -> cuentaService.darDeAltaCuenta(cuenta, dniTitular));

        //Verificar que no se llamen ciertos metodos
        verify(clienteService, never()).agregarCuenta(any(Cuenta.class), anyLong());
        verify(cuentaDao, never()).save(any(Cuenta.class));
    }

      //    2 - cuenta no soportada
      @Test
      void testTipoCuentaNoSoportada() throws CuentaAlreadyExistsException,TipoCuentaAlreadyExistsException {
        
        //Configurar la cuenta de prueba
          Cuenta cuenta = new Cuenta();
          cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
          cuenta.setMoneda(TipoMoneda.DOLARES);
          cuenta.setNumeroCuenta(1L);
          
          long dniTitular = 123455678l;
        //Configurar el comportamiento del mock
          when(cuentaDao.find(cuenta.getNumeroCuenta())).thenReturn(null);
  
          // Act & Assert 
          assertThrows(TipoCuentaNoSportadaException.class, () -> cuentaService.darDeAltaCuenta(cuenta, dniTitular));
  
          // Verificar que ciertos métodos no se llamen cuando el tipo de cuenta no es soportado
          verify(clienteService, never()).agregarCuenta(any(Cuenta.class), anyLong());
          verify(cuentaDao, never()).save(any(Cuenta.class));
      }
      
      //    3 - cliente ya tiene cuenta de ese tipo
     @Test
      void testTipoCuentaYaExistente() throws TipoCuentaAlreadyExistsException, TipoCuentaNoSportadaException {
          //Configurar el cliente y la cuenta de prueba 
          Cuenta cuentaExistente = new Cuenta();
          cuentaExistente.setMoneda(TipoMoneda.PESOS);
          cuentaExistente.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
  
          Cliente cliente = new Cliente();
          cliente.setDni(123456789L);
          cliente.addCuenta(cuentaExistente);
        
          //Configurar el comportamiento del mock
          when(clienteService.buscarClientePorDni(anyLong())).thenReturn(cliente);
  
          Cuenta cuentaNueva = new Cuenta();
          cuentaNueva.setMoneda(TipoMoneda.PESOS);
          cuentaNueva.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
          long dniTitular = 123456789L;
          
  
          // Act & Assert
          assertThrows(TipoCuentaAlreadyExistsException.class, () -> cuentaService.darDeAltaCuenta(cuentaNueva, dniTitular));
        
          //Verificacion
          verify(clienteService, times(1)).buscarClientePorDni(dniTitular);
          verify(clienteService, never()).agregarCuenta(any(Cuenta.class), anyLong());
          verify(cuentaDao, never()).save(any(Cuenta.class));
      }
  

        //    4 - cuenta creada exitosamente
    @Test
    void testCuentaCreadaSucces(){

        //Configuracion del objeto de prueba
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1L);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        long dniTitular = 12345678L;

        //Configuracion del comportamiento del mock
        when(cuentaDao.find(cuenta.getNumeroCuenta())).thenReturn(null);

        //Ejecuto el metodo a testear
        assertDoesNotThrow(() -> cuentaService.darDeAltaCuenta(cuenta, dniTitular));

        //Verificar que se guardo la cuenta
        verify(cuentaDao, times(1)).save(cuenta);
        }
}
