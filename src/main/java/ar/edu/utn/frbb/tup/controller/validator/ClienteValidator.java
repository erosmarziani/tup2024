package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class ClienteValidator {


    public void validarCliente(ClienteDto clienteDto) {
        validarExistenDatos(clienteDto);
        validarDni(clienteDto.getDni());
        validarFechaNacimiento(clienteDto.getFechaNacimiento());
    
    }
    private void validarExistenDatos(ClienteDto clienteDto) {
        //Valido que no haya datos vacios o nulos
        if (clienteDto.getNombre() == null  || clienteDto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }

        if (clienteDto.getApellido() == null || clienteDto.getApellido().isEmpty()) {
            throw new IllegalArgumentException("El apellido del cliente es obligatorio");
        }

        if (clienteDto.getDireccion() == null || clienteDto.getDireccion().isEmpty()) {
            throw new IllegalArgumentException("La dirección del cliente es obligatoria");
        }
        
    }
    public void validarFechaNacimiento(String fechaNacimiento){
        //Verifico que no sea un campo vacio antes de intentar parsea
        if(fechaNacimiento ==null || fechaNacimiento.isEmpty()) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        }
        try{

            //Verifico que el formato sea correcto
            LocalDate fecha =LocalDate.parse(fechaNacimiento);
            
            //Establezco un rango de fecha valida entre el año 1900 y hace 18 años
            LocalDate fechaMinima = LocalDate.of(1900, 1, 1);
            LocalDate fechaMaxima = LocalDate.now().minusYears(18);
            
            //Verificar que como minimo tenga 18 años
            if (fecha.isAfter(fechaMaxima)) {
                throw new IllegalArgumentException("El cliente tiene que ser mayor de edad");
            }
            //Verifico que la fecha de nacimiento no sea anterior a lo permitido
            if (fecha.isBefore(fechaMinima)) {
                throw new IllegalArgumentException("La fecha de nacimiento debe estar entre el 01/01/1900 y hoy");
            }
        }catch(DateTimeParseException e){
            throw new IllegalArgumentException("Error en el formato de fecha (yyyy-MM-dd)");
        }
    }
    public void validarDni(String dni){
        //Verifico que no sea campo vacio antes de parsear
        if (dni == null || dni.isEmpty()    ) {
            throw new IllegalArgumentException("El DNI es obligatorio");
        }
        try {
            //Verifico que el formato ingresado sea correcto
            Long.parseLong(dni);
        
        //Verifico que tenga 8 digitos
        if (dni.length() != 8) {
            throw new IllegalArgumentException("El DNI debe contener 8 digitos");
        }
        //Verifico que no sea un numero de DNI erroneo
        if (dni.equals("00000000")) {
            throw new IllegalArgumentException("El DNI no puede ser 0000000000");
        }
        //Verifico que el DNI no sea un numero negativo
        if (Integer.parseInt(dni)<0) {
            throw new IllegalArgumentException("El DNI no puede ser negativo");
        }

    }   catch (NumberFormatException e){
        throw new IllegalArgumentException("El DNI debe ser numérico");
    }

}

}