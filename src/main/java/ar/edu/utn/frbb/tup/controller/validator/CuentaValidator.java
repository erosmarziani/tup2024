package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.Dto.CuentaDto;
import org.springframework.stereotype.Component;


@Component
public class CuentaValidator {
    public void validarCuenta(CuentaDto cuentaDto) {

        validarDatos(cuentaDto);
        validarDNI(cuentaDto.getDniTitular());
    }

    private void validarDatos(CuentaDto cuentaDto) {
        if (cuentaDto.getIdCuenta() == null || cuentaDto.getIdCuenta().isEmpty()) {
           throw new IllegalArgumentException("El id de la cuenta es obligatorio"); 
        }
        
        if (cuentaDto.getMoneda() == null || cuentaDto.getMoneda().isEmpty()) {
            throw new IllegalArgumentException("La moneda es obligatoria");
        }

        if (cuentaDto.getTipoCuenta() == null || cuentaDto.getTipoCuenta().isEmpty()) {
            throw new IllegalArgumentException("El tipo de cuenta es obligatorio");
        }
    }


    private void validarDNI(String dni) {
        // Validar que exista el dni antes de parsear
        if (dni == null || dni.isEmpty()) {
            throw new IllegalArgumentException("Eldni del titular es obligatorio");
        }
        try {
            Long.parseLong(dni);
            // Verifico que tenga 8 digitos
            if (dni.length() != 8) {
                throw new IllegalArgumentException("El DNI del titular debe contener 8 digitos");
            }
            // Verifico que no sea un numero de DNI erroneo
            if (dni.equals("00000000")) {
                throw new IllegalArgumentException("El DNI del titular no puede ser 0000000000");
            }
            // Verifico que el DNI no sea un numero negativo
            if (Integer.parseInt(dni) < 0) {
                throw new IllegalArgumentException("El DNI del titular no puede ser negativo");
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El dni del titular debe ser numerico");
        }
        ;
    }
}
