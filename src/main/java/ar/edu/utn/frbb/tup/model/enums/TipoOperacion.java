package ar.edu.utn.frbb.tup.model.enums;

public enum TipoOperacion {
    DEBITO("DEBITO"),
    CREDITO("CREDITO");

    // Constructor vacío para que no se pueda instanciar    
    private String detalle;

    // Constructor con parámetro
    private TipoOperacion(String detalle) {
        this.detalle = detalle;
    }
    
    // Método para obtener el detalle de la operación
    public String getDetalle() {
        return detalle;
    }

    // Método para obtener el enumerado por el detalle de la operación
    public static TipoOperacion obtenerPorDetalle(String detalle) {
        for (TipoOperacion tipoOperacion : values()) {
            if (tipoOperacion.getDetalle().equals(detalle)) {
                return tipoOperacion;
            }
        }
        throw new IllegalArgumentException("No se ha encontrado un tipo de operacion con la descripcion");
    
}

}