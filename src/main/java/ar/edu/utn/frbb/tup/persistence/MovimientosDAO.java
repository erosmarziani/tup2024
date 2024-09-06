package ar.edu.utn.frbb.tup.persistence;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Movimiento;

public interface MovimientosDAO {
        public void agregarMovimiento(Movimiento movimiento) throws Exception;
        public List<Movimiento> obtenerMovimientoPorCuenta(long idCuenta) throws Exception;
        public boolean eliminarMovimientosPorCuenta(long idCuenta) throws Exception;
        public List<Movimiento> obtenerMovimientos() throws Exception;   
}
