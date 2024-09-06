/*package ar.edu.utn.frbb.tup.service;

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

    public void validacionTransferencia(long idCuentaOrigen, long idCuentaDestino, double importe, TipoMoneda tipoMoneda) throws ErrorCuentaNoEncontradaException, ErrorArchivoNoEncontradoException, IOException, CuentaInexistenteException, TransferenciaRechazadaException {

        Cuenta cuentaOrigen = cuentaDao.obtenerCuentaPorId(idCuentaOrigen);
        Cuenta cuentaDestino = cuentaDao.obtenerCuentaPorId(idCuentaDestino);

        // Verifico que las dos cuentas existan
        if (cuentaOrigen == null && cuentaDestino == null) {
            throw new CuentaInexistenteException("No se ha encontrado la cuenta origen y la cuenta destino en la base de datos");
        } else if (cuentaOrigen == null) {
            throw new CuentaInexistenteException("No se ha encontrado la cuenta origen en la base de datos");
        } else if (cuentaDestino == null) {
            if (banelcoService.transferir() == false) {
                throw new TransferenciaRechazadaException("La transferencia entre los bancos fue rechazada");
            }
            e
        }

        Cliente clienteOrigen = clienteDao.obtenerClientePorDNI(cuentaOrigen.getTitular());
        Cliente clienteDestino = clienteDao.obtenerClientePorDNI(cuentaDestino.getTitular());

        // Verificar saldo
        if (cuentaOrigen.getBalance() < importe) {
            throw new IllegalArgumentException("No se puede realizar la transferencia, saldo insuficiente");
        }

        // Si las cuentas no pertenecen al mismo banco se consulta a un servicio que da una respuesta aleatoria
        
        // Verificar que la moneda es la misma en ambas cuentra
        if (cuentaDestino.getMoneda() != cuentaOrigen.getMoneda()) {
            throw new IllegalArgumentException("Las cuentas deben tener la misma moneda");
        }
    }

    private Transferencia transfDistintosBancos(Cuenta cuentaOrigen, double importe){
        //Calcular el importe que se acredita y debita, descontando los cargos
        double importeTotal =  calcularCargo(cuentaDestino.getMoneda(), importe);

         //Actualizar el saldo solo en la cuenta de origen que es la que se encuentra en el banco
    cuentaDao.actualizarBalance(cuentaOrigen.getNumeroCuenta(), cuentaOrigen.getBalance() - importeTotal);

    long idMovimiento = generarIdMovimiento();
    //Agregar movimiento a la cuenta Origen
       movimientosDao.agregarMovimiento(
        new Movimiento(
            idMovimiento,
            cuentaOrigen.getTitular(),
            LocalDate.now(),
            importeTotal,
            TipoOperacion.DEBITO,
            cuentaOrigen.getMoneda())
       );

}

    private Transferencia realizarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, double importe){
        //Calcular el importe que se acredita y debita, descontando los cargos
        double importeTotal =  calcularCargo(cuentaDestino.getMoneda(), importe);

    //Actualizar el saldo en las cuentas
    cuentaDao.actualizarBalance(cuentaOrigen.getNumeroCuenta(), cuentaOrigen.getBalance() - importeTotal);
    cuentaDao.actualizarBalance(cuentaDestino.getNumeroCuenta(), cuentaDestino.getBalance() + importeTotal);

    long idMovimiento = generarIdMovimiento();
    //Agregar movimiento a la cuenta Origen
       movimientosDao.agregarMovimiento(
        new Movimiento(
            idMovimiento,
            cuentaOrigen.getTitular(),
            LocalDate.now(),
            importeTotal,
            TipoOperacion.DEBITO,
            cuentaOrigen.getMoneda())
       );

       //Agregar movimiento a la cuenta Destino
       movimientosDao.agregarMovimiento(
        new Movimiento(
            idMovimiento,
            cuentaDestino.getTitular(),
            LocalDate.now()x,
            importeTotal,
            TipoOperacion.CREDITO,
            cuentaDestino.getMoneda())
       );
        return new Transferencia(cuentaOrigen.getNumeroCuenta(), cuentaDestino.getNumeroCuenta(),LocalDate.now(), importeTotal,cuentaOrigen.getMoneda();
    }

    private long generarIdMovimiento() {
        return (long) (Math.random() * 100000); // Generar un ID único aleatorio para el ejemplo
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

*/