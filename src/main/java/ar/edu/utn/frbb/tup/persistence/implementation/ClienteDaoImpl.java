package ar.edu.utn.frbb.tup.persistence.implementation;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.enums.*;
import ar.edu.utn.frbb.tup.persistence.ClientesDAO;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarLineaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorManejoArchvivoException;
import ar.edu.utn.frbb.tup.persistence.exception.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorActualizarClienteException;


public class ClienteDaoImpl implements ClientesDAO{

    private static final String FILE_PATH = "src" + File.separator + "main" + File.separator + "java" + File.separator + "ar" + File.separator + "edu" + File.separator + "utn" + File.separator + "frbb" + File.separator + "tup" + File.separator + "persistence" + File.separator + "resources" + File.separator + "clientes.txt";
   
   
    @Override
    public void guardarCliente(Cliente cliente) throws ErrorGuardarClienteException {
        File file = new File(FILE_PATH); //Creacion de archivo mediante la clase File
        boolean isNewFile = !file.exists();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))){
            if(isNewFile){
                writer.write("DNI;Nombre;Apellido;Fecha_Alta;Tipo_Persona;Banco;Fecha_Nacimiento;Direccion;Telefono");
                writer.newLine();
            }
            writer.write(cliente.getDni() + ";" + cliente.getNombre() + ";" + cliente.getApellido() + ";" + cliente.getFechaAlta() + ";" + cliente.getTipoPersona() + ";" + cliente.getBanco() + ";" + cliente.getFechaNacimiento() + ";" + cliente.getDireccion() + ";" + cliente.getTelefono());
            writer.newLine();
        }catch( IOException e ){
            throw new ErrorGuardarClienteException("Error al guardar el cliente en el archivo: " , e);
        }   
        


        
    }

    public Cliente parsearCliente(String[] datos){
        Cliente cliente = new Cliente();

        cliente.setDni(Long.parseLong(datos[0]));
        cliente.setNombre(datos[1]);
        cliente.setApellido(datos[2]);
        cliente.setDireccion(datos[3]);
        cliente.setFechaNacimiento(LocalDate.parse(datos[4]));
        cliente.setTipoPersona(TipoPersona.valueOf(datos[5]));
        cliente.setBanco(datos[6]);
        cliente.setFechaAlta(LocalDate.parse(datos[7]));
        cliente.setDireccion(datos[8]);
        cliente.setTelefono(datos[9]);

        return cliente;

        
    }


    @Override
    public Cliente obtenerClientePorDNI(long dni) throws ErrorArchivoNoEncontradoException{
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Evita leer el encabezado
                    continue;
                }
            
                String[] datos = line.split(";");
                if (datos.length >= 7 ) {
                    if (dni == Long.parseLong(datos[0])) {   //Comprueba que el dni exista en la base de datos
                        Cliente cliente = new Cliente();
                        cliente = parsearCliente(datos);
                        return cliente;
                    }               
                }    

    }}catch(FileNotFoundException e){
        throw new ErrorArchivoNoEncontradoException("Archivo no encontrado en la ruta especificada", e);

    } catch(IOException e){
        throw new ErrorArchivoNoEncontradoException("Error al leer el archivo", e);
    }
    return null;
    }


    @Override
    public void eliminarCliente(Long dni) throws ErrorEliminarLineaException, ErrorManejoArchvivoException {
        File inputFile = new File(FILE_PATH);
        //Creo un archivo temporal para guardar los datos y evitar que se borren por error
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

        try( BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                
                String line;
                boolean isHeader = true;

                while ((line = reader.readLine())!= null) {
                  if (isHeader) {
                        writer.write(line); //Copiar el encabezado
                        writer.newLine();
                        isHeader = false; // Evita leer el encabezado
                        continue;
                  }
                  String[] campos = line.split(";"); //divide cuando encuentra un caracter ;
                  if (campos.length == 9) {
                        Long dniActual = Long.parseLong(campos[0]); 
                        if (!dniActual.equals(dni)) {
                            writer.write(line); // Si el dni no coincide con el que pasamos como parametro se escribe la linea
                            writer.newLine();
                            
                        }
                            
                        }
                  }
                    
                  }catch(IOException e){
                    throw new ErrorEliminarLineaException("Error al eliminar el cliente" + e.getMessage());
    
                }

                if (!inputFile.delete()) {
                    throw new ErrorManejoArchvivoException("Error al eliminar el archivo original");
                    
                }

                if (!tempFile.renameTo(inputFile)) {
                    throw new ErrorManejoArchvivoException("Error al renombrar el archivo temporal");
                }
            };



    

   


    @Override
public List<Cliente> obtenerListaClientes() throws ErrorArchivoNoEncontradoException {
    List<Cliente> clientesEncontrados = new ArrayList<>();
    
    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
        String line;
        boolean isHeader = true;

        while ((line = reader.readLine()) != null) {
            if (isHeader) {
                isHeader = false; // Evita leer el encabezado
                continue;
            }

            String[] datos = line.split(";");
            if (datos.length >= 7) {
                Cliente cliente = new Cliente();
                    cliente = parsearCliente(datos);
                    clientesEncontrados.add(cliente);
                
            }
        }
    } catch (FileNotFoundException e) {
        throw new ErrorArchivoNoEncontradoException("Archivo no encontrado en la ruta especificada", e);
    } catch (IOException e) {
        throw new ErrorArchivoNoEncontradoException("Error al leer el archivo", e);
    }

    return clientesEncontrados;
}

    @Override
    public Cliente actualizarCliente(Cliente cliente) throws ErrorGuardarClienteException, ClienteNoEncontradoException , ErrorEliminarLineaException, ErrorArchivoNoEncontradoException{
        List<Cliente> clientes = obtenerListaClientes();

        boolean clienteEncontrado = false;

        for (Cliente c : clientes) {
            if (c.getDni() == cliente.getDni()) {
                clienteEncontrado = true;
                break; //Salir del bucle si se encuentra el cliente 
            }

    }
    if (clienteEncontrado) {
        
    try{
        eliminarCliente(cliente.getDni()); //Elimina el cliente existente
        guardarCliente(cliente);  //Guardo el cliente con los nuevos datos

    }catch (Exception e) {
        throw new ErrorEliminarLineaException("Erorr al eliminar el cliente: " + e.getMessage());

    }catch (IOError e){
        throw new ErrorGuardarClienteException("Error al guardar el cliente: " ,e);
    }
    return cliente;
}

    return null;

    }}