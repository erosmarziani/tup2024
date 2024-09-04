package ar.edu.utn.frbb.tup.model.enums;

public enum TipoMoneda {
    ARS("PESOS"),
    USD("DOLARES");

    private final String descripcion;

    TipoMoneda(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
    //Metodo para convertir un String en TipoMoneda
    public static TipoMoneda fromString(String value) {
        for (TipoMoneda moneda : TipoMoneda.values()) {
            if (moneda.getDescripcion().equalsIgnoreCase(value)) {
                return moneda;
            }
        }
        return null;
    }


}
