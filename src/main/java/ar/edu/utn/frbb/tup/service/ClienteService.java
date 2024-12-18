package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.service.exception.*;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;
import ar.edu.utn.frbb.tup.persistence.implementation.ClienteDaoImpl;
import ar.edu.utn.frbb.tup.persistence.implementation.CuentaDaoImpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteDaoImpl clienteDao;

    @Autowired
    private CuentaDaoImpl cuentaDao;    

    public ClienteService(ClienteDaoImpl clienteDao, CuentaDaoImpl cuentaDao) {
        this.clienteDao = clienteDao;
        this.cuentaDao = cuentaDao;
    }
    
    public Cliente darDeAltaCliente(ClienteDto clienteDto) throws ErrorArchivoException, ClienteServiceException {
        Cliente cliente = new Cliente(clienteDto);

        Cliente clienteExistente = clienteDao.obtenerClientePorDNI(cliente.getDni());
        if (clienteExistente != null) {
            throw new ClienteServiceException("Ya existe un cliente con DNI " + cliente.getDni());
        }
       
        if (cliente.getEdad() < 18) {
            throw new ClienteServiceException("El cliente debe ser mayor a 18 años");
        }

        clienteDao.guardarCliente(cliente);
        return cliente;
    }
    
    public Cliente eliminarCliente(long dni) throws ErrorArchivoException, ClienteServiceException{
        //Verificar si el cliente existe
        Cliente cliente = clienteDao.obtenerClientePorDNI(dni);

        if (cliente == null) {
            throw new ClienteServiceException("El cliente no ha sido encontrado en la base de datos");
        }

        List<Cuenta> cuentas = cuentaDao.obtenerCuentasDelCliente(dni);
        if (cuentas == null) {
            throw new ClienteServiceException("La cuenta no ha sido encontrada en la base de datos");
        }

        for (Cuenta cuenta : cuentas) {
            cuentaDao.eliminarCuenta(cuenta.getNumeroCuenta());
        }
       
        clienteDao.eliminarCliente(dni);

        return cliente;

    }

    public Cliente modificarCliente(ClienteDto clienteDto) throws ErrorArchivoException {
        Cliente clienteModificado = new Cliente(clienteDto);
        Cliente  clienteActualizado = clienteDao.actualizarCliente(clienteModificado);
        if (clienteActualizado == null) {
            throw new ErrorArchivoException("El cliente no ha sido encontrado en la base de datos");
        }
        return clienteActualizado;
    }



    public Cliente obtenerCliente(long dni) throws ErrorArchivoException{
        Cliente cliente = clienteDao.obtenerClientePorDNI(dni);
        
        return cliente;

    }
    public List<Cliente> mostrarListaCliente() throws ErrorArchivoException {
        List<Cliente> listaClientes = clienteDao.obtenerListaClientes();
        if (listaClientes.isEmpty()) {
            throw new ErrorArchivoException("No se encontraron clientes en la base de datos");
        }
        return listaClientes;

    }
}
