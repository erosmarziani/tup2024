package ar.edu.utn.frbb.tup.model.enums;

public enum TipoPersona {

    PERSONA_FISICA,
    PERSONA_JURIDICA;


    
    public static TipoPersona fromString(String value) {
        try{
            return TipoPersona.valueOf(value.toUpperCase());
        }catch(IllegalArgumentException e ){
            throw new IllegalArgumentException("Tipo de operacion incorrecta");
        }
    }
}
