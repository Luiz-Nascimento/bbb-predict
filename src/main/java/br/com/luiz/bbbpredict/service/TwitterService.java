package br.com.luiz.bbbpredict.service;

import br.com.luiz.bbbpredict.dto.probability.ProbabilityHistoryResponse;
import br.com.luiz.bbbpredict.dto.twitter.TweetDataDto;
import br.com.luiz.bbbpredict.model.ProbabilityHistory;
import br.com.luiz.bbbpredict.repository.ProbabilityHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class TwitterService {

    private static final Logger log = LoggerFactory.getLogger(TwitterService.class);


    private final ProbabilityHistoryService probabilityHistoryService;

    public TwitterService(ProbabilityHistoryService probabilityHistoryService) {

        this.probabilityHistoryService = probabilityHistoryService;
    }

    public String tweetGenerator(Long contestantId) {
        List<ProbabilityHistory> extractedData = probabilityHistoryService
                .findLatestTwoProbabilityByContestantId(contestantId);
        String contestantName = extractedData.getFirst().getContestant().getName();
        BigDecimal winPercentage = extractedData.getFirst().getProbability().multiply(BigDecimal.valueOf(100));
        BigDecimal currentWinProbability = extractedData.getFirst().getProbability();
        BigDecimal previousWinProbability = extractedData.get(1).getProbability();
        BigDecimal variationPercentage = currentWinProbability.subtract(previousWinProbability)
                .multiply(BigDecimal.valueOf(100));

        String variationFormatted = String.format(
                "%+.2f",
                variationPercentage
        );


        return """
              Probabilidade de vencer segundo dados do Polymarket:
              
              Participante: %s
              Chance de vencer: %.2f%%
              Variação em relação a última coleta: %s%%
              
              #BBB26 #BBBPredict
              """.formatted(
                      contestantName, winPercentage, variationFormatted);
    }






}
