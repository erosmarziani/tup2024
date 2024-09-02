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

import ar.edu.utn.frbb.tup.model.Movimiento;

import ar.edu.utn.frbb.tup.model.enums.TipoOperacion;
import ar.edu.utn.frbb.tup.persistence.*;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEliminarLineaException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEscribirArchivoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorManejoArchvivoException;;

public class MovimientosDaoImpl implements MovimientosDAO {
    private static final String FILE_PATH = "src" + File.separator + "main" + File.separator + "java" + File.separator
            + "ar" + File.separator + "edu" + File.separator + "utn" + File.separator + "frbb" + File.separator + "tup"
            + File.separator + "persistence" + File.separator + "resources" + File.separator + "movimientos.txt";



    public Movimiento parseMovimiento(String[] campos){
        Movimiento movimiento = new Movimiento(
            Long.parseLong(campos[0]),
            Long.parseLong(campos[1]),
            LocalDate.parse(campos[2]),
            Integer.parseInt(campos[3]),
            TipoOperacion.valueOf(campos[4]));
            return movimiento;
    }

    @Override
    public void agregarMovimiento(Movimiento movimiento) throws ErrorEscribirArchivoException {
        File file = new File(FILE_PATH);
        boolean isHeader = true;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (isHeader) {
                writer.write("idMovimiento;idCuenta;fecha;importe;tipoOperacion");
                writer.newLine();
            }
            writer.write(
                    movimiento.getIdMovimiento() + ";" + movimiento.getIdCuenta() + ";" + movimiento.getFechaOperacion()
                            + ";" + movimiento.getImporte() + ";" + movimiento.getTipoOperacion());
            writer.newLine();

        } catch (IOException e) {
            throw new ErrorEscribirArchivoException("Error al escribir en el archivo: " + e.getMessage());

        }

    }

    @Override
    public List<Movimiento> obtenerMovimientoPorCuenta(long idCuenta) throws ErrorManejoArchvivoException {
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
                    Long numeroCuenta = Long.parseLong(campos[1]);
                    if (numeroCuenta.equals(idCuenta)) {
                        Movimiento movimiento = parseMovimiento(campos);
                        movimientos.add(movimiento);
                    }
                }
            }
        } catch (IOException e) {
            throw new ErrorManejoArchvivoException("Error al capturar los movimientos");
        }

        return movimientos;
    }

    @Override
    public Movimiento eliminarMovimientoPorID(long idMovimiento) throws ErrorManejoArchvivoException, ErrorEliminarLineaException {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File(inputFile.getAbsolutePath() + ".tmp");
        Movimiento movimientoEliminado = null;

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
                if (campos.length >= 5) {
                    long idActual = Long.parseLong(campos[0]);
                    if (idActual == idMovimiento) {
                        movimientoEliminado = parseMovimiento(campos);

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
            throw new ErrorManejoArchvivoException("Error al eliminar el archivo original");
        }

        if (!tempFile.renameTo(inputFile)) {
            throw new ErrorManejoArchvivoException("Error al renombrar el archivo temporal");
        }
        return movimientoEliminado;
    }

    @Override
    public List<Movimiento> obtenerMovimientos() throws ErrorManejoArchvivoException {
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
        throw new ErrorManejoArchvivoException("Error al capturar los movimientos");
    }
    return listaMovimientos;

}

}
