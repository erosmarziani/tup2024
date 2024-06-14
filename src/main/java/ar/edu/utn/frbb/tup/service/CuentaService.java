package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaNoSportadaException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CuentaService {
    CuentaDao cuentaDao = new CuentaDao();

    @Autowired
    ClienteService clienteService;

    //Generar casos de test para darDeAltaCuenta
    //    1 - cuenta existente
    //    2 - cuenta no soportada
    //    3 - cliente ya tiene cuenta de ese tipo
    //    4 - cuenta creada exitosamente
    public void darDeAltaCuenta(Cuenta cuenta, long dniTitular) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, TipoCuentaNoSportadaException {
        if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }
    
        // Chequear cuentas soportadas por el banco
        if (!tipoCuentaEstaSoportada(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaNoSportadaException("El tipo de cuenta no es soportado.");
        }
    
        clienteService.agregarCuenta(cuenta, dniTitular);
        cuentaDao.save(cuenta);
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
        
    }

    public boolean tipoCuentaEstaSoportada(TipoCuenta tipoCuenta,TipoMoneda tipoMoneda){
        return (tipoCuenta == TipoCuenta.CAJA_AHORRO && tipoMoneda == TipoMoneda.PESOS) || 
        (tipoCuenta == TipoCuenta.CUENTA_CORRIENTE && tipoMoneda == TipoMoneda.PESOS) || 
        (tipoCuenta == TipoCuenta.CAJA_AHORRO && tipoMoneda ==  TipoMoneda.DOLARES);
    }

    
}
