package br.com.luiz.bbbpredict.service;

import br.com.luiz.bbbpredict.dto.twitter.TweetDataDto;
import br.com.luiz.bbbpredict.model.Contestant;
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

    private final ProbabilityHistoryRepository probabilityHistoryRepository;

    public TwitterService(ProbabilityHistoryRepository probabilityHistoryRepository) {
        this.probabilityHistoryRepository = probabilityHistoryRepository;
    }

    public TweetDataDto extractTweetContent(Contestant contestant) {
        List<ProbabilityHistory> latestData = probabilityHistoryRepository
                .findFirst2ByContestantNameOrderByTimestampDesc(contestant.getName());

        BigDecimal currentProbability = latestData.getFirst().getProbability();
        BigDecimal previousProbability = latestData.get(1).getProbability();
        BigDecimal variation = currentProbability.subtract(previousProbability);
        String contestantName = contestant.getName();
        BigDecimal currentProbabilityPercent = currentProbability.multiply(BigDecimal.valueOf(100));
        BigDecimal variationPercent = variation.multiply(BigDecimal.valueOf(100));

        return new TweetDataDto(
                currentProbabilityPercent,
                variationPercent,
                contestantName
        );
    }

    public String generateTweetText(TweetDataDto data) {
        int comparison = data.variationPercent().compareTo(BigDecimal.ZERO);

        String emoji;
        String sign = "";

        if (comparison > 0) {
            emoji = "🚀 Subiu!";
            sign = "+";
        } else if (comparison < 0) {
            emoji = "🔻 Caiu...";
        } else {
            emoji = "➖ Estável";
        }

        String nameHashtag = "#" + data.contestantName().replace(" ", "");

        return """
           📊 Probabilidades #BBB26
           
           👤 %s
           
           🔥 Chance: %.2f%%
           📉 Variação (24h): %s%.2f%% | %s
           
           %s #RedeBBB #BotBBB
           """.formatted(
                data.contestantName(),
                data.currentProbabilityPercent(),
                sign,
                data.variationPercent(),
                emoji,
                nameHashtag
        );
    }
}
