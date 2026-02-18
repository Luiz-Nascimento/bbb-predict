package br.com.luiz.bbbpredict.infra.exception;

public class TwitterIntegrationException extends RuntimeException {
    public TwitterIntegrationException(String message) {
        super(message);
    }
}
