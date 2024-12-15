package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;
import ar.edu.utn.frbb.tup.persistence.exception.GuardadoException;
import ar.edu.utn.frbb.tup.service.ClienteService;
import ar.edu.utn.frbb.tup.service.exception.ClienteServiceException;

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


    @GetMapping("/{dni}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable long dni) throws ErrorArchivoException  {
        return ResponseEntity.ok(clienteService.obtenerCliente(dni));
    }
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody ClienteDto clienteDto) throws GuardadoException, ErrorArchivoException, ClienteServiceException{
        clienteValidator.validarCliente(clienteDto);
        return ResponseEntity.ok(clienteService.darDeAltaCliente(clienteDto));
    }
    @GetMapping
    public ResponseEntity<List<Cliente>> getClientes() throws ErrorArchivoException{
        return ResponseEntity.ok(clienteService.mostrarListaCliente());
    }
    
    @PutMapping("/modificar/{dni}")
    public ResponseEntity<Cliente> modificarCliente(@RequestBody ClienteDto clienteDto) throws ErrorArchivoException   {
        clienteValidator.validarCliente(clienteDto);
        return ResponseEntity.ok(clienteService.modificarCliente(clienteDto));
    }

    @DeleteMapping("/eliminar/{dni}")
    public ResponseEntity<Cliente> deleteCliente(@PathVariable long dni) throws ErrorArchivoException, ClienteServiceException {
        return  ResponseEntity.ok(clienteService.eliminarCliente(dni));
    }


}
