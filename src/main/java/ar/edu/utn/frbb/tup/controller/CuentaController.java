package ar.edu.utn.frbb.tup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.controller.Dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.Dto.MovimientosResponseDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;
import ar.edu.utn.frbb.tup.service.CuentaService;
import ar.edu.utn.frbb.tup.service.exception.ClienteServiceException;
import ar.edu.utn.frbb.tup.service.exception.CuentaServiceException;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {
    
    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    @GetMapping
    public ResponseEntity<List<Cuenta>> getCuentas() throws ErrorArchivoException, CuentaServiceException {
        return ResponseEntity.ok(cuentaService.obtenerCuentas());
    }

    @GetMapping("/{dni}")
    public ResponseEntity<List<Cuenta>> getCuentaPorIdCliente(@PathVariable Long dni) throws  ErrorArchivoException, ClienteServiceException{
        return ResponseEntity.ok(cuentaService.obtenerCuentasPorIdCliente(dni));
    }

    @PostMapping
    public ResponseEntity<Cuenta> agregarCuenta(@RequestBody CuentaDto cuentaDto) throws ErrorArchivoException, ClienteServiceException, CuentaServiceException{
        cuentaValidator.validarCuenta(cuentaDto);
        return ResponseEntity.ok(cuentaService.altaCuenta(cuentaDto));
    }
    @GetMapping("/movimientos/{idCuenta}")
    public ResponseEntity<MovimientosResponseDto> obtenerTransacciones(@PathVariable long idCuenta) throws ErrorArchivoException, CuentaServiceException{
        MovimientosResponseDto response = cuentaService.obtenerMovimientos(idCuenta);
        return ResponseEntity.ok(response);
    }

}
