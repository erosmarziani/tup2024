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
import ar.edu.utn.frbb.tup.persistence.exception.ErrorArchivoException;


@Repository
public class MovimientosDaoImpl implements MovimientosDAO {
    private static final String FILE_PATH = "tup2024\\src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\persistence\\resources\\movimientos.txt";

    public Movimiento parseMovimiento(String[] campos){
        Movimiento movimiento = new Movimiento();
        movimiento.setFechaOperacion(LocalDate.parse(campos[1]));
        movimiento.setTipo(TipoMovimiento.fromString(campos[2]));
        movimiento.setDescripcion(campos[3]);
        movimiento.setMonto(Double.parseDouble(campos[4]));

        return movimiento;
    }

    @Override
    public void guardarMovimiento(Movimiento movimiento, long numeroCuenta) throws ErrorArchivoException {

        File file = new File(FILE_PATH); //Creacion de archivo mediante la clase File

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))){
            if(file.length() == 0){
                writer.write("idCuenta;fechaOperacion;tipo;descripcion;monto");
                writer.newLine();
            }
            writer.write(numeroCuenta + ";" + movimiento.getFechaOperacion()+ 
            ";" + movimiento.getTipo() + ";" + movimiento.getDescripcion() + ";" + movimiento.getMonto() );
            writer.newLine();
        }catch( IOException e ){
            throw new ErrorArchivoException("Error al guardar el movimiento en el archivo: " , e);
        }             
    }
    @Override
    public List<Movimiento> obtenerMovimientoPorCuenta(long idCuenta) throws ErrorArchivoException {
        List<Movimiento> listaMovimientos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                String[] campos = line.split(";");
                if (campos.length == 5) {
                    Long numeroCuenta = Long.parseLong(campos[0]);
                    if (numeroCuenta.equals(idCuenta)) {
                        Movimiento movimiento = parseMovimiento(campos);
                        listaMovimientos.add(movimiento);
                    }
                }
            }
        } catch (IOException e) {
            throw new ErrorArchivoException("Error al capturar los movimientos");
        }

        return listaMovimientos;
    }


}
