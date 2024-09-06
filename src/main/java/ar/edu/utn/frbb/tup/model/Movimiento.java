package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import ar.edu.utn.frbb.tup.model.enums.*;

public class Movimiento {
    private long idCuenta;
    private LocalDate fechaOperacion;
    private double importe;
    TipoOperacion tipoOperacion;
    TipoMoneda tipoMoneda;
    

    //Constructor para creacion sin ID
    public Movimiento(long idCuenta, LocalDate fechaOperacion, double importe, TipoOperacion tipoOperacion, TipoMoneda tipoMoneda){
        this.idCuenta = idCuenta;
        this.fechaOperacion = fechaOperacion;
        this.importe = importe;
        this.tipoOperacion = tipoOperacion;
        this.tipoMoneda = tipoMoneda;
    }

    public long getIdCuenta() {
        return idCuenta;
    }
    
    public void setIdCuenta(long idCuenta) {
        this.idCuenta = idCuenta;
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
}
