package ar.edu.utn.frbb.tup.model.enums;

public enum TipoOperacion {
    DEBITO,
    CREDITO;


    // Método para obtener el enumerado por el detalle de la operación
    public static TipoOperacion fromString(String value) {
        try{
            return TipoOperacion.valueOf(value.toUpperCase());
        }catch(IllegalArgumentException e ){
            throw new IllegalArgumentException("Tipo de operacion incorrecta");
        }
    }

}