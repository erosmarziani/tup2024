package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorActualizarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarLineaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorManejoArchivoException;
import ar.edu.utn.frbb.tup.persistence.implementation.ClienteDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;
import ar.edu.utn.frbb.tup.service.exception.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.service.exception.CuentaNoEncontradaException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteDaoImpl clienteDao;

    @Autowired
    private CuentaDaoImpl cuentaDao;    

    public ClienteService(ClienteDaoImpl clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Cliente darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException, ClienteMenorDeEdadException, ErrorArchivoNoEncontradoException, ErrorGuardarClienteException {
        Cliente cliente = new Cliente(clienteDto);

        Cliente clienteEcontrado = clienteDao.obtenerClientePorDNI(cliente.getDni());
        if (clienteEcontrado.getDni() == Long.parseLong(clienteDto.getDni())) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        }

        if (cliente.getEdad() < 18) {
            throw new ClienteMenorDeEdadException("El cliente debe ser mayor a 18 años");
        }

        clienteDao.guardarCliente(cliente);
        return cliente;
    }
    
    public Cliente eliminarCliente(long dni) throws ClienteNoEncontradoException, ErrorArchivoNoEncontradoException, ErrorEliminarLineaException,ErrorManejoArchivoException, CuentaNoEncontradaException{
        //Verificar si el cliente existe
        Cliente cliente = clienteDao.obtenerClientePorDNI(dni);

        if (cliente == null) {
            throw new ClienteNoEncontradoException("El cliente no ha sido encontrado en la base de datos");
        }

        List<Cuenta> cuentas = cuentaDao.obtenerCuentasDelCliente(dni);
        if (cuentas == null) {
            throw new CuentaNoEncontradaException("La cuenta no ha sido encontrada en la base de datos");
        }

        for (Cuenta cuenta : cuentas) {
            cuentaDao.eliminarCuenta(cuenta.getNumeroCuenta());
        }
       
        clienteDao.eliminarCliente(dni);
        cuentaDao.eliminarCuenta(dni);

        return cliente;
        
        //borrar Transferencias

        
    }

    public Cliente modificarCliente(ClienteDto clienteDto) throws ErrorGuardarClienteException, ErrorActualizarClienteException, ClienteNoEncontradoException, ErrorArchivoNoEncontradoException,  ErrorEliminarLineaException, ErrorManejoArchivoException {
        Cliente clienteModificado = new Cliente(clienteDto);
        Cliente  clienteActualizado = clienteDao.actualizarCliente(clienteModificado);
        if (clienteActualizado == null) {
            throw new ClienteNoEncontradoException("El cliente no ha sido encontrado en la base de datos");
        }
        return clienteActualizado;
    }



    public Cliente obtenerCliente(long dni) throws ErrorArchivoNoEncontradoException, ClienteNoEncontradoException{
        Cliente cliente = clienteDao.obtenerClientePorDNI(dni);
        
        return cliente;

    }
    public List<Cliente> mostrarListaCliente() throws ErrorArchivoNoEncontradoException,ClienteNoEncontradoException {
        List<Cliente> listaClientes = clienteDao.obtenerListaClientes();
        if (listaClientes.isEmpty()) {
            throw new ClienteNoEncontradoException("No se encontraron clientes en la base de datos");
        }
        return listaClientes;

    }
}
