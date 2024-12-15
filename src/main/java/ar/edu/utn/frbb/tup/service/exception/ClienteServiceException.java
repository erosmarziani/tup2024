package ar.edu.utn.frbb.tup.service.exception;


public class ClienteServiceException extends Exception{
    public ClienteServiceException(String message, Throwable cause) {
     super(message, cause);
    }

    public ClienteServiceException(String message) {
        super(message);
    }
}
