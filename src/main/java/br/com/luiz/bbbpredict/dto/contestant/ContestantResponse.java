package br.com.luiz.bbbpredict.dto.contestant;

public record ContestantResponse(
        Long id,
        String name,
        String clobTokenId,
        Boolean active
) {
}
