package ar.edu.utn.frbb.tup.model.enums;

public enum TipoCuenta {

    CUENTA_CORRIENTE,
    CAJA_AHORRO;


    public static TipoCuenta fromString(String value) {
       try{
            return TipoCuenta.valueOf(value.toUpperCase());
       } catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Tipo de cuenta incorrecto");
       }
    }
}
