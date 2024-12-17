package ar.edu.utn.frbb.tup.persistence.implementation;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.enums.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.CuentasDAO;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;

@Repository
public class CuentaDaoImpl implements CuentasDAO {
    private static final String FILE_PATH = "tup2024\\src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\persistence\\resources\\cuentas.txt";

    @Autowired
    private ClienteDaoImpl clienteDao;

    public Cuenta parsearCuenta(String[] campos) {
        Cuenta cuenta = new Cuenta();

        cuenta.setNumeroCuenta(Long.parseLong(campos[0]));
        cuenta.setFechaCreacion(LocalDate.parse(campos[1]));
        cuenta.setBalance(Double.valueOf(campos[2]));
        cuenta.setTipoCuenta(TipoCuenta.valueOf(campos[3]));
        cuenta.setTitular(Long.parseLong(campos[4]));
        cuenta.setMoneda(TipoMoneda.valueOf(campos[5]));
        return cuenta;

    }

    @Override
    public void guardarCuenta(Cuenta cuenta) throws ErrorArchivoException {
        File file = new File(FILE_PATH);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (file.length() == 0) {
                writer.write("idCuenta;fechaAlta;balance;tipoCuenta;dniTitular;moneda");
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
            throw new ErrorArchivoException("Error al guardar cuenta en el archivo: " , e);
        }
    }

    @Override
    public Cuenta obtenerCuentaPorId(long idCuenta) throws ErrorArchivoException{
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
                        Cuenta cuentaObtenida = new Cuenta(
                            numeroCuentaLeida,
                            LocalDate.parse(datos[1]),
                            Double.parseDouble(datos[2]),
                            TipoCuenta.valueOf(datos[3]),
                            Long.parseLong(datos[4]),
                            TipoMoneda.valueOf(datos[5])

                        );
                        //Obtener el cliente del Dao
                        Cliente titular = clienteDao.obtenerClientePorDNI(Long.parseLong(datos[4]));
                        if (titular == null) {
                            throw new ErrorArchivoException("No se encontró el cliente con el DNI");
                        }
                        return  cuentaObtenida;
                    }
                }
            }
        } catch (IOException e) {
            throw new ErrorArchivoException("Error al leer el archivo", e);
        }
        return null;
    }

    @Override
    public Cuenta eliminarCuenta(long clienteID) throws ErrorArchivoException {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");
        Cuenta cuentaEliminada = null;

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
                String[] campos = line.split(";");
                if (campos.length >= 6) {
                    long idActual = Long.parseLong(campos[4]);
                    if (idActual == clienteID) {
                        cuentaEliminada = parsearCuenta(campos);

                    } else {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            throw new ErrorArchivoException("Error al eliminar la cuenta: " + e.getMessage());
        }

        if (!inputFile.delete()) {
            throw new ErrorArchivoException("Error al eliminar el archivo original");
        }

        if (!tempFile.renameTo(inputFile)) {
            throw new ErrorArchivoException("Error al renombrar el archivo temporal");
        }
        return cuentaEliminada;
    }

    @Override
    public List<Cuenta> obtenerCuentasDelCliente(long idCliente) throws ErrorArchivoException {
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        File file = new File(FILE_PATH);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                } else {
                    String[] campos = line.split(";");
                    if (Long.parseLong(campos[4]) == idCliente) {
                        Cuenta cuenta = parsearCuenta(campos);
                        cuentasDelCliente.add(cuenta);
                    }
                }
            }
        } catch (IOException e) {
            throw new ErrorArchivoException("Error al leer el archivo: ", e);
        }
        return cuentasDelCliente;
    }

    @Override
    public List<Cuenta> obtenerCuentas() throws ErrorArchivoException {
        List<Cuenta> cuentasEncontradas = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false; // Evita leer el encabezado
                    continue;
                }

                String[] campos = line.split(";");
                if (campos.length >= 6) {
                    Cuenta cuenta = parsearCuenta(campos);
                    cuentasEncontradas.add(cuenta);

                }
            }
        } catch (FileNotFoundException e) {
            throw new ErrorArchivoException("Archivo no encontrado en la ruta especificada" + e.getMessage());
        } catch (IOException e) {
            throw new ErrorArchivoException("Error al leer el archivo" + e.getMessage());
        }

        return cuentasEncontradas;
    }

    @Override
    public Cuenta actualizarCuenta(Cuenta cuenta) throws ErrorArchivoException {
        List<Cuenta> cuentas = obtenerCuentas();
        boolean cuentaEncontrada = false;

        for (Cuenta c : cuentas) {
            if (c.getNumeroCuenta() == cuenta.getNumeroCuenta()) {
                cuentaEncontrada = true;
                break; // Salir del bucle si se encuentra el cliente
            }
        }
        if (!cuentaEncontrada) {
            return null;
        }

        try {
            eliminarCuenta(cuenta.getNumeroCuenta()); // Elimina la cuenta existente
        }catch(Exception e ){
            throw new ErrorArchivoException("Error al eliminar la cuenta anteriormente");
        };

        try{
            guardarCuenta(cuenta);
        }catch (Exception e) {
            throw new ErrorArchivoException("Error al guardar la cuenta");
        }
        return cuenta;

    }
    @Override
    public Cuenta actualizarBalance(long numeroCuenta, double nuevoBalance) throws ErrorArchivoException {
        List<Cuenta> cuentas = obtenerCuentas();
        Cuenta cuentaActualizada = null;
        boolean cuentaEncontrada = false;
    
        // Buscar la cuenta y actualizar el balance
        for (Cuenta c : cuentas) {
            if (c.getNumeroCuenta() == numeroCuenta) {
                cuentaEncontrada = true;
                c.setBalance(nuevoBalance);
                cuentaActualizada = c;
                break; // Salir del bucle si se encuentra la cuenta
            }
        }
        // Si la cuenta no se encontró, retornar null
        if (!cuentaEncontrada) {
            return null;
        }
    
        // Intentar guardar la cuenta actualizada
        try{
            File file = new File(FILE_PATH);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("idCuenta;fechaAlta;balance;tipoCuenta;dniTitular;moneda");
                writer.newLine();

                for (Cuenta c : cuentas) {
                    writer.write(c.getNumeroCuenta() + ";"
                            + c.getFechaCreacion() + ";"
                            + c.getBalance() + ";"
                            + c.getTipoCuenta() + ";"
                            + c.getTitular() + ";"
                            + c.getMoneda());
                    writer.newLine();
                }
            }
        }catch(IOException e){
            throw new ErrorArchivoException("Error al actualizar el balance de la cuenta");
        }
        return cuentaActualizada;
    }

}