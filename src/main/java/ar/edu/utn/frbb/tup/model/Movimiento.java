package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import ar.edu.utn.frbb.tup.model.enums.*;

public class Movimiento {
    private final long idMovimiento;
    private long idCuenta;
    private LocalDate fechaOperacion;
    private double importe;
    TipoOperacion tipoOperacion;
    TipoMoneda tipoMoneda;
    
    //constructor pasa idMovimiento por parametro
    public Movimiento(long idMovimiento, long idCuenta, LocalDate fechaOperacion, double importe, TipoOperacion tipoOperacion, TipoMoneda tipoMoneda){
        this.idMovimiento = idMovimiento;
        this.idCuenta = idCuenta;
        this.fechaOperacion = fechaOperacion;
        this.importe = importe;
        this.tipoOperacion = tipoOperacion;
        this.tipoMoneda = tipoMoneda;
    }

    //Constructor para creacion sin ID
    public Movimiento(long idCuenta, LocalDate fechaOperacion, double importe, TipoOperacion tipoOperacion, TipoMoneda tipoMoneda){
        this.idMovimiento = (long) (Math.random() * 100000); // genera un id aleatorio
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
    public long getIdMovimiento(){
        return idMovimiento;
    }

    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }
    
    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
}
