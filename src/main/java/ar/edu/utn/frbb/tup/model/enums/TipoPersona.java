package ar.edu.utn.frbb.tup.model.enums;

public enum TipoPersona {

    PERSONA_FISICA("PERSONA_FISICA"),
    PERSONA_JURIDICA("PERSONA_JURIDICA");

    private final String descripcion;

    TipoPersona(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Metodo para parsear
    public static TipoPersona fromString(String text) {
        for (TipoPersona tipo : TipoPersona.values()) {
            if (tipo.descripcion.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("No se pudo encontrar un TipoPersona con la descripción: " + text);
    } 
}
