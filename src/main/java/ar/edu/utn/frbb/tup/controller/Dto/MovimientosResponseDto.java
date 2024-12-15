package ar.edu.utn.frbb.tup.controller.Dto;

import java.util.List;

public class MovimientosResponseDto {
    private String numeroCuenta;
    private List<MovimientoDto> transacciones;

    // Getters y setters
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public List<MovimientoDto> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<MovimientoDto> transacciones) {
        this.transacciones = transacciones;
    }

}
