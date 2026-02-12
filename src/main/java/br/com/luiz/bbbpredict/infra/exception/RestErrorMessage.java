package br.com.luiz.bbbpredict.infra.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RestErrorMessage(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path,
        List<FieldMessage> validations
) {
    public RestErrorMessage(Instant timestamp, Integer status, String error, String message, String path) {
        this(timestamp, status, error, message, path, null);
    }
}
