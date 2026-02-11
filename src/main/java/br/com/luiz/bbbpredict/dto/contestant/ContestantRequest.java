package br.com.luiz.bbbpredict.dto.contestant;

import jakarta.validation.constraints.NotEmpty;

public record ContestantRequest(
        @NotEmpty(message = "Field name can not be empty")
        String name,
        @NotEmpty(message = "Field clobTokenId can not be empty")
        String clobTokenId
) {
}
