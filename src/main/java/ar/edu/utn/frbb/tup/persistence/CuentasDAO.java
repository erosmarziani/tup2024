package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Cuenta;

public interface CuentasDAO {
    void guardarCuenta(Cuenta cuenta) throws Exception;
    Cuenta obtenerCuentaPorId(long idCuenta) throws Exception;
    void eliminarCuenta(Cuenta cuenta) throws Exception;
    void actualizarCuenta(Cuenta cuenta) throws Exception;
}
