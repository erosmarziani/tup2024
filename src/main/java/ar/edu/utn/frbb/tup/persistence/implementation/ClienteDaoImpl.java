package ar.edu.utn.frbb.tup.persistence.implementation;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.persistence.ClientesDAO;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarArchivoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorActualizarClienteException;


public class ClienteDaoImpl implements ClientesDAO{

    private static final String FILE_PATH = "src" + File.separator + "main" + File.separator + "java" + File.separator + "ar" + File.separator + "edu" + File.separator + "utn" + File.separator + "frbb" + File.separator + "tup" + File.separator + "persistence" + File.separator + "resources" + File.separator + "clientes.txt";
   
   
    @Override
    public void guardarCliente(Cliente cliente) throws ErrorGuardarClienteException {
        File file = new File(FILE_PATH); //Creacion de archivo mediante la clase File
        boolean isNewFile = !file.exists();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))){
            if(isNewFile){
                writer.write("DNI;Nombre;Apellido;Fecha_Alta;Tipo_Persona;Banco;Fecha_Nacimiento;");
                writer.newLine();
            }
            writer.write(cliente.getDni() + ";" + cliente.getNombre() + ";" + cliente.getApellido() + ";" + cliente.getFechaAlta() + ";" + cliente.getTipoPersona() + ";" + cliente.getBanco() + ";" + cliente.getFechaNacimiento());
            writer.newLine();
        }catch( Exception e ){
            throw new ErrorGuardarClienteException("Error al guardar el cliente en el archivo.");
        }   
        
        
    }

    @Override
    public void eliminarCliente(Long dni) throws ErrorEliminarClienteException, ErrorEliminarArchivoException {
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
                  String[] parts = line.split(";"); //divide cuando encuentra un caracter ;
                  if (parts.length == 7) {
                        String dniActual = parts[0]; 
                        if (!dniActual.equals(dni)) {
                            writer.write(line); // Si el dni no coincide con el que pasamos como parametro se escribe la linea
                            writer.newLine();
                            
                        }
                            
                        }
                  }
                    
                  }catch(IOException e){
                    throw new ErrorEliminarClienteException("Error al eliminar el cliente" ,e);
    
                }

                if (!inputFile.delete()) {
                    throw new ErrorEliminarArchivoException("Error al eliminar el archivo original");
                    
                }

                if (!tempFile.renameTo(inputFile)) {
                    throw new ErrorEliminarArchivoException("Error al renombrar el archivo temporal");
                }
            };



    

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
                    long dniActual = Long.parseLong(datos[0]);
                    if (dniActual == (dni)) {
                        String nombre = datos[1];
                        String apellido = datos[2];
                        LocalDate fechaAlta = LocalDate.parse(datos[3]);
                        TipoPersona tipoPersona = TipoPersona.fromString(datos[4]);
                        String banco = datos[5];
                        LocalDate fechaNacimiento = LocalDate.parse(datos[6]);

                        return new Cliente(dniActual,apellido,nombre,fechaNacimiento,tipoPersona,banco,fechaAlta);
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
                    long idActual = Long.parseLong(datos[0]);
                    String nombre = datos[1];
                    String apellido = datos[2];
                    LocalDate fechaAlta = LocalDate.parse(datos[3]);
                    TipoPersona tipoPersona = TipoPersona.fromString(datos[4]);
                    String banco = datos[5];
                    LocalDate fechaNacimiento = LocalDate.parse(datos[6]);

                    // Crear un nuevo cliente y agregarlo a la lista para devolverlo
                    Cliente cliente = new Cliente(idActual, nombre, apellido, fechaAlta, tipoPersona, banco, fechaNacimiento);
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
    public void actualizarCliente(Cliente cliente) throws Exception {
        List<Cliente> clientes = obtenerListaClientes();

        boolean clienteEncontrado = false;

        for (Cliente c : clientes) {
            if (c.getDni() == cliente.getDni()) {
                clienteEncontrado = true;
                break; //Salir del bucle si se encuentra el cliente 
            }

    }

    if(!clienteEncontrado) {
        throw new ErrorActualizarClienteException("El cliente no se encuentra en la lista");
    }

    try{
        eliminarCliente(cliente.getDni()); //Elimina el cliente existente
        guardarCliente(cliente);  //Guardo el cliente con los nuevos datos

    }catch(Exception e){
        throw new ErrorGuardarClienteException("Error al guardar el cliente");
    }
        


    }}