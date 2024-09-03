package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorCuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarLineaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEscribirArchivoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarCuentaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorManejoArchivoException;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.MovimientosDaoImpl;
import ar.edu.utn.frbb.tup.service.exception.CuentaInexistenteException;
import ar.edu.utn.frbb.tup.service.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.service.exception.CuentaSinSaldoException;
import ar.edu.utn.frbb.tup.service.exception.MovimientosVaciosException;
import ar.edu.utn.frbb.tup.model.enums.TipoOperacion;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovimientoService {

    @Autowired
    private MovimientosDaoImpl movimientosDao;

    @Autowired
    private CuentaDaoImpl cuentaDao;

    public Movimiento realizarDeposito(long idCuenta, int monto) throws CuentaInexistenteException,
            ErrorEscribirArchivoException, ErrorArchivoNoEncontradoException, ErrorCuentaNoEncontradaException,
            ErrorGuardarCuentaException, ErrorEliminarLineaException, ErrorManejoArchivoException, IOException {

        Cuenta cuenta = cuentaDao.obtenerCuentaPorId(idCuenta);

        if (cuenta == null) {
            throw new CuentaInexistenteException("El CBU no existe");
        }

        int nuevoBalance = cuenta.getBalance() + monto;
        cuentaDao.actualizarBalance(idCuenta, nuevoBalance);

        Movimiento movimiento = crearMovimiento(idCuenta, LocalDate.now(), nuevoBalance, TipoOperacion.DEBITO);

        movimientosDao.agregarMovimiento(movimiento);

        return movimiento;
    }

    public Movimiento realizarRetiro(long idCuenta, int monto)
            throws CuentaNoEncontradaException, CuentaSinSaldoException, ErrorCuentaNoEncontradaException, ErrorArchivoNoEncontradoException, IOException, ErrorGuardarCuentaException, ErrorEliminarLineaException, ErrorManejoArchivoException, ErrorEscribirArchivoException {
        Cuenta cuenta = cuentaDao.obtenerCuentaPorId(idCuenta);
        if (cuenta == null) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada");
        }
        if (cuenta.getBalance() < monto) {
            throw new CuentaSinSaldoException("Saldo insuficiente");
        }
        int nuevoBalance = cuenta.getBalance() - monto;
        cuentaDao.actualizarBalance(idCuenta, nuevoBalance);

        Movimiento movimiento = crearMovimiento(idCuenta, LocalDate.now(), nuevoBalance, TipoOperacion.CREDITO);

        movimientosDao.agregarMovimiento(movimiento);
        return movimiento;
    }

    public List<Movimiento> obtenerMovimientosPorId(long idCuenta) throws MovimientosVaciosException, ErrorManejoArchivoException {

        List<Movimiento> movimientos = movimientosDao.obtenerMovimientoPorCuenta(idCuenta);
        if (movimientos.isEmpty()) {
            throw new MovimientosVaciosException("No se encontraron movimientos");
        }
        return movimientos;
    }

    public void eliminarMovimientosPorID(long idCuenta) throws ErrorManejoArchivoException, ErrorEliminarLineaException, MovimientosVaciosException {
        boolean movimientoEncontrado = movimientosDao.eliminarMovimientoPorId(idCuenta);
        if (movimientoEncontrado == false) {
            throw new MovimientosVaciosException("No se encontraron movimientos");
        }

    }

    private static Movimiento crearMovimiento(long idCuenta, LocalDate fechaOperacion, int importe,
            TipoOperacion tipoOperacion) {

        Movimiento movimiento = new Movimiento(idCuenta, fechaOperacion, importe, tipoOperacion);
        return movimiento;
    }

}