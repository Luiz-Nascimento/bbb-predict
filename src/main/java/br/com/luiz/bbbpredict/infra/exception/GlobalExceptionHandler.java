package br.com.luiz.bbbpredict.infra.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorMessage> handleUntreatedExceptions(
            Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        logger.error("Erro não tratado no endpoint: {}", request.getRequestURI(), e);

        RestErrorMessage response = new RestErrorMessage(
                Instant.now(),
                status.value(),
                "Internal Server Error",
                "An Unexpected error has occurred",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(response);


    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<RestErrorMessage> handleHttpRequestMethodNotSupportedExceptions(
            HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        RestErrorMessage response = new RestErrorMessage(
                Instant.now(),
                status.value(),
                "HTTP Request method not supported",
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RestErrorMessage> handleNoResourceFoundExceptions(
            NoResourceFoundException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        RestErrorMessage response = new RestErrorMessage(
                Instant.now(),
                status.value(),
                "No resource found",
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RestErrorMessage> handleResourceNotFoundExceptions(
            ResourceNotFoundException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        RestErrorMessage response = new RestErrorMessage(
                Instant.now(),
                status.value(),
                "Resource not found",
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(InvalidClobTokenIdException.class)
    public ResponseEntity<RestErrorMessage> handleInvalidClobTokenIdExceptions(
            InvalidClobTokenIdException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        RestErrorMessage response = new RestErrorMessage(
                Instant.now(),
                status.value(),
                "Invalid Clob Token Id",
                exception.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }


}
