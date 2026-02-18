package br.com.luiz.bbbpredict.dto.twitter;

import java.math.BigDecimal;

public record TweetDataDto(
        BigDecimal currentProbabilityPercent,
        String contestantName
) {
}
