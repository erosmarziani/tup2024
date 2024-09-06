package ar.edu.utn.frbb.tup.controller;

import java.io.IOException;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorCuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarLineaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEscribirArchivoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarCuentaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorManejoArchivoException;
import ar.edu.utn.frbb.tup.service.MovimientoService;
import ar.edu.utn.frbb.tup.service.exception.CuentaInexistenteException;
import ar.edu.utn.frbb.tup.service.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.service.exception.CuentaSinSaldoException;
import ar.edu.utn.frbb.tup.service.exception.MovimientosVaciosException;


@RestController
@RequestMapping("/movimientos")
public class MovimientosController {
    
    @Autowired
    private MovimientoService movimientoService;

    @GetMapping("/{idCuenta}")
    public ResponseEntity<List<Movimiento>> getCuentas(@PathVariable long idCuenta) throws MovimientosVaciosException, ErrorManejoArchivoException {
        return ResponseEntity.ok(movimientoService.obtenerMovimientosPorId(idCuenta));
    }

    @PostMapping("/deposito/{idCuenta}")
    public ResponseEntity<Movimiento> realizarDeposito(@PathVariable long idCuenta, @RequestParam double importe) throws CuentaInexistenteException, ErrorEscribirArchivoException, ErrorArchivoNoEncontradoException, ErrorCuentaNoEncontradaException, ErrorGuardarCuentaException, ErrorEliminarLineaException, ErrorManejoArchivoException, IOException, ErrorGuardarClienteException{
        return new ResponseEntity<>(movimientoService.realizarDeposito(idCuenta, importe), HttpStatus.OK);
    }

    @PostMapping("/retiro/{idCuenta}")
    public ResponseEntity<Movimiento> realizarRetiro(@PathVariable long idCuenta, @RequestParam double importe) throws CuentaNoEncontradaException, CuentaSinSaldoException, ErrorCuentaNoEncontradaException, ErrorArchivoNoEncontradoException, IOException, ErrorGuardarCuentaException, ErrorEliminarLineaException, ErrorManejoArchivoException, ErrorEscribirArchivoException, ErrorGuardarClienteException  {
        return new ResponseEntity<>(movimientoService.realizarRetiro(idCuenta, importe),HttpStatus.OK);
    }
    @DeleteMapping("/eliminar/{idCuenta}")
    public ResponseEntity<String> eliminarMovimientos(@PathVariable long idCuenta) throws ErrorManejoArchivoException, ErrorEliminarLineaException, MovimientosVaciosException{
        return ResponseEntity.ok(movimientoService.eliminarMovimientosPorID(idCuenta));
    }
}
