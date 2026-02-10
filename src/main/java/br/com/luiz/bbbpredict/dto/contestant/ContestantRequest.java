package br.com.luiz.bbbpredict.dto.contestant;

import jakarta.validation.constraints.NotEmpty;

public record ContestantRequest(
        @NotEmpty(message = "Field name can not be empty")
        String name,
        String clobTokenId
) {
}
