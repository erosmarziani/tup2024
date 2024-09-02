package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.enums.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enums.TipoOperacion;

public class Transferencia {
    private Long cuentaOrigen;
    private Long cuentaDestino;
    private int monto;
    private TipoMoneda tipoMoneda;
    private TipoOperacion tipoOperacion;

    public Transferencia(Long cuentaOrigen,Long cuentaDestino, int monto,TipoMoneda tipoMoneda,TipoOperacion tipoOperacion){
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
        this.tipoMoneda = tipoMoneda;
        this.tipoOperacion = tipoOperacion;
    }
    
    // Getters and Setters
    public Long getCuentaOrigen() {
        return cuentaOrigen;
    }
    
    public void setCuentaOrigen(Long cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }
    
    public Long getCuentaDestino() {
        return cuentaDestino;
    }
    
    public void setCuentaDestino(Long cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }
    
    public int getMonto() {
        return monto;
    }
    
    public void setMonto(int monto) {
        this.monto = monto;
    }
    
    public TipoMoneda getTipoMoneda() {
        return tipoMoneda;
    }
    
    public void setTipoMoneda(TipoMoneda tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
    
    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }
    
    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }
    
    
}
