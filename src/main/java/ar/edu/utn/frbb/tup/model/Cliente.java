package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;

import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.enums.TipoPersona;

public class Cliente extends Persona{

    private TipoPersona tipoPersona;
    private String banco;
    private LocalDate fechaAlta;

    public Cliente() {
        super();
    }

    public Cliente(ClienteDto clienteDto) {
        super(clienteDto.getDni(), clienteDto.getApellido(), clienteDto.getNombre(), clienteDto.getFechaNacimiento(),clienteDto.getDireccion(), clienteDto.getTelefono());
        fechaAlta = LocalDate.now();
        banco = clienteDto.getBanco();
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }





}
