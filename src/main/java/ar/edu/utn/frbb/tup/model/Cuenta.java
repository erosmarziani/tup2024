package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;
import java.util.Random;

import ar.edu.utn.frbb.tup.model.exception.CantidadNegativaException;

import ar.edu.utn.frbb.tup.model.exception.NoAlcanzaException;

public class Cuenta {
    private long idCuenta;
    LocalDate fechaCreacion;
    int balance;
    TipoCuenta tipoCuenta;
    long idTitular;
    TipoMoneda moneda;


    public Cuenta(long idCuenta,LocalDate fechaCreacion,int balance,TipoCuenta tipoCuenta, long titular, TipoMoneda moneda) {
        this.idCuenta = idCuenta;
        this.fechaCreacion = fechaCreacion;
        this.balance = balance;
        this.tipoCuenta = tipoCuenta;
        this.idTitular = titular;
        this.moneda = moneda;
    }
    public Cuenta() {
        this.idCuenta = new Random().nextLong();
        this.balance = 0;
        this.fechaCreacion = LocalDate.now();
    }

    public long getTitular() {
        return idTitular;
    }

    public void setTitular(long idTitular) {
        this.idTitular = idTitular;
    }


    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }


    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public int getBalance() {
        return balance;
    }

    public Cuenta setBalance(int balance) {
        this.balance = balance;
        return this;
    }

    public void debitarDeCuenta(int cantidadADebitar) throws NoAlcanzaException, CantidadNegativaException {
        if (cantidadADebitar < 0) {
            throw new CantidadNegativaException();
        }

        if (balance < cantidadADebitar) {
            throw new NoAlcanzaException();
        }
        this.balance = this.balance - cantidadADebitar;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.idCuenta = numeroCuenta;
    }

    public void forzaDebitoDeCuenta(int i) {
        this.balance = this.balance - i;
    }

    public long getNumeroCuenta() {
        return idCuenta;
    }


}
