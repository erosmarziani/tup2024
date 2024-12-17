package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;

import ar.edu.utn.frbb.tup.controller.Dto.ClienteDto;

public class Cliente {
    private String nombre;
    private String apellido;
    private long dni;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String telefono;
    private LocalDate fechaAlta;

    public Cliente() {}
    public Cliente(long dni, String apellido, String nombre, LocalDate fechaNacimiento, String direccion, String telefono) {
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public Cliente(ClienteDto clienteDto){
        this.dni = Long.parseLong(clienteDto.getDni());
        this.apellido = clienteDto.getApellido();
        this.nombre = clienteDto.getNombre();
        this.direccion = clienteDto.getDireccion();
        this.fechaNacimiento = LocalDate.parse(clienteDto.getFechaNacimiento());
        this.telefono = clienteDto.getTelefono();
        this.fechaAlta = LocalDate.now();
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public String getDireccion(){
        return direccion;
    }

    public void setTelefono(String telefono){
        this.telefono = telefono;
    }

    public String getTelefono(){
        return telefono;
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

