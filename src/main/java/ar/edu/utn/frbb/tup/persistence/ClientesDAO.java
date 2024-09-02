package ar.edu.utn.frbb.tup.persistence;

import java.util.List;

import ar.edu.utn.frbb.tup.model.Cliente;

public interface ClientesDAO{
    void guardarCliente(Cliente cliente) throws Exception;
    Cliente obtenerClientePorDNI(long dni) throws Exception;
    List<Cliente> obtenerListaClientes() throws Exception;
    void eliminarCliente(Long dni) throws Exception;
    Cliente actualizarCliente(Cliente cliente) throws Exception;
    
}