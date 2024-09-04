package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;

import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoOperacion;

public class Transferencia  {

    private long idOrigen;
    private long idDestino;
    private LocalDate fechaOperacion;
    private double monto;
    private TipoOperacion tipoOperacion;
    private TipoMoneda tipoMoneda;

    public Transferencia(long idOrigen,long idDestino,LocalDate fechaOperacion, double importe, TipoMoneda moneda, TipoOperacion operacion) {
        this.idOrigen = idOrigen;
        this.idDestino = idDestino;
        this.fechaOperacion = fechaOperacion;
        this.monto = importe;
        this.tipoOperacion = operacion;
        this.tipoMoneda = moneda;


    }
    public LocalDate getFechaOperacion() {
        return fechaOperacion;
    }
    public void setFechaOperacion(LocalDate fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }
    public double getMonto() {
        return monto;
    }
    public void setMonto(double monto) {
        this.monto = monto;
    }
    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }
    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
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
    

    public long getIdDestino() {
        return idDestino;
    }
    public void setIdDestino(long idDestino) {
        this.idDestino = idDestino;
    }
    

}
