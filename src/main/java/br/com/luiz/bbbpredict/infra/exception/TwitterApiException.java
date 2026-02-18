package br.com.luiz.bbbpredict.infra.exception;

import org.springframework.http.HttpStatusCode;

public class TwitterApiException extends RuntimeException {
    public TwitterApiException(String message, HttpStatusCode statusCode) {
        super(message);
    }
}
