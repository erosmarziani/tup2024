package ar.edu.utn.frbb.tup.controller;

public class ApiResponse {
    private String estado;
    private String mensaje;

    public ApiResponse(String estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    //Getters y setters

    public String getEstado(){
        return estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    public String getMensaje(){
        return mensaje;
    }
    
    public void setMensaje(String mensaje){
        this.mensaje = mensaje;
    }
}