package ar.edu.utn.frbb.tup.persistence.exception;

public class GuardadoException extends Exception{
    public GuardadoException(String message, Throwable cause)  {
        super(message, cause);
    }
    public GuardadoException(String message)  {
        super(message);
}
}
