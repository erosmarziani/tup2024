package ar.edu.utn.frbb.tup.service;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoOperacion;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorCuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.persistence.implementation.ClienteDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.MovimientosDaoImpl;
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
    private ClienteDaoImpl clienteDao;

    public void validacionTransferencia(long idCuentaOrigen, long idCuentaDestino, double monto, TipoMoneda tipoMoneda) throws ErrorCuentaNoEncontradaException, ErrorArchivoNoEncontradoException, IOException, CuentaInexistenteException, TransferenciaRechazadaException {

        Cuenta cuentaOrigen = cuentaDao.obtenerCuentaPorId(idCuentaDestino);
        Cuenta cuentaDestino = cuentaDao.obtenerCuentaPorId(idCuentaDestino);

        // Verifico que las dos cuentas existan
        if (cuentaOrigen == null && cuentaDestino == null) {
            throw new CuentaInexistenteException(
                    "No se ha encontrado la cuenta origen y la cuenta destino en la base de datos");
        } else if (cuentaOrigen == null) {
            throw new CuentaInexistenteException("No se ha encontrado la cuenta origen en la base de datos");
        } else if (cuentaDestino == null) {
            throw new CuentaInexistenteException("No se ha encontrado la cuenta destino en la base de datos");
        }

        Cliente clienteOrigen = clienteDao.obtenerClientePorDNI(cuentaOrigen.getTitular());
        Cliente clienteDestino = clienteDao.obtenerClientePorDNI(cuentaDestino.getTitular());

        // Verificar saldo
        if (cuentaOrigen.getBalance() < monto) {
            throw new IllegalArgumentException("No se puede realizar la transferencia, saldo insuficiente");
        }

        // Si las cuentas no pertenecen al mismo banco se consulta a un servicio que da
        // una respuesta aleatoria
        if (clienteOrigen.getBanco() != clienteDestino.getBanco()) {
            if (banelcoService.transferir() == false) {
                throw new TransferenciaRechazadaException("La transferencia entre los bancos fue rechazada");
            }
        }

        // Verificar que la moneda es la misma en ambas cuentra
        if (cuentaDestino.getMoneda() != cuentaOrigen.getMoneda()) {
            throw new IllegalArgumentException("Las cuentas deben tener la misma moneda");
        }
    }

    private Transferencia realizarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, double monto){
        //Calcular el importe que se acredita y debita, descontando los cargos
        double montoTotal =  calcularCargo(cuentaDestino.getMoneda(), monto);

    //Actualizar el saldo en las cuentas
    cuentaDao.actualizarBalance(cuentaOrigen.getNumeroCuenta(), cuentaOrigen.getBalance() - montoTotal);
    cuentaDao.actualizarBalance(cuentaDestino.getNumeroCuenta(), cuentaDestino.getBalance() + montoTotal);

    long idMovimiento = generarIdMovimiento();
    //Agregar movimiento a la cuenta Origen
       movimientosDao.agregarMovimiento(
        new Movimiento(
            idMovimiento,
            cuentaOrigen.getTitular(),
            LocalDate.now(),
            montoTotal,
            TipoOperacion.DEBITO,
            cuentaOrigen.getMoneda())
       );

       //Agregar movimiento a la cuenta Destino
       movimientosDao.agregarMovimiento(
        new Movimiento(
            idMovimiento,
            cuentaDestino.getTitular(),
            LocalDate.now(),
            montoTotal,
            TipoOperacion.CREDITO,
            cuentaDestino.getMoneda())
       );
        return new Transferencia(cuentaOrigen.getNumeroCuenta(), cuentaDestino.getNumeroCuenta(),LocalDate.now(), montoTotal,cuentaOrigen.getMoneda();
    }

    private long generarIdMovimiento() {
        return (long) (Math.random() * 100000); // Generar un ID único aleatorio para el ejemplo
    }

    private double calcularCargo(TipoMoneda tipoMoneda, double monto) {
        if (tipoMoneda == TipoMoneda.ARS && monto > 1000000) {
            return monto - (monto * 0.02);
        }
        if (tipoMoneda == TipoMoneda.USD && monto > 5000) {
            return monto - (monto * 0.05);
        }
        return 0;
    }
}