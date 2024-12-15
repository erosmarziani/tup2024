package ar.edu.utn.frbb.tup.service.exception;

public class CuentaServiceException extends Exception {
    public CuentaServiceException(String message) {
        super(message);
    }
    
    public CuentaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
