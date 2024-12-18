package ar.edu.utn.frbb.tup.controller.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ar.edu.utn.frbb.tup.controller.Dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.exception.TransferenciaException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferenciaValidatorTest{
    TransferenciaValidator transferenciaValidator;

    @BeforeEach
    public void setUp() {
        transferenciaValidator = new TransferenciaValidator();
    }

    //Testeo si los datos son correctos
     @Test
    public void testValidarTransferenciaDatosValidos() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdDestino("12345");
        transferenciaDto.setIdOrigen("54321");
        transferenciaDto.setFechaOperacion("2024-09-05");
        transferenciaDto.setImporte("1000.50");
        transferenciaDto.setTipoMoneda("USD");

        assertDoesNotThrow(() -> transferenciaValidator.validarTransferencia(transferenciaDto));
    }

    //Testeo si no se ingresa idDestino
    @Test
    public void testValidarTransferenciaIdDestinoNulo() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
      //  transferenciaDto.setIdDestino(null);
        transferenciaDto.setIdOrigen("54321");
        transferenciaDto.setFechaOperacion("2024-09-05");
        transferenciaDto.setImporte("1000.50");
        transferenciaDto.setTipoMoneda("USD");

        TransferenciaException thrown = assertThrows(TransferenciaException.class, 
                () -> transferenciaValidator.validarTransferencia(transferenciaDto));
        assertEquals("El id de la cuenta es obligatorio", thrown.getMessage());
    }

    //Testeo si no se ingresa idOrigen
    @Test
    public void testValidarTransferenciaIdOrigenNulo() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdDestino("12345");
      //  transferenciaDto.setIdOrigen(null);
        transferenciaDto.setFechaOperacion("2024-09-05");
        transferenciaDto.setImporte("1000.50");
        transferenciaDto.setTipoMoneda("USD");

        TransferenciaException thrown = assertThrows(TransferenciaException.class, 
                () -> transferenciaValidator.validarTransferencia(transferenciaDto));
        assertEquals("El id de la cuenta es obligatorio", thrown.getMessage());
    }

    //Testeo si no se ingresa la fecha de operacion
    @Test
    public void testValidarTransferenciaFechaOperacionNula() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdDestino("12345");
        transferenciaDto.setIdOrigen("54321");
       // transferenciaDto.setFechaOperacion(null);
        transferenciaDto.setImporte("1000.50");
        transferenciaDto.setTipoMoneda("USD");

        TransferenciaException thrown = assertThrows(TransferenciaException.class, 
                () -> transferenciaValidator.validarTransferencia(transferenciaDto));
        assertEquals("La fecha de operacion es obligatoria", thrown.getMessage());
    }

    //Testeo si no se ingresa el importe de la transferencia
    @Test
    public void testValidarTransferenciaImporteNulo() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdDestino("12345");
        transferenciaDto.setIdOrigen("54321");
        transferenciaDto.setFechaOperacion("2024-09-05");
        //transferenciaDto.setImporte(null);
        transferenciaDto.setTipoMoneda("USD");

        TransferenciaException thrown = assertThrows(TransferenciaException.class, 
                () -> transferenciaValidator.validarTransferencia(transferenciaDto));
        assertEquals("El importe de la transferencia es obligatorio", thrown.getMessage());
    }

    //Testeo si no se ingresa el importe en formato numerico
    @Test
    public void testValidarTransferenciaImporteInvalido() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdDestino("12345");
        transferenciaDto.setIdOrigen("54321");
        transferenciaDto.setFechaOperacion("2024-09-05");
        transferenciaDto.setImporte("numero");
        transferenciaDto.setTipoMoneda("USD");

        NumberFormatException thrown = assertThrows(NumberFormatException.class, 
                () -> transferenciaValidator.validarTransferencia(transferenciaDto));
        assertEquals("El importe de la transferencia debe ser numérico", thrown.getMessage());
    }

    //Testeo si el importe de la transferencia es negativo
    @Test
    public void testValidarTransferenciaImporteNegativo() {
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdDestino("12345");
        transferenciaDto.setIdOrigen("54321");
        transferenciaDto.setFechaOperacion("2024-09-05");
        transferenciaDto.setImporte("-1000.50");
        transferenciaDto.setTipoMoneda("USD");

        assertThrows(TransferenciaException.class, () -> transferenciaValidator.validarTransferencia(transferenciaDto));

    }

    //Testeo si no se ingresa tipo de moneda
    @Test
    public void testValidarTransferenciaTipoMonedaNulo() {
        
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdDestino("12345");
        transferenciaDto.setIdOrigen("54321");
        transferenciaDto.setFechaOperacion("2024-09-05");
        transferenciaDto.setImporte("1000.50");
      //  transferenciaDto.setTipoMoneda(null);

        
      TransferenciaException thrown = assertThrows(TransferenciaException.class, 
                () -> transferenciaValidator.validarTransferencia(transferenciaDto));
        assertEquals("El tipo de moneda es obligatorio", thrown.getMessage());
    }

    //Testeo si el tipo de moneda ingresado es invalido
    @Test
    public void testValidarTransferenciaTipoMonedaInvalido() {
        // Arrange
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdDestino("12345");
        transferenciaDto.setIdOrigen("54321");
        transferenciaDto.setFechaOperacion("2024-09-05");
        transferenciaDto.setImporte("1000.50");
        transferenciaDto.setTipoMoneda("EUR"); // Moneda inválida

        // Act & Assert
        TransferenciaException thrown = assertThrows(TransferenciaException.class, 
                () -> transferenciaValidator.validarTransferencia(transferenciaDto));
        assertEquals("El tipo de moneda debe ser USD o ARS", thrown.getMessage());
    }

    @Test
    public void testValidarTransferenciaFechaOperacionInvalida() {
        // Arrange
        TransferenciaDto transferenciaDto = new TransferenciaDto();
        transferenciaDto.setIdDestino("12345");
        transferenciaDto.setIdOrigen("54321");
        transferenciaDto.setFechaOperacion("2024-02-30"); // Fecha inválida
        transferenciaDto.setImporte("1000.50");
        transferenciaDto.setTipoMoneda("USD");

        assertThrows(TransferenciaException.class, () ->
      transferenciaValidator.validarFecha(transferenciaDto.getFechaOperacion()));
    }
}

