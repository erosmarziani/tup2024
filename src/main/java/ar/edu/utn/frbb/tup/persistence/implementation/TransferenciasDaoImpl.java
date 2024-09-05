/*package ar.edu.utn.frbb.tup.persistence.implementation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ar.edu.utn.frbb.tup.model.Movimiento;
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.persistence.TransferenciasDAO;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEscribirArchivoException;

public class TransferenciasDaoImpl implements TransferenciasDAO{
        private static final String FILE_PATH = "tup2024\\src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\persistence\\resources\\transferencias.txt";

    
    @Override
    public void agregarTransferencia(Transferencia transferencia) throws  {
        File file = new File(FILE_PATH);
        boolean isHeader = true;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (isHeader) {
                writer.write("idOrigen;idDestino;fechaOperacion;monto;Resultado");
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
}


*/