package ar.edu.utn.frbb.tup.model.enums;

public enum TipoMovimiento {
    CREDITO,DEBITO;

    public static TipoMovimiento fromString(String value) {
        try{
             return TipoMovimiento.valueOf(value.toUpperCase());
        } catch(IllegalArgumentException e){
             throw new IllegalArgumentException("Tipo de mobvimiento incorrecto");
        }
     }
}
