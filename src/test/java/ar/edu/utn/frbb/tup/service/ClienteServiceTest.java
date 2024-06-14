package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteMenor18Años() {
        Cliente clienteMenorDeEdad = new Cliente();
        clienteMenorDeEdad.setFechaNacimiento(LocalDate.of(2020, 2, 7));
        assertThrows(IllegalArgumentException.class, () -> clienteService.darDeAltaCliente(clienteMenorDeEdad));
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException {
        Cliente cliente = new Cliente();
        cliente.setFechaNacimiento(LocalDate.of(1978,3,25));
        cliente.setDni(29857643);
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        clienteService.darDeAltaCliente(cliente);

        verify(clienteDao, times(1)).save(cliente);
    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456437);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(26456437, false)).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(pepeRino));
    }



    @Test
    public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(pepeRino);

        clienteService.agregarCuenta(cuenta, pepeRino.getDni());

        verify(clienteDao, times(1)).save(pepeRino);

        assertEquals(1, pepeRino.getCuentas().size());
        assertEquals(pepeRino, cuenta.getTitular());

    }


    @Test
    public void testAgregarCuentaAClienteDuplicada() throws TipoCuentaAlreadyExistsException {
        Cliente luciano = new Cliente();
        luciano.setDni(26456439);
        luciano.setNombre("Pepe");
        luciano.setApellido("Rino");
        luciano.setFechaNacimiento(LocalDate.of(1978, 3,25));
        luciano.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        when(clienteDao.find(26456439, true)).thenReturn(luciano);

        clienteService.agregarCuenta(cuenta, luciano.getDni());

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2, luciano.getDni()));
        verify(clienteDao, times(1)).save(luciano);
        assertEquals(1, luciano.getCuentas().size());
        assertEquals(luciano, cuenta.getTitular());

    }

    //Agregar una CA$ y CC$ --> success 2 cuentas, titular peperino
    @Test
    public void testAgregarCA$yCC$() throws TipoCuentaAlreadyExistsException{
        Cliente peperino = new Cliente();
        peperino.setDni(12345678);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setFechaNacimiento(LocalDate.of(1975,03,25));
        peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);
        

        Cuenta cuentaCA = new Cuenta();
        cuentaCA.setMoneda(TipoMoneda.PESOS);
        cuentaCA.setBalance(0);
        cuentaCA.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cuenta cuentaCC = new Cuenta();
        cuentaCC.setMoneda(TipoMoneda.PESOS);
        cuentaCC.setBalance(1);
        cuentaCC.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        doReturn(peperino).when(clienteDao).find(12345678L, true);

        //Agregamos la cuenta de caja de ahorro
        clienteService.agregarCuenta(cuentaCA, 12345678);

        //Agregamos la cuenta corriente
        clienteService.agregarCuenta(cuentaCC, 12345678);

        verify(clienteDao, times(2)).save(peperino);
        assertEquals(2, peperino.getCuentas().size());

        assertEquals(peperino, cuentaCA.getTitular());
        assertEquals(peperino, cuentaCC.getTitular());

    }
    //Agregar una CA$ y CAU$S --> success 2 cuentas, titular peperino...
    @Test
    public void testAgregarCA$yCAU$() throws TipoCuentaAlreadyExistsException{
        Cliente peperino = new Cliente();
        peperino.setDni(12345678);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setFechaNacimiento(LocalDate.of(1975,03,25));
        peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);
        

        Cuenta cuentaCA = new Cuenta();
        cuentaCA.setMoneda(TipoMoneda.PESOS);
        cuentaCA.setBalance(0);
        cuentaCA.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        Cuenta cuentaCAUSD = new Cuenta();
        cuentaCAUSD.setMoneda(TipoMoneda.DOLARES);
        cuentaCAUSD.setBalance(130);
        cuentaCAUSD.setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        doReturn(peperino).when(clienteDao).find(12345678L, true);
        //Agregamos la cuenta de caja de ahorro
        clienteService.agregarCuenta(cuentaCA, 12345678);

        //Agregamos la cuenta corriente
        clienteService.agregarCuenta(cuentaCAUSD, 12345678);

        verify(clienteDao, times(2)).save(peperino);
        assertEquals(2, peperino.getCuentas().size());

        assertEquals(peperino, cuentaCA.getTitular());
        assertEquals(peperino, cuentaCAUSD.getTitular());
    
    
    
        //Testear clienteService.buscarPorDni
}
@Test
void testBuscarClientePorDniSuccess() {
    
    // Configurar el cliente de prueba
    Cliente peperino = new Cliente();
    peperino.setDni(12345689L);
    peperino.setNombre("Pepe");
    peperino.setApellido("Rino");
    peperino.setFechaNacimiento(LocalDate.of(2002, 1, 1));
    peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);

    // Configurar el comportamiento del mock para devolver el cliente
    doReturn(peperino).when(clienteDao).find(12345689L, true);
    
    // Llamar al método a probar
    Cliente resultado = clienteService.buscarClientePorDni(12345689L);

    // Verificar que se devolvió el cliente correcto
    assertNotNull(resultado);
    assertEquals(12345689L, resultado.getDni());

    // Verificar que se llamó al método find del clienteDao con los parámetros correctos
    verify(clienteDao, times(1)).find(12345689L, true);
}



    @Test
    public void testBuscarClientePorDniFailed() throws IllegalArgumentException{
        long dniNoExistente = 99999999L;
        when(clienteDao.find(dniNoExistente, true)).thenReturn(null);

        
        // Llamar al método a probar y verificar que se lanza una excepción
        assertThrows(IllegalArgumentException.class, () -> {
        clienteService.buscarClientePorDni(dniNoExistente);
        });
    
        // Verificar que se llamó al método find del clienteDao con los parámetros correctos
        verify(clienteDao, times(1)).find(dniNoExistente, true);


    }
}