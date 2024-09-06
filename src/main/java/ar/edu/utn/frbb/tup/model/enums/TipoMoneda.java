package ar.edu.utn.frbb.tup.model.enums;

public enum TipoMoneda {

    ARS,
    USD;
    
    //Metodo para convertir un String en TipoMoneda
    public static TipoMoneda fromString(String value) {
        try{
            return TipoMoneda.valueOf(value.toUpperCase());
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Tipo de moneda incorrecta");
        }
    }

    
    

}
