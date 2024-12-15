package ar.edu.utn.frbb.tup.persistence.exception;

public class ErrorArchivoException  extends Exception {
    public ErrorArchivoException(String message) {
        super(message);
    }

    public ErrorArchivoException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
