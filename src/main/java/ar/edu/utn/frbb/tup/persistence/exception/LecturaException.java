package ar.edu.utn.frbb.tup.persistence.exception;


public class LecturaException extends Exception {

    public LecturaException(String message, Throwable cause) {
        super(message,cause);
    }

    public LecturaException(String message){
        super(message);
    }


}