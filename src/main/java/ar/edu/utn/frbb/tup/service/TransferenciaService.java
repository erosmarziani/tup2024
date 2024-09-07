package ar.edu.utn.frbb.tup.service;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import ar.edu.utn.frbb.tup.controller.Dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoOperacion;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorCuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarLineaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEscribirArchivoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarCuentaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorManejoArchivoException;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.MovimientosDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.TransferenciasDaoImpl;
import ar.edu.utn.frbb.tup.service.exception.CuentaInexistenteException;
import ar.edu.utn.frbb.tup.service.exception.TransferenciaRechazadaException;

public class TransferenciaService {

    @Autowired
    private MovimientosDaoImpl movimientosDao;

    @Autowired
    private CuentaDaoImpl cuentaDao;

    @Autowired
    private BanelcoService banelcoService;

    @Autowired
    private TransferenciasDaoImpl transferenciasDao;;

    public Transferencia validacionTransferencia(TransferenciaDto transferenciaDto)
            throws ErrorCuentaNoEncontradaException, ErrorArchivoNoEncontradoException, IOException,
            CuentaInexistenteException, TransferenciaRechazadaException, ErrorGuardarCuentaException,
            ErrorEliminarLineaException, ErrorManejoArchivoException, ErrorGuardarClienteException, ErrorEscribirArchivoException {

        Transferencia transferencia = new Transferencia(transferenciaDto);
        boolean existeDestino = true;

        Cuenta cuentaOrigen = cuentaDao.obtenerCuentaPorId(transferencia.getIdOrigen());
        Cuenta cuentaDestino = cuentaDao.obtenerCuentaPorId(transferencia.getIdDestino());

        // Verifico que las dos cuentas existan
        if (cuentaOrigen == null && cuentaDestino == null) {
            throw new CuentaInexistenteException(
                    "No se ha encontrado la cuenta origen y la cuenta destino en la base de datos");
        } else if (cuentaOrigen == null) {
            throw new CuentaInexistenteException("No se ha encontrado la cuenta origen en la base de datos");
            // Si la cuenta origen no pertenece al banco se consulta a un servicio que da
            // una respuesta aleatoria
        } else if (cuentaDestino == null) {
            if (banelcoService.transferir() == false) {
                throw new TransferenciaRechazadaException("La transferencia entre los bancos fue rechazada");
            } else {
                existeDestino = false;
            }
        }
        // Verificar saldo
        if (cuentaOrigen.getBalance() < transferencia.getImporte()) {
            throw new IllegalArgumentException("No se puede realizar la transferencia, saldo insuficiente");
        }
        // Verificar que la moneda es la misma en ambas cuentra
        if (cuentaDestino.getMoneda() != cuentaOrigen.getMoneda()) {
            throw new IllegalArgumentException("Las cuentas deben tener la misma moneda");
        }
       if (!existeDestino) {
            transferenciaDistintosBancos(cuentaOrigen, transferencia.getImporte());
       }else{
            transferenciaMismoBanco(cuentaOrigen, cuentaDestino, transferencia.getImporte());
       }
       transferenciasDao.agregarTransferencias(transferencia);
       return transferencia;
       
    }

    private void transferenciaDistintosBancos(Cuenta cuentaOrigen, double importe) throws ErrorArchivoNoEncontradoException, ErrorCuentaNoEncontradaException, ErrorGuardarCuentaException, ErrorEliminarLineaException, ErrorManejoArchivoException, ErrorGuardarClienteException {
        // Calcular el importe que se acredita y debita, descontando los cargos
        double importeTotal = calcularCargo(cuentaOrigen.getMoneda(), importe);

        // Actualizar el saldo solo en la cuenta de origen que es la que se encuentra en
        // el banco
        cuentaDao.actualizarBalance(cuentaOrigen.getNumeroCuenta(), cuentaOrigen.getBalance() - importeTotal);

        // Agregar movimiento a la cuenta Origen
        movimientosDao.agregarMovimiento(
                new Movimiento(
                        cuentaOrigen.getTitular(),
                        LocalDate.now(),
                        importeTotal,
                        TipoOperacion.DEBITO,
                        cuentaOrigen.getMoneda()));
    }

    private void transferenciaMismoBanco(Cuenta cuentaOrigen, Cuenta cuentaDestino, double importe)
            throws ErrorArchivoNoEncontradoException, ErrorCuentaNoEncontradaException, ErrorGuardarCuentaException,
            ErrorEliminarLineaException, ErrorManejoArchivoException, ErrorGuardarClienteException {
        // Calcular el importe que se acredita y debita, descontando los cargos
        double importeTotal = calcularCargo(cuentaDestino.getMoneda(), importe);

        // Actualizar el saldo en las cuentas
        cuentaDao.actualizarBalance(cuentaOrigen.getNumeroCuenta(), cuentaOrigen.getBalance() - importeTotal);
        cuentaDao.actualizarBalance(cuentaDestino.getNumeroCuenta(), cuentaDestino.getBalance() + importeTotal);

        // Agregar movimiento a la cuenta Origen
        movimientosDao.agregarMovimiento(
                new Movimiento(
                        cuentaOrigen.getTitular(),
                        LocalDate.now(),
                        importeTotal,
                        TipoOperacion.DEBITO,
                        cuentaOrigen.getMoneda()));

        // Agregar movimiento a la cuenta Destino
        movimientosDao.agregarMovimiento(
                new Movimiento(
                        cuentaDestino.getTitular(),
                        LocalDate.now(),
                        importeTotal,
                        TipoOperacion.CREDITO,
                        cuentaDestino.getMoneda()));
    }

    private double calcularCargo(TipoMoneda tipoMoneda, double importe) {
        if (tipoMoneda == TipoMoneda.ARS && importe > 1000000) {
            return importe - (importe * 0.02);
        }
        if (tipoMoneda == TipoMoneda.USD && importe > 5000) {
            return importe - (importe * 0.05);
        }
        return 0;
    }
}
