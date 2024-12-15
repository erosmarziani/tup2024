package ar.edu.utn.frbb.tup.controller.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.Dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.exception.TransferenciaException;

@Component
public class TransferenciaValidator {
    public void validarTransferencia(TransferenciaDto transferencia) throws TransferenciaException{
        validarId(transferencia.getIdDestino());
        validarId(transferencia.getIdOrigen());
        validarCuentasDiferentes(transferencia.getIdOrigen(), transferencia.getIdDestino());
        validarFecha(transferencia.getFechaOperacion());
        validarImporte(transferencia.getImporte());
        validarTipoMoneda(transferencia.getTipoMoneda());
    }
    
    private void validarTipoMoneda(String tipoMoneda) throws TransferenciaException {
        //Verifico que el dato ingresado no sea nulo o vacio
        if (tipoMoneda == null || tipoMoneda.isEmpty()) {
            throw new TransferenciaException("El tipo de moneda es obligatorio");
        }
        //Verifico que el tipo de moneda sea valido
        if (!"USD".equals(tipoMoneda) &&!"ARS".equals(tipoMoneda)) {
            throw new TransferenciaException("El tipo de moneda debe ser USD o ARS");
        }
    }

    private void validarImporte(String importe) throws TransferenciaException{
        //Verifico que el dato ingresado no sea nulo o vacio
        if (importe == null || importe.isEmpty()) {
            throw new TransferenciaException("El importe de la transferencia es obligatorio");
        }
        try{
            Double.parseDouble(importe);
            //Verifico que el importe no sea negativo
            if (Double.parseDouble(importe) < 0) {
                throw new TransferenciaException("El importe de la transferencia no puede ser negativo");
            }
        }catch(TransferenciaException e){
            throw new TransferenciaException("El importe de la transferencia debe ser numérico");
        }
    }

    //Validacion de entrada de fecha de operacion en formato yyyy-MM-dd  (Ej: 2022-01-31)
    public void validarFecha(String fechaOperacion) throws TransferenciaException{
        //Verifico que el dato ingresado no sea nulo o vacio
        if (fechaOperacion == null || fechaOperacion.isEmpty()) {
            throw new TransferenciaException("La fecha de operacion es obligatoria");
        }
        try{
            LocalDate.parse(fechaOperacion);
        }catch(Exception e){
            throw new TransferenciaException("Error en el formato de fecha (yyyy-MM-dd)");
        }
    }
    //Validacion de entrada de id
    private void validarId(String idCuenta) throws NumberFormatException, TransferenciaException {
        //Verifico que el dato ingresado no sea nulo o vacio
        if (idCuenta == null || idCuenta.isEmpty()) {
        throw new TransferenciaException("El id de la cuenta es obligatorio");
        }
        try{
            //Si el parseo falla quiere decir que no se ingreso un formato numerico
            Long.parseLong(idCuenta);
        //Veriico que el numero de cuenta no sea negativo
        if (Long.parseLong(idCuenta) < 0) {
            throw new NumberFormatException("El id de la cuenta no puede ser negativo");
        }
        }catch(NumberFormatException e){
            throw new NumberFormatException("El id de la cuenta debe ser numérico");
        }
    }

    private void validarCuentasDiferentes(String idCuentaOrigen, String idCuentaDestino) throws TransferenciaException{
        if (idCuentaOrigen.equals(idCuentaDestino)) {
            throw new TransferenciaException("Las cuentas deben ser distintas");
        }
    }
    
}
