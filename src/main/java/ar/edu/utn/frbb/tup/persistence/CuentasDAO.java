package ar.edu.utn.frbb.tup.persistence;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Cuenta;

public interface CuentasDAO {
    void guardarCuenta(Cuenta cuenta) throws Exception;
    Cuenta obtenerCuentaPorId(long idCuenta) throws Exception;
    List<Cuenta> obtenerCuentasDelCliente(long idCliente) throws Exception;
    List<Cuenta> obtenerCuentas() throws Exception;
    Cuenta eliminarCuenta(long idCuenta) throws Exception;
    Cuenta actualizarCuenta(Cuenta cuenta) throws Exception;
    Cuenta actualizarBalance(long numeroCuenta, double nuevoBalance) throws Exception;
    }
