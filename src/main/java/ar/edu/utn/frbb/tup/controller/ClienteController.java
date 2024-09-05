package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorActualizarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarLineaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorManejoArchivoException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.service.exception.CuentaNoEncontradaException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteValidator clienteValidator;


    @GetMapping
    public ResponseEntity<List<Cliente>> getClientes() throws ErrorArchivoNoEncontradoException, ClienteNoEncontradoException{
        return ResponseEntity.ok(clienteService.mostrarListaCliente());
    }
    
    @GetMapping("/{dni}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable long dni) throws ErrorArchivoNoEncontradoException, ClienteNoEncontradoException{
        return ResponseEntity.ok(clienteService.obtenerCliente(dni));
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody ClienteDto clienteDto) throws ClienteAlreadyExistsException, ClienteMenorDeEdadException, ErrorArchivoNoEncontradoException, ErrorGuardarClienteException {
        clienteValidator.validarCliente(clienteDto);
        return ResponseEntity.ok(clienteService.darDeAltaCliente(clienteDto));
    }

    @PutMapping("/modificar/{dni}")
    public ResponseEntity<Cliente> modificarCliente(@RequestBody ClienteDto clienteDto) throws ErrorGuardarClienteException, ErrorActualizarClienteException, ClienteNoEncontradoException, ErrorArchivoNoEncontradoException, ErrorEliminarLineaException, ErrorManejoArchivoException  {
        clienteValidator.validarCliente(clienteDto);
        return ResponseEntity.ok(clienteService.modificarCliente(clienteDto));
    }

    @DeleteMapping("/eliminar/{dni}")
    public ResponseEntity<Cliente> deleteCliente(@PathVariable long dni) throws ClienteNoEncontradoException, ErrorArchivoNoEncontradoException, ErrorEliminarLineaException, ErrorManejoArchivoException, CuentaNoEncontradaException{
        return  ResponseEntity.ok(clienteService.eliminarCliente(dni));
    }


}
