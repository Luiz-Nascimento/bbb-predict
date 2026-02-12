package br.com.luiz.bbbpredict.dto.probability;

public record ContestantWinProbabilityResponse(
        String contestantName,
        Double probability
) {
}
