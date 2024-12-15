package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;

public class Cliente extends Persona{

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
        this.fechaAlta = LocalDate.now();
    }
   
    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public int getEdad(){
        LocalDate hoy = LocalDate.now();
        return hoy.getYear() - getFechaNacimiento().getYear();
    }
}
