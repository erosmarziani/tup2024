package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.Dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enums.*;
import ar.edu.utn.frbb.tup.model.exception.*;
import ar.edu.utn.frbb.tup.persistence.exception.*;
import ar.edu.utn.frbb.tup.persistence.implementation.ClienteDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.service.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CuentaService {

    @Autowired
    private CuentaDaoImpl cuentaDao;

    @Autowired
    private ClienteDaoImpl clienteDao;

    public Cuenta altaCuenta(CuentaDto cuentaDto)
            throws ErrorArchivoNoEncontradoException, ClienteNoEncontradoException, ErrorGuardarCuentaException {
        Cuenta cuenta = new Cuenta(Long.parseLong(cuentaDto.getIdCuenta()), LocalDate.now(), 0,
                TipoCuenta.valueOf(cuentaDto.getTipoCuenta()), Long.parseLong(cuentaDto.getDniTitular()),
                TipoMoneda.valueOf(cuentaDto.getMoneda()));
        Cliente clienteAsociado = clienteDao.obtenerClientePorDNI(cuenta.getTitular());
        if (clienteAsociado == null) {
            throw new ClienteNoEncontradoException("El cliente no ha sido encontrado en la base de datos");
        }
        cuentaDao.guardarCuenta(cuenta);
        return cuenta;

    }

    public Cuenta eliminarCuenta(long idCuenta)
            throws ErrorEliminarLineaException, ErrorManejoArchvivoException, CuentaNoEncontradaException {
        Cuenta cuenta = cuentaDao.eliminarCuenta(idCuenta);

        if (cuenta == null) {
            throw new CuentaNoEncontradaException("La cuenta no ha sido encontrada en la base de datos.");

        }
        // Borrar movimientos y transferencias
        return cuenta;
    }

    public List<Cuenta> obtenerCuentasPorId(Long id)
            throws ErrorArchivoNoEncontradoException, ClienteNoEncontradoException {
        Cliente cliente = clienteDao.obtenerClientePorDNI(id);
        if (cliente == null) {
            throw new ClienteNoEncontradoException("El cliente no ha sido encontrado en la base de datos");
        }

        List<Cuenta> cuentas = cuentaDao.obtenerCuentasDelCliente(id);
        return cuentas;
    }

    public List<Cuenta> obtenerCuentas()
            throws ErrorArchivoNoEncontradoException, ErrorCuentaNoEncontradaException, CuentasInexistentesException {
        List<Cuenta> cuentas = cuentaDao.obtenerCuentas();
        if (cuentas.isEmpty()) {
            throw new CuentasInexistentesException("No se han encontrado cuentas en la base de datos");
        }
        return cuentas;

    }
}
