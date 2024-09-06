package ar.edu.utn.frbb.tup.persistence.implementation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.enums.*;
import ar.edu.utn.frbb.tup.persistence.*;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarLineaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorGuardarClienteException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorManejoArchivoException;


@Repository
public class MovimientosDaoImpl implements MovimientosDAO {
    private static final String FILE_PATH = "tup2024\\src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\persistence\\resources\\movimientos.txt";

    public Movimiento parseMovimiento(String[] campos){
        Movimiento movimiento = new Movimiento(
            Long.parseLong(campos[0]),
            LocalDate.parse(campos[1]),
            Double.parseDouble(campos[2]),
            TipoOperacion.valueOf(campos[3]),
            TipoMoneda.valueOf(campos[4]));
            return movimiento;
    }
    @Override
    public void agregarMovimiento(Movimiento movimiento) throws ErrorGuardarClienteException {

        File file = new File(FILE_PATH); //Creacion de archivo mediante la clase File

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))){
            if(file.length() == 0){
                writer.write("idCuenta;fechaOperacion;importe;tipoOperacion;tipoMoneda");
                writer.newLine();
            }
            writer.write(movimiento.getIdCuenta() + ";" + movimiento.getFechaOperacion()+ 
            ";" + movimiento.getImporte() + ";" + movimiento.getTipoOperacion() + ";" + movimiento.getTipoMoneda() + ";" );
            writer.newLine();
        }catch( IOException e ){
            throw new ErrorGuardarClienteException("Error al guardar el movimiento en el archivo: " , e);
        }             
    }
    @Override
    public List<Movimiento> obtenerMovimientoPorCuenta(long idCuenta) throws ErrorManejoArchivoException {
        List<Movimiento> movimientos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] campos = line.split(";");
                if (campos.length >= 5) {
                    Long numeroCuenta = Long.parseLong(campos[0]);
                    if (numeroCuenta.equals(idCuenta)) {
                        Movimiento movimiento = parseMovimiento(campos);
                        movimientos.add(movimiento);
                    }
                }
            }
        } catch (IOException e) {
            throw new ErrorManejoArchivoException("Error al capturar los movimientos");
        }

        return movimientos;
    }

    @Override
    public boolean eliminarMovimientosPorCuenta(long idCuenta) throws ErrorManejoArchivoException, ErrorEliminarLineaException {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");
        boolean movimientoEncontrado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    //Copio el encabezado en el archivo temporal
                    writer.write(line);
                    writer.newLine();
                    isHeader = false;
                    continue;
                }
                String[] campos = line.split(";");
                if (campos.length >= 5) {
                    long idCuentaActual= Long.parseLong(campos[0]);
                    if (idCuentaActual == idCuenta) {
                        movimientoEncontrado = true;
                    } else {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            throw new ErrorEliminarLineaException("Error al eliminar movimiento" );
        }

        if (!inputFile.delete()) {
            throw new ErrorManejoArchivoException("Error al eliminar el archivo original");
        }

        if (!tempFile.renameTo(inputFile)) {
            throw new ErrorManejoArchivoException("Error al renombrar el archivo temporal");
        }
        return movimientoEncontrado;
    }

    @Override
    public List<Movimiento> obtenerMovimientos() throws ErrorManejoArchivoException {
        List<Movimiento> listaMovimientos = new ArrayList<>();
        File file = new File(FILE_PATH);
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                String[] campos = line.split(";");
                if (campos.length>=5) {
                    Movimiento movimiento = parseMovimiento(campos);
                    listaMovimientos.add(movimiento);
                    
                }
        }
    }catch(Exception e){
        throw new ErrorManejoArchivoException("Error al capturar los movimientos");
    }
    return listaMovimientos;

}

}
