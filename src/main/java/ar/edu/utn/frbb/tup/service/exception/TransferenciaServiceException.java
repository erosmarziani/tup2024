package ar.edu.utn.frbb.tup.service.exception;

public class TransferenciaServiceException extends Exception{
    public TransferenciaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public TransferenciaServiceException(String message){
        super(message);
    }
}
