package ar.edu.utn.frbb.tup.model;

public enum TipoCuenta {

    CUENTA_CORRIENTE,
    CAJA_AHORRO;


    public static TipoMoneda fromString(String value) {
        for (TipoMoneda moneda : TipoMoneda.values()) {
            if (moneda.getDescripcion().equalsIgnoreCase(value)) {
                return moneda;
            }
        }
        return null;
    }

}
