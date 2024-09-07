package ar.edu.utn.frbb.tup.controller.Dto;

public class TransferenciaDto {
    private String idOrigen;
    private String idDestino;
    private String fechaOperacion;
    private String importe;
    private String tipoMoneda;

    // Getters y setters

    public String getIdOrigen() {
        return idOrigen;
    }
    public void setIdOrigen(String idOrigen) {
        this.idOrigen = idOrigen;
    }
    public String getIdDestino() {
        return idDestino;
    }
    public void setIdDestino(String idDestino) {
        this.idDestino = idDestino;
    }
    public String getFechaOperacion() {
        return fechaOperacion;
    }
    public void setFechaOperacion(String fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }
    public String getImporte() {
        return importe;
    }
    public void setImporte(String importe) {
        this.importe = importe;
    }
    public String getTipoMoneda() {
        return tipoMoneda;
    }
    public void setTipoMoneda(String tipoMoneda) {
        this.tipoMoneda = tipoMoneda;
    }
}
