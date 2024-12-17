package ar.edu.utn.frbb.tup.controller.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ar.edu.utn.frbb.tup.controller.Dto.CuentaDto;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CuentaValidatorTest {
    CuentaValidator cuentaValidator;

    @BeforeEach
    public void setUp() {
        cuentaValidator = new CuentaValidator();
    }

    @Test
    public void testValidarCuentaExitosa(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setIdCuenta("14859");
        cuentaDto.setMoneda("ARS");
        cuentaDto.setTipoCuenta("CUENTA_CORRIENTE");
        cuentaDto.setDniTitular("44051174");

        //Verificar que no genere ningun throw
        assertDoesNotThrow(() -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testIdCuentaInexistente(){
        CuentaDto cuentaDto = new CuentaDto();
       // cuentaDto.setIdCuenta("14859");
        cuentaDto.setMoneda("ARS");
        cuentaDto.setTipoCuenta("CUENTA_CORRIENTE");
        cuentaDto.setDniTitular("44051174");

        //Verificar que lanze una excepcion
        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }
    
    @Test
    public void testCuentaSinMoneda(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setIdCuenta("14859");
        //cuentaDto.setMoneda("ARS");
        cuentaDto.setTipoCuenta("CUENTA_CORRIENTE");
        cuentaDto.setDniTitular("44051174");

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testCuentaSinTipoCuenta(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setIdCuenta("14859");
        cuentaDto.setMoneda("ARS");
       // cuentaDto.setTipoCuenta("CUENTA_CORRIENTE");
        cuentaDto.setDniTitular("44051174");

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testCuentaSinIditular(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setIdCuenta("14859");
        cuentaDto.setMoneda("ARS");
        cuentaDto.setTipoCuenta("CUENTA_CORRIENTE");
       // cuentaDto.setDniTitular("44051174");

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testCuentaConDniInvalido(){
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setIdCuenta("14859");
        cuentaDto.setMoneda("ARS");
        cuentaDto.setTipoCuenta("CUENTA_CORRIENTE");
        cuentaDto.setDniTitular("44051174X");
        
        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testDniIncorrecto() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setIdCuenta("12345");
        cuentaDto.setMoneda("USD");
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setDniTitular("00000000"); // Valor no permitido

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testDniAlfabetico() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setIdCuenta("12345");
        cuentaDto.setMoneda("USD");
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setDniTitular("esto es el dni"); // Valor no permitido

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }

    @Test
    public void testDniNegativo() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setIdCuenta("12345");
        cuentaDto.setMoneda("USD");
        cuentaDto.setTipoCuenta("CAJA_AHORRO");
        cuentaDto.setDniTitular("-12345678"); // Valor no permitido

        assertThrows(IllegalArgumentException.class, () -> cuentaValidator.validarCuenta(cuentaDto));
    }
        
    
    


}
