package br.com.luiz.bbbpredict.infra.exception;

public class InvalidClobTokenIdException extends RuntimeException {
    public InvalidClobTokenIdException(Object id) {
        super("Invalid Clob Token Id: " + id);
    }
}
