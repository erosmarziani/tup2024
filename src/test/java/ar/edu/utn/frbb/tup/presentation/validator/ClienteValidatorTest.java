package ar.edu.utn.frbb.tup.presentation.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteValidatorTest {

    ClienteValidator clienteValidator;

    @BeforeEach
    public void setUp() {
        clienteValidator = new ClienteValidator();
    }

    
    @Test
    public void testClienteValidator() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Eros");
        clienteDto.setApellido("Marziani");
        clienteDto.setDireccion("Salta 432");
        clienteDto.setFechaNacimiento("2002-03-04");
        clienteDto.setBanco("Credicoop");
        clienteDto.setDni("12341234");

        // Verifico que no haga ningun Throw
        assertDoesNotThrow(() -> clienteValidator.validarCliente(clienteDto));
    }

        // Testeo de cliente sin nombre
    @Test
    public void testClienteSinNombre() {
        ClienteDto clienteDto = new ClienteDto();
        // clienteDto.setNombre("Peperino");
        clienteDto.setApellido("Marziani");
        clienteDto.setDireccion("Irigoyen 8879");
        clienteDto.setFechaNacimiento("2000-02-02");
        clienteDto.setBanco("Galicia");
        clienteDto.setDni("11223388");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

        // Testeo de cliente sin apellido
    @Test
    public void testClienteSinApellidoException() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Eros");
        // clienteDto.setApellido("Pomoro");
        clienteDto.setDireccion("Mitre 432");
        clienteDto.setFechaNacimiento("2000-12-12");
        clienteDto.setBanco("ICBC");
        clienteDto.setDni("77889966");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    // Testeo de cliente sin direccion
    @Test
    public void testClienteSinDireccion() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Erps");
        clienteDto.setApellido("Mateo");
        // clienteDto.setDireccion("");
        clienteDto.setFechaNacimiento("1999-03-02");
        clienteDto.setBanco("Pampa");
        clienteDto.setDni("8888888");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    // Testeo de cliente sin fecha de nacimiento
    @Test
    public void testClienteSinFechaNacimiento() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Roberto");
        clienteDto.setApellido("Galati");
        clienteDto.setDireccion("Villarino 34");
        // clienteDto.setFechaNacimiento("");
        clienteDto.setBanco("Supervielle");
        clienteDto.setDni("99999999");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    // Testeo de cliente sin banco
    @Test
    public void testClienteSinBanco() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Daniela");
        clienteDto.setApellido("Marziani");
        clienteDto.setDireccion("Zeballos 290");
        clienteDto.setFechaNacimiento("2003-05-07");
        // clienteDto.setBanco("Macro");
        clienteDto.setDni("24829115");

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }

    @Test
    public void testClienteSinDni() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Sofia");
        clienteDto.setApellido("Urquiza");
        clienteDto.setDireccion("Calle inexistente 345");
        clienteDto.setFechaNacimiento("1999-11-11");
        clienteDto.setBanco("Galicia");
        // clienteDto.setDni();

        assertThrows(IllegalArgumentException.class, () -> clienteValidator.validarCliente(clienteDto));
    }
    
      @Test
     public void testClienteDniNegativo(){
      ClienteDto clienteDto = new ClienteDto();
     clienteDto.setNombre("Luciana");
     clienteDto.setApellido("Castro");
     clienteDto.setDireccion("San Jose 98");
     clienteDto.setFechaNacimiento("1980-01-01");
      clienteDto.setBanco("Macro");
      clienteDto.setDni("-12341234");
     
      assertThrows(IllegalArgumentException.class, () ->
      clienteValidator.validarCliente(clienteDto));
      }

      @Test void testClienteMenorDeEdad(){
        ClienteDto clienteDto = new ClienteDto();
     clienteDto.setNombre("Luciana");
     clienteDto.setApellido("Castro");
     clienteDto.setDireccion("San Jose 98");
     clienteDto.setFechaNacimiento("2020-01-01");
      clienteDto.setBanco("Macro");
      clienteDto.setDni("12341234");

      assertThrows(IllegalArgumentException.class, () ->
      clienteValidator.validarFechaNacimiento(clienteDto.getFechaNacimiento()));
      }

      @Test
    public void testFechaNacimientoMuyAntigua() {
        ClienteDto clienteDto = new ClienteDto();
     clienteDto.setNombre("Luciana");
     clienteDto.setApellido("Castro");
     clienteDto.setDireccion("San Jose 98");
     clienteDto.setFechaNacimiento("1555-01-01");
      clienteDto.setBanco("Macro");
      clienteDto.setDni("12341234");
      
      assertThrows(IllegalArgumentException.class, () ->
      clienteValidator.validarFechaNacimiento(clienteDto.getFechaNacimiento()));
    }
    
    @Test
    public void testFechaNacimientoFormatoIncorrecto(){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Luciana");
        clienteDto.setApellido("Castro");
        clienteDto.setDireccion("San Jose 98");
        clienteDto.setFechaNacimiento("01-2000-01");
         clienteDto.setBanco("Macro");
         clienteDto.setDni("12341234");

         assertThrows(IllegalArgumentException.class, () ->
      clienteValidator.validarFechaNacimiento(clienteDto.getFechaNacimiento()));
    }
      @Test
      public void testFechaNacimientoValida() {
          // Arrange
          String fechaNacimiento = "2000-01-01";
  
          // Act & Assert
          assertDoesNotThrow(() -> clienteValidator.validarFechaNacimiento(fechaNacimiento));
      }

      @Test
      public void testDniValido() {
        String dniValido = "12345678";

        assertDoesNotThrow(() -> clienteValidator.validarDni(dniValido));
      }

      @Test
      public void testLongitudIncorrectaDNI() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Luciana");
        clienteDto.setApellido("Castro");
        clienteDto.setDireccion("San Jose 98");
        clienteDto.setFechaNacimiento("01-2000-01");
         clienteDto.setBanco("Macro");
         clienteDto.setDni("111111111111111111");

         assertThrows(IllegalArgumentException.class, () ->
         clienteValidator.validarDni(clienteDto.getDni()));
      }

      


}