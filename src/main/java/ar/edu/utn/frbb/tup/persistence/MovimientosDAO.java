package ar.edu.utn.frbb.tup.persistence;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Movimiento;

public interface MovimientosDAO {
        public void guardarMovimiento(Movimiento movimiento, long numeroCuenta) throws Exception;
        public List<Movimiento> obtenerMovimientoPorCuenta(long idCuenta) throws Exception;
}
