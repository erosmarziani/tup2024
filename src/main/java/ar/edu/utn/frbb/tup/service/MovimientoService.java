package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorCuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarLineaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEscribirArchivoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarCuentaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorManejoArchivoException;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.MovimientosDaoImpl;
import ar.edu.utn.frbb.tup.service.exception.CuentaInexistenteException;
import ar.edu.utn.frbb.tup.service.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.service.exception.CuentaSinSaldoException;
import ar.edu.utn.frbb.tup.service.exception.MovimientosVaciosException;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
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

    public Movimiento realizarDeposito(long idCuenta, double importe) throws CuentaInexistenteException,
            ErrorEscribirArchivoException, ErrorArchivoNoEncontradoException, ErrorCuentaNoEncontradaException,
            ErrorGuardarCuentaException, ErrorEliminarLineaException, ErrorManejoArchivoException, IOException, ErrorGuardarClienteException {

        Cuenta cuenta = cuentaDao.obtenerCuentaPorId(idCuenta);

        if (cuenta == null) {
            throw new CuentaInexistenteException("El CBU no existe");
        }
        if (importe < 0) {
            throw new IllegalArgumentException("El importe no puede ser negativo");
        }

        double nuevoBalance = cuenta.getBalance() + importe;
        cuentaDao.actualizarBalance(idCuenta, nuevoBalance);

        Movimiento movimiento = crearMovimiento(idCuenta, LocalDate.now(), nuevoBalance, TipoOperacion.DEBITO, cuenta.getMoneda());

        movimientosDao.agregarMovimiento(movimiento);

        return movimiento;
    }

    public Movimiento realizarRetiro(long idCuenta, double importe)
            throws CuentaNoEncontradaException, CuentaSinSaldoException, ErrorCuentaNoEncontradaException, ErrorArchivoNoEncontradoException, IOException, ErrorGuardarCuentaException, ErrorEliminarLineaException, ErrorManejoArchivoException, ErrorEscribirArchivoException, ErrorGuardarClienteException {
        Cuenta cuenta = cuentaDao.obtenerCuentaPorId(idCuenta);
        if (cuenta == null) {
            throw new CuentaNoEncontradaException("Cuenta no encontrada");
        }
        if (cuenta.getBalance() < importe) {
            throw new CuentaSinSaldoException("Saldo insuficiente");
        }
        if (importe < 0) {
            throw new IllegalArgumentException("El importe no puede ser negativo");
        }
        double nuevoBalance = cuenta.getBalance() - importe;
        cuentaDao.actualizarBalance(idCuenta, nuevoBalance);

        Movimiento movimiento = crearMovimiento(idCuenta, LocalDate.now(), nuevoBalance, TipoOperacion.CREDITO,cuenta.getMoneda());

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

    public String eliminarMovimientosPorID(long idCuenta) throws ErrorManejoArchivoException, ErrorEliminarLineaException, MovimientosVaciosException {
        boolean movimientoEncontrado = movimientosDao.eliminarMovimientosPorCuenta(idCuenta);
        if (movimientoEncontrado == false) {
            throw new MovimientosVaciosException("No se encontraron movimientos");
        }
        return "ok: Movimientos de la cuenta" + idCuenta + " eliminados";

    }

    private static Movimiento crearMovimiento(long idCuenta, LocalDate fechaOperacion, double importe,
            TipoOperacion tipoOperacion, TipoMoneda tipoMoneda) {

        Movimiento movimiento = new Movimiento(idCuenta, fechaOperacion, importe, tipoOperacion,tipoMoneda);
        return movimiento;
    }

}