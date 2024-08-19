package ar.edu.utn.frbb.tup.persistence.implementation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.persistence.CuentasDAO;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorActualizarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorActualizarCuentaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorCuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarArchivoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarCuentaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarCuentaException;

public class CuentaDaoImpl implements CuentasDAO {
    private static final String FILE_PATH = "src" + File.separator + "main" + File.separator + "java" + File.separator + "ar" + File.separator + "edu" + File.separator + "utn" + File.separator + "frbb" + File.separator + "tup" + File.separator + "persistence" + File.separator + "resources" + File.separator + "cuentas.txt";

    @Override
    public void guardarCuenta(Cuenta cuenta) throws ErrorGuardarCuentaException { 
        File file = new File(FILE_PATH);
        boolean isNewFile = !file.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (isNewFile) {
                writer.write("ID_CUENTA,FECHA_CREACION,BALANCE,TIPO_CUENTA,TITULAR,MONEDA");
                writer.newLine();
            }
            writer.write(cuenta.getNumeroCuenta() + ";" 
                        + cuenta.getFechaCreacion() + ";" 
                        + cuenta.getBalance() + ";" 
                        + cuenta.getTipoCuenta() + ";" 
                        + cuenta.getTitular() + ";" 
                        + cuenta.getMoneda());
            writer.newLine();
        } catch (IOException e) {
            throw new ErrorGuardarCuentaException("Error al guardar cuenta en el archivo: " + e.getMessage());
        }
    }

    @Override
    public Cuenta obtenerCuentaPorId(long idCuenta) throws ErrorCuentaNoEncontradaException, ErrorArchivoNoEncontradoException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] datos = line.split(";");
                if (datos.length >= 6) {
                    long numeroCuentaLeida = Long.parseLong(datos[0]);
                    if (numeroCuentaLeida == idCuenta) {
                        LocalDate fechaCreacion = LocalDate.parse(datos[1]);
                        int balance = Integer.parseInt(datos[2]);
                        TipoCuenta tipoCuenta = TipoCuenta.valueOf(datos[3]);
                        long dniTitular = Long.parseLong(datos[4]);
                        TipoMoneda tipoMoneda = TipoMoneda.valueOf(datos[5]);

                        // Aquí se debe obtener el Cliente del DAO correspondiente
                        ClienteDaoImpl clienteDao = new ClienteDaoImpl(); // Esto debería ser inyectado
                        Cliente titular = clienteDao.obtenerClientePorDNI(dniTitular);
                        if (titular == null) {
                            throw new ErrorCuentaNoEncontradaException("No se encontró el cliente con el DNI: " + dniTitular);
                        }

                        return new Cuenta(numeroCuentaLeida, fechaCreacion, balance, tipoCuenta, dniTitular, tipoMoneda);
                    }
                }
            }
        } catch (IOException e) {
            throw new ErrorArchivoNoEncontradoException("Error al leer el archivo", e);
        }

        throw new ErrorCuentaNoEncontradaException("No se encontró la cuenta con el número: " + idCuenta);
    }

    @Override
    public void eliminarCuenta(long cuentaID) throws ErrorEliminarCuentaException,ErrorEliminarArchivoException {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    writer.write(line);
                    writer.newLine();
                    isHeader = false;
                    continue;
                }
                String[] parts = line.split(";");
                if (parts.length >= 6) {
                    long idActual = Long.parseLong(parts[0]);
                    if (idActual != cuentaID) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            throw new ErrorEliminarCuentaException("Error al eliminar la cuenta: " + e.getMessage());
        }

        if (!inputFile.delete()) {
            throw new ErrorEliminarArchivoException("Error al eliminar el archivo original");
        }

        if (!tempFile.renameTo(inputFile)) {
            throw new ErrorEliminarArchivoException("Error al renombrar el archivo temporal");
        }
    }

    @Override
    public List<Cuenta> obtenerCuentas() throws ErrorArchivoNoEncontradoException,ErrorCuentaNoEncontradaException {
        List<Cuenta> cuentasEncontradas = new ArrayList<>();

         try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
        String line;
        boolean isHeader = true;

        while ((line = reader.readLine()) != null) {
            if (isHeader) {
                isHeader = false; // Evita leer el encabezado
                continue;
            }

            String[] datos = line.split(";");
            if (datos.length >= 6) {
                    long idCuenta = Long.parseLong(datos[0]);
                    LocalDate fechaCreacion = LocalDate.parse(datos[1]);
                    int balance = Integer.parseInt(datos[2]);
                    TipoCuenta tipoCuenta = TipoCuenta.valueOf(datos[3]);
                    long cliente = Long.parseLong(datos[4]);
                    TipoMoneda tipoMoneda = TipoMoneda.valueOf(datos[5]);

                    // Crear un nuevo cliente y agregarlo a la lista para devolverlo
                    Cuenta cuenta = new Cuenta(idCuenta,fechaCreacion,balance,tipoCuenta,cliente,tipoMoneda);
                    cuentasEncontradas.add(cuenta);
                
            }
        }
    } catch (FileNotFoundException e) {
        throw new ErrorCuentaNoEncontradaException("Archivo no encontrado en la ruta especificada" + e.getMessage());
    } catch (IOException e) {
        throw new ErrorCuentaNoEncontradaException("Error al leer el archivo" + e.getMessage());
    }

        return cuentasEncontradas;
}

        


    @Override
    public void actualizarCuenta(Cuenta cuenta) throws ErrorActualizarCuentaException, ErrorGuardarCuentaException, ErrorArchivoNoEncontradoException, ErrorCuentaNoEncontradaException {
        List<Cuenta> cuentas = obtenerCuentas();

        boolean cuentaEncontrada = false;

        for (Cuenta c : cuentas) {
            if (c.getNumeroCuenta() == cuenta.getNumeroCuenta()) {
                cuentaEncontrada = true;
                break; //Salir del bucle si se encuentra el cliente 
            }

    }

    if(!cuentaEncontrada) {
        throw new ErrorActualizarCuentaException("El cliente no se encuentra en la lista");
    }

    try{
        eliminarCuenta(cuenta.getNumeroCuenta()); //Elimina la cuenta existente
        guardarCuenta(cuenta);  //Guardo la cuenta con los nuevos datos

    }catch(Exception e){
        throw new ErrorGuardarCuentaException("Error al guardar la cuenta");
    }
        


    }
}
