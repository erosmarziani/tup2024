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
import ar.edu.utn.frbb.tup.model.Transferencia;
import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.TransferenciasDAO;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorEscribirArchivoException;
import ar.edu.utn.frbb.tup.persistence.exception.ErrorManejoArchivoException;

public class TransferenciasDaoImpl implements TransferenciasDAO{
        private static final String FILE_PATH = "tup2024\\src\\main\\java\\ar\\edu\\utn\\frbb\\tup\\persistence\\resources\\transferencias.txt";

    
    @Override
    public Transferencia agregarTransferencias(Transferencia transferencia) throws ErrorEscribirArchivoException  {
        File file = new File(FILE_PATH);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (file.length() == 0) {
                writer.write("idOrigen;idDestino;fechaOperacion;importe;moneda");
                writer.newLine();
            }
            writer.write(
                    transferencia.getIdOrigen() + ";" +
                    transferencia.getIdDestino() + ";" +
                    transferencia.getFechaOperacion().toString() + ";" +
                    transferencia.getImporte() + ";" +
                    transferencia.getTipoMoneda()
                    );
            writer.newLine();

        } catch (IOException e) {
            throw new ErrorEscribirArchivoException("Error al escribir en el archivo: " + e.getMessage());
        }
        return transferencia;
    }

    @Override
    public List<Transferencia> obtenerListadoTransferencias() throws Exception {

        List<Transferencia> transferencias = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))){
                String line;
                boolean isHeader = true;
                while ((line = reader.readLine()) != null) {
                    if (isHeader) {
                        isHeader = false; // Evita leer el encabezado
                        continue;
                    }
                
                    String[] campos = line.split(";");
                    if (campos.length >= 5 ) {
                            long idOrigen = Long.parseLong(campos[0]);
                            long idDestino = Long.parseLong(campos[1]);
                            LocalDate fechaOperacion = LocalDate.parse(campos[2]);
                            double importe = Double.parseDouble(campos[3]);
                            TipoMoneda tipoMoneda = TipoMoneda.fromString(campos[4]);

                            Transferencia transferencia = new Transferencia(idOrigen, idDestino, fechaOperacion, importe, tipoMoneda);
                            transferencias.add(transferencia);
                        }     
                    }
                }catch(IOException e){
                    throw new ErrorManejoArchivoException("Error al manejar el archivo");
                }
                return transferencias;
        }

    }