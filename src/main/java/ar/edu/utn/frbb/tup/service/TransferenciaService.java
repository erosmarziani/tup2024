package ar.edu.utn.frbb.tup.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ar.edu.utn.frbb.tup.controller.Dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.controller.exception.TransferenciaException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoMovimiento;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.MovimientosDaoImpl;
import ar.edu.utn.frbb.tup.service.exception.CuentaServiceException;
import ar.edu.utn.frbb.tup.service.exception.TransferenciaServiceException;

@Service
public class TransferenciaService {

    @Autowired
    private MovimientosDaoImpl movimientosDao;

    @Autowired
    private CuentaDaoImpl cuentaDao;

    @Autowired
    private BanelcoService banelcoService;

    public Transferencia validacionTransferencia(TransferenciaDto transferenciaDto) throws ErrorArchivoException, CuentaServiceException, TransferenciaServiceException, TransferenciaException{

        Transferencia transferencia = new Transferencia(transferenciaDto);

        Cuenta cuentaOrigen = validarCuentaOrigen(transferencia.getIdOrigen());
        Cuenta cuentaDestino = validarCuentaDestino(transferencia.getIdDestino());
        // Verificar saldo
        validarSaldo(cuentaOrigen,transferencia.getImporte());

       if (cuentaDestino == null) {
            transferenciaDistintosBancos(cuentaOrigen, transferencia.getImporte());
       }else{
            transferenciaMismoBanco(cuentaOrigen, cuentaDestino, transferencia.getImporte());
       }
       return transferencia;
       
    }

    private Cuenta validarCuentaOrigen(long idCuentaOrigen) throws ErrorArchivoException, CuentaServiceException{
        Cuenta cuentaOrigen = cuentaDao.obtenerCuentaPorId(idCuentaOrigen);
        if(cuentaOrigen == null){
            throw new CuentaServiceException("No se ha encontrado la cuenta origen en la base de datos");
        }
        return cuentaOrigen;
    }

    private Cuenta validarCuentaDestino(long idCuentaDestino) throws TransferenciaServiceException, ErrorArchivoException {
        Cuenta cuentaDestino = cuentaDao.obtenerCuentaPorId(idCuentaDestino);
        if(cuentaDestino == null){
            if (!banelcoService.transferir()) {
                
                throw new TransferenciaServiceException("La transferencia entre los bancos ha sido rechazada");
            }
        }
        return cuentaDestino;
    }

    private void validarSaldo(Cuenta cuenta, double importe) throws TransferenciaException{
        if (cuenta.getBalance() < importe) {
            throw new TransferenciaException("No se puede realizar la transferencia, saldo insuficiente");
        }
    }
    private void transferenciaDistintosBancos(Cuenta cuentaOrigen, double importe) throws ErrorArchivoException {
        // Calcular el importe que se acredita y debita, descontando los cargos
        double importeTotal = calcularCargo(cuentaOrigen.getMoneda(), importe);

        // Actualizar el saldo solo en la cuenta de origen que es la que se encuentra en
        // el banco
        cuentaDao.actualizarBalance(cuentaOrigen.getNumeroCuenta(), cuentaOrigen.getBalance() - importeTotal);

        // Agregar movimiento a la cuenta Origen
        Movimiento movimientoOrigen = new Movimiento();
            movimientoOrigen.setFechaOperacion(LocalDate.now());
            movimientoOrigen.setTipo(TipoMovimiento.DEBITO);
            movimientoOrigen.setDescripcion("Transferencia realizada a otro banco");
            movimientoOrigen.setMonto(importeTotal);
            movimientosDao.guardarMovimiento(movimientoOrigen, cuentaOrigen.getNumeroCuenta());
        
    }

    private void transferenciaMismoBanco(Cuenta cuentaOrigen, Cuenta cuentaDestino, double importe) throws ErrorArchivoException, CuentaServiceException{
        // Calcular el importe que se acredita y debita, descontando los cargos
        double importeTotal = calcularCargo(cuentaDestino.getMoneda(), importe);

        // Actualizar el saldo en las cuentas
        cuentaDao.actualizarBalance(cuentaOrigen.getNumeroCuenta(), cuentaOrigen.getBalance() - importeTotal);
        cuentaDao.actualizarBalance(cuentaDestino.getNumeroCuenta(), cuentaDestino.getBalance() + importeTotal);

         // Verificar que la moneda es la misma en ambas cuentra
         if (cuentaDestino.getMoneda() != cuentaOrigen.getMoneda()) {
            throw new CuentaServiceException("Las cuentas deben tener la misma moneda");
        }

        // Agregar movimiento a la cuenta Origen
        agregarMovimiento(cuentaOrigen, importeTotal, true);
        //Agregar movimiento a la cuenta Destino
        agregarMovimiento(cuentaDestino, importeTotal, false);

    }

    private void agregarMovimiento(Cuenta cuenta, double importe, boolean esDebito) throws ErrorArchivoException{
        Movimiento movimiento = new Movimiento();
        movimiento.setFechaOperacion(LocalDate.now());
        movimiento.setTipo(esDebito ? TipoMovimiento.DEBITO : TipoMovimiento.CREDITO);
        movimiento.setDescripcion(esDebito ? "Transferencia realizada" : "Transferencia recibida");
        movimiento.setMonto(importe);
        
        movimientosDao.guardarMovimiento(movimiento,cuenta.getNumeroCuenta());
        
    }
    private double calcularCargo(TipoMoneda tipoMoneda, double importe) {
        double cargo = 0;
        if (tipoMoneda == TipoMoneda.ARS && importe > 1000000) {
            cargo = 0.2;
        }
        if (tipoMoneda == TipoMoneda.USD && importe > 5000) {
            cargo = 0.05;
        }
        if (cargo > 0){
            System.out.println("Cargo aplicado: " + cargo * importe);
        }
        return importe - (importe * cargo);
    }

}
