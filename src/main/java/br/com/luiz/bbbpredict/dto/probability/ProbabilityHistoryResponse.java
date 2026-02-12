package br.com.luiz.bbbpredict.dto.probability;

import java.math.BigDecimal;
import java.time.Instant;

public record ProbabilityHistoryResponse(
        Long id,
        String contestantName,
        BigDecimal probability,
        Instant timestamp
) {
}
