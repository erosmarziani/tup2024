package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;

import ar.edu.utn.frbb.tup.controller.Dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.model.enums.*;

public class Transferencia  {

    private long idOrigen;
    private long idDestino;
    private LocalDate fechaOperacion;
    private double importe;
    private TipoMoneda tipoMoneda;
    

    //Constructor para el dto
    public Transferencia(TransferenciaDto transferenciaDto) {
        this.idOrigen = Long.parseLong(transferenciaDto.getIdOrigen());
        this.idDestino = Long.parseLong(transferenciaDto.getIdDestino());
        this.fechaOperacion = LocalDate.parse(transferenciaDto.getFechaOperacion());
        this.importe = Double.parseDouble(transferenciaDto.getImporte());
        this.tipoMoneda =TipoMoneda.fromString(transferenciaDto.getTipoMoneda());
    }

    public Transferencia(long idOrigen,long idDestino,LocalDate fechaOperacion, double importe, TipoMoneda moneda) {
        this.idOrigen = idOrigen;
        this.idDestino = idDestino;
        this.fechaOperacion = fechaOperacion;
        this.importe = importe;
        this.tipoMoneda = moneda;

    }
    public LocalDate getFechaOperacion() {
        return fechaOperacion;
    }
    public void setFechaOperacion(LocalDate fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }
    public double getImporte() {
        return importe;
    }
    public void setImporte(double importe) {
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
    public long getIdDestino() {
        return idDestino;
    }
    public void setIdDestino(long idDestino) {
        this.idDestino = idDestino;
    }
    

}
