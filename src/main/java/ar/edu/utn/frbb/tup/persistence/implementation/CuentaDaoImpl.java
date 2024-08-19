package ar.edu.utn.frbb.tup.persistence.implementation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.CuentasDAO;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoNoEncontradoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorCuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarCuentaException;

public class CuentaDaoImpl implements CuentasDAO {
    private static final String FILE_PATH = "src" + File.separator + "main" + File.separator + "java" + File.separator + "ar" + File.separator + "edu" + File.separator + "utn" + File.separator + "frbb" + File.separator + "tup" + File.separator + "persistence" + File.separator + "resources" + File.separator + "cuentas.txt";

    @Override
    public void guardarCuenta(Cuenta cuenta)  throws ErrorGuardarCuentaException { 
        File file = new File(FILE_PATH);
        boolean isNewFile = !file.exists();
        System.out.println("Guardando cuenta en archivo: " + FILE_PATH);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file,true))){
            if (isNewFile) {
                writer.write("ID_CUENTA,FECHA_CREACION,BALANCE,TIPO_CUENTA,TITULAR,MONEDA"); //Insertamos el encabezado
                writer.newLine();
            }
                writer.write(cuenta.getNumeroCuenta()+ ";" + cuenta.getMoneda() + ";" + cuenta.getBalance() + ";" + cuenta.getTipoCuenta() + ";" + cuenta.getTitular() + ";" + cuenta.getMoneda());
                writer.newLine();
        }catch(Exception e){
            throw new ErrorGuardarCuentaException("Error al guardar cuenta en el archivo: " + e.getMessage());
    
    }

}

    @Override
    public Cuenta obtenerCuentaPorId(long idCuenta) throws ErrorCuentaNoEncontradaException, ErrorArchivoNoEncontradoException, IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
            String line;
            boolean isHeader = true;

        while ((line = reader.readLine())!=null) {
            if (isHeader) {
                isHeader = false; // Evita leer el encabezado
                continue;
            }
            String[] datos = line.split(";");
            if (datos.length >= 6) {
                long numeroCuentaLeida = Long.parseLong(datos[0]);
                if (numeroCuentaLeida == idCuenta) {
                    LocalDate Fecha_Alta = LocalDate.parse(datos[1]);
                    int balance = Integer.parseInt(datos[2]);
                    TipoCuenta tipoCuenta = TipoCuenta.valueOf(datos[3]);
                    //  Cliente titular = new Cliente(datos[4]);
                    TipoMoneda tipoMoneda = TipoMoneda.fromString(datos[5]);

                    return new Cuenta(numeroCuentaLeida,Fecha_Alta,balance,tipoMoneda,titular,tipoMoneda);
                }
                
            }
            
        }

    }catch(FileNotFoundException e) {
        throw new ErrorArchivoNoEncontradoException("No se encontró la cuenta con el número: " + idCuenta , e);
    }catch(IOException e){
        throw new ErrorArchivoNoEncontradoException("Error al leer el archivo" ,e );
    }
    }


    @Override
    public void eliminarCuenta(Cuenta cuenta) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarCuenta'");
    }

    @Override
    public void actualizarCuenta(Cuenta cuenta) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarCuenta'");
    }   
}
