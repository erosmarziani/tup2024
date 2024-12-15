package ar.edu.utn.frbb.tup.controller.Dto;



public class CuentaDto {
    private String idCuenta;
    private String dniTitular;
    private String tipoCuenta;
    private String moneda;
    private String fechaAlta;
    private String balance;


    // Getters y Setters
    public String getIdCuenta() {
        return idCuenta;
    }
    public void setIdCuenta(String idCuenta) {
        this.idCuenta = idCuenta;
    }
    public String getDniTitular() {
        return dniTitular;
    }
    public void setDniTitular(String dniTitular) {
        this.dniTitular = dniTitular;
    }
    public String getTipoCuenta() {
        return tipoCuenta;
    }
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }
    public String getMoneda() {
        return moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
    public String getFechaAlta() {
        return fechaAlta;
    }
    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }
    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }

}