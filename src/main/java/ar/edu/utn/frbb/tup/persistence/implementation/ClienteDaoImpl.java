package ar.edu.utn.frbb.tup.persistence.implementation;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.persistence.ClientesDAO;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;

@Repository
public class ClienteDaoImpl implements ClientesDAO{

    private static final String FILE_PATH = "tup2024\\src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\persistence\\resources\\clientes.txt";
   
    @Override
    public void guardarCliente(Cliente cliente) throws ErrorArchivoException {
        File file = new File(FILE_PATH); //Creacion de archivo mediante la clase File
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))){
            if(file.length() == 0){
                writer.write("DNI;Nombre;Apellido;Fecha_Alta;Fecha_Nacimiento;Direccion;Telefono");
                writer.newLine();
            }
            writer.write(cliente.getDni() + ";" + cliente.getNombre() + ";" + cliente.getApellido() + ";" + cliente.getFechaAlta() +  ";" + cliente.getFechaNacimiento() + ";" + cliente.getDireccion() + ";" + cliente.getTelefono());
            writer.newLine();
        }catch(IOException e ){
            throw new ErrorArchivoException("Error al guardar el cliente en el archivo: " + e.getMessage());
        }        
    }

    public Cliente parsearCliente(String[] datos) {
        Cliente cliente = new Cliente();
    
        cliente.setDni(Long.parseLong(datos[0]));
        cliente.setNombre(datos[1]);
        cliente.setApellido(datos[2]);
        cliente.setFechaNacimiento(LocalDate.parse(datos[3]));
        cliente.setFechaAlta(LocalDate.parse(datos[4]));
        cliente.setDireccion(datos[5]);
        cliente.setTelefono(datos[6]);
    
        return cliente;
    }


    @Override
    public Cliente obtenerClientePorDNI(long dni) throws ErrorArchivoException{
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            String header = reader.readLine();
            if (header == null){
                throw new ErrorArchivoException("El archivo está vacío");
            } 
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] datos = line.split(";");
                    if (datos.length >= 7 && Long.parseLong(datos[0]) == dni) {
                        // Parsear y devolver el cliente cuando se encuentra el DNI
                        return parsearCliente(datos);
                    }
                }

    }catch(FileNotFoundException e){
        throw new ErrorArchivoException("Archivo no encontrado en la ruta especificada", e);

    } catch(IOException e){
        throw new ErrorArchivoException("Error al leer el archivo" + FILE_PATH, e);
    }
    return null;
    }


    @Override
    public void eliminarCliente(Long dni) throws ErrorArchivoException {
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
                  if (campos.length == 7) {
                        Long dniActual = Long.parseLong(campos[0]); 
                        if (!dniActual.equals(dni)) {
                            writer.write(line); // Si el dni no coincide con el que pasamos como parametro se escribe la linea
                            writer.newLine();
                            
                        }
                            
                        }
                  }
                    
                  }catch(IOException e){
                    throw new ErrorArchivoException("Error al eliminar el cliente" + e.getMessage());
                }

                if (!inputFile.delete()) {
                    throw new ErrorArchivoException("Error al eliminar el archivo original");
                    
                }
                if (!tempFile.renameTo(inputFile)) {
                    throw new ErrorArchivoException("Error al renombrar el archivo temporal");
                }
            };

    @Override
public List<Cliente> obtenerListaClientes() throws ErrorArchivoException {
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
        throw new ErrorArchivoException("Archivo no encontrado en la ruta especificada", e);
    } catch (IOException e) {
        throw new ErrorArchivoException("Error al leer el archivo", e);
    }

    return clientesEncontrados;
}

    @Override
    public Cliente actualizarCliente(Cliente cliente) throws ErrorArchivoException{
        List<Cliente> clientes = obtenerListaClientes();
        boolean clienteEncontrado = false;

        for (Cliente c : clientes) {
            if (c.getDni() == cliente.getDni()) {
                clienteEncontrado = true;
                break; //Salir del bucle si se encuentra el cliente 
            }

    }
    if (!clienteEncontrado) {
        return null;
    }

    try{
        eliminarCliente(cliente.getDni());
    }catch(ErrorArchivoException e){
        throw new ErrorArchivoException("Error al eliminar el cliente: " + e.getMessage());
    }
      
    try{
        guardarCliente(cliente); //Guardo el cliente con los nuevos datos
    }catch(ErrorArchivoException e){
        throw new ErrorArchivoException("Error al guardar el cliente: " + e.getMessage());
    }

    return cliente;
}
    }