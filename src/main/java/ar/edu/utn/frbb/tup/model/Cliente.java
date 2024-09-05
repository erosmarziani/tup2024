package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;


import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.enums.TipoPersona;

public class Cliente extends Persona{

    private TipoPersona tipoPersona;
    private LocalDate fechaAlta;

    public Cliente() {
        super();
    }

    public Cliente(ClienteDto clienteDto) {
        super(
            Long.parseLong(clienteDto.getDni()),
             clienteDto.getApellido(),
              clienteDto.getNombre(),
               LocalDate.parse(clienteDto.getFechaNacimiento()),
               clienteDto.getDireccion(),
                clienteDto.getTelefono());
        this.tipoPersona = TipoPersona.fromString(clienteDto.getTipoPersona());
        this.fechaAlta = LocalDate.now();
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

   
    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }





}
