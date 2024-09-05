package ar.edu.utn.frbb.tup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.controller.Dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorActualizarCuentaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorCuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarCuentaException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.exception.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.service.exception.CuentasInexistentesException;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {
    
    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    @GetMapping
    public ResponseEntity<List<Cuenta>> getCuentas() throws ErrorArchivoNoEncontradoException, ErrorCuentaNoEncontradaException, CuentasInexistentesException{
        return ResponseEntity.ok(cuentaService.obtenerCuentas());
    }

    @GetMapping("/{dni}")
    public ResponseEntity<List<Cuenta>> getCuentaPorIdCliente(@PathVariable Long dni) throws ErrorArchivoNoEncontradoException, ClienteNoEncontradoException{
        return ResponseEntity.ok(cuentaService.obtenerCuentasPorIdCliente(dni));
    }

    @PostMapping
    public ResponseEntity<Cuenta> agregarCuenta(@RequestBody CuentaDto cuentaDto) throws ErrorArchivoNoEncontradoException, ClienteNoEncontradoException, ErrorGuardarCuentaException{
        cuentaValidator.validarCuenta(cuentaDto);
        return ResponseEntity.ok(cuentaService.altaCuenta(cuentaDto));
    }

    @PutMapping("/modificar")
    public ResponseEntity<Cuenta> modificarCuenta(@RequestBody CuentaDto cuentaDto) throws ErrorArchivoNoEncontradoException, ClienteNoEncontradoException, ErrorCuentaNoEncontradaException, ErrorGuardarCuentaException, CuentaNoEncontradaException, ErrorActualizarCuentaException{
        cuentaValidator.validarCuenta(cuentaDto);
        return ResponseEntity.ok(cuentaService.actualizarCuenta(cuentaDto));
    } 


}
