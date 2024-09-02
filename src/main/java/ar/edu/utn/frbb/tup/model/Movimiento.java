package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import ar.edu.utn.frbb.tup.model.enums.*;

public class Movimiento {
    private final long idMovimiento;
    private long idCuenta;
    private LocalDate fechaOperacion;
    private int importe;
    TipoOperacion TipoOperacion;

    //constructor pasa idMovimiento por parametro
    public Movimiento(long idMovimiento, long idCuenta, LocalDate fechaOperacion, int importe, TipoOperacion tipoOperacion){
        this.idMovimiento = idMovimiento;
        this.idCuenta = idCuenta;
        this.fechaOperacion = fechaOperacion;
        this.importe = importe;
        this.TipoOperacion = tipoOperacion;
    }

    //Constructor para creacion sin ID
    public Movimiento(long idCuenta, LocalDate fechaOperacion, int importe, TipoOperacion tipoOperacion){
        this.idMovimiento = (long) (Math.random() * 100000); // genera un id aleatorio
        this.idCuenta = idCuenta;
        this.fechaOperacion = fechaOperacion;
        this.importe = importe;
        this.TipoOperacion = tipoOperacion;
    }

    public long getIdCuenta() {
        return idCuenta;
    }
    
    public LocalDate getFechaOperacion() {
        return fechaOperacion;
    }
    
    public int getImporte() {
        return importe;
    }
    
    public TipoOperacion getTipoOperacion() {
        return TipoOperacion;
    }
    
    public void setImporte(int importe) {
        this.importe = importe;
    }
    
    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.TipoOperacion = tipoOperacion;
    }
    public long getIdMovimiento(){
        return idMovimiento;
    }
    @Override
    public String toString() {
        return "Movimiento{" +
                "idMovimiento" + idMovimiento +
                "idCuenta=" + idCuenta +
                ", fechaOperacion=" + fechaOperacion +
                ", importe=" + importe +
                ", tipoOperacion=" + TipoOperacion +
                '}';
    
    }
}
