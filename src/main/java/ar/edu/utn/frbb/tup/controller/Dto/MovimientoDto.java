package ar.edu.utn.frbb.tup.controller.Dto;

public class MovimientoDto {
    private String fechaOperacion;
    private String tipo;
    private String descripcion;
    private String monto;
    
    //Getters y setters

    public String getFechaOperacion() {
        return fechaOperacion;
    }
    
    public void setFechaOperacion(String fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }   
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getMonto() {
        return monto;
    }
    
    public void setMonto(String monto) {
        this.monto = monto;
    }
    
}
