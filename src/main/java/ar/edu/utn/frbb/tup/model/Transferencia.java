package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;

import ar.edu.utn.frbb.tup.model.enums.*;

public class Transferencia  {

    private long idOrigen;
    private long idDestino;
    private LocalDate fechaOperacion;
    private double importe;
    private TipoMoneda tipoMoneda;
    private ResultadoTransferencia ResultadoTransferencia;

    public Transferencia(long idOrigen,long idDestino,LocalDate fechaOperacion, double importe, TipoMoneda moneda,ResultadoTransferencia resultadoTransferencia) {
        this.idOrigen = idOrigen;
        this.idDestino = idDestino;
        this.fechaOperacion = fechaOperacion;
        this.importe = importe;
        this.tipoMoneda = moneda;
        this.ResultadoTransferencia = resultadoTransferencia;

    }
    public LocalDate getFechaOperacion() {
        return fechaOperacion;
    }
    public void setFechaOperacion(LocalDate fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }
    public double getimporte() {
        return importe;
    }
    public void setimporte(double importe) {
        this.importe = importe;
    }
    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }
    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
    public long getIdOrigen() {
        return idOrigen;
    }
    public void setIdOrigen(long idOrigen) {
        this.idOrigen = idOrigen;
    }
    public void setResultado(ResultadoTransferencia resultado) {
        this.ResultadoTransferencia = resultado;
    }
    public ResultadoTransferencia getResultadoTransferencia() {
        return ResultadoTransferencia;
    }

    public long getIdDestino() {
        return idDestino;
    }
    public void setIdDestino(long idDestino) {
        this.idDestino = idDestino;
    }
    

}
