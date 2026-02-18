package br.com.luiz.bbbpredict.scheduler;

import br.com.luiz.bbbpredict.model.Contestant;
import br.com.luiz.bbbpredict.model.ProbabilityHistory;
import br.com.luiz.bbbpredict.repository.ContestantRepository;
import br.com.luiz.bbbpredict.repository.ProbabilityHistoryRepository;
import br.com.luiz.bbbpredict.service.ContestantService;
import br.com.luiz.bbbpredict.service.ProbabilityHistoryService;
import br.com.luiz.bbbpredict.service.TwitterPublisher;
import br.com.luiz.bbbpredict.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PredictionScheduler {

    private static final Logger log = LoggerFactory.getLogger(PredictionScheduler.class);

    private final TwitterService twitterService;
    private final TwitterPublisher twitterPublisher;
    private final ContestantService contestantService;

    public PredictionScheduler(TwitterService twitterService, TwitterPublisher twitterPublisher, ProbabilityHistoryService probabilityHistoryService, ContestantService contestantService) {
        this.twitterService = twitterService;
        this.twitterPublisher = twitterPublisher;
        this.contestantService = contestantService;
    }
    @Scheduled(cron = "0 0 15 * * *", zone = "America/Sao_Paulo")
    public void runRoutine() {
        log.info("Starting daily tweet post routine...");

        List<Contestant> contestants = contestantService.findAllActive();

        if (contestants.isEmpty()) {
            log.warn("Contestants list is empty.");
            return;
        }

        for (Contestant contestant : contestants) {
            try {
                log.debug("Publishing tweet from contestant: {}", contestant.getName());
                String tweetContent = twitterService.tweetGenerator(contestant.getId());
                twitterPublisher.publishTweet(tweetContent);
                log.debug("Tweet published successfully for contestant: {}, initiating 4 minutes pause.", contestant.getName());
                Thread.sleep(240000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread interrupted during sleep.", e);
                return;
            } catch (Exception e) {
                log.error("An unexpected error has occurred while publishing tweet for contestant: {} ",contestant.getName(), e);
            }

        }

        log.info("Daily tweet post routine finished");
    }
}

