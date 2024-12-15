package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;

import ar.edu.utn.frbb.tup.controller.Dto.MovimientoDto;
import ar.edu.utn.frbb.tup.model.enums.*;

public class Movimiento {
    private LocalDate fechaOperacion;
    private TipoMovimiento tipo;
    private String descripcion;
    private Double monto;

   //Constructor
   public Movimiento(){
        super();
   }
   public Movimiento (MovimientoDto movimientoDto) { 
        this.fechaOperacion = LocalDate.parse(movimientoDto.getFechaOperacion());
        this.tipo = TipoMovimiento.fromString(movimientoDto.getTipo());
        this.descripcion = movimientoDto.getDescripcion();
        this.monto = Double.parseDouble(movimientoDto.getMonto());
   }
    //Getters y setters
   
    public LocalDate getFechaOperacion() {
        return fechaOperacion;
    }
    public void setFechaOperacion(LocalDate fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }
    public TipoMovimiento getTipo() {
        return tipo;
    }
    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public double getMonto() {
        return monto;
    }
    public void setMonto(double monto) {
        this.monto = monto;
    }
    
}
