package ar.edu.utn.frbb.tup.controller.Dto;

public class ClienteDto extends PersonaDto{
    private String banco;

    //Getters y setters
  
    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }
}
