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
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class PredictionScheduler {

    private static final Logger log = LoggerFactory.getLogger(PredictionScheduler.class);

    private final TwitterService twitterService;
    private final TwitterPublisher twitterPublisher;
    private final ContestantService contestantService;
    private final TaskScheduler taskScheduler;

    public PredictionScheduler(TwitterService twitterService, TwitterPublisher twitterPublisher, ProbabilityHistoryService probabilityHistoryService, ContestantService contestantService, TaskScheduler taskScheduler) {
        this.twitterService = twitterService;
        this.twitterPublisher = twitterPublisher;
        this.contestantService = contestantService;
        this.taskScheduler = taskScheduler;
    }
    @Scheduled(cron = "0 0 16 * * *", zone = "America/Sao_Paulo")
    public void planDailyTweets() {
        log.info("Planning daily tweet post routine...");

        List<Contestant> contestants = contestantService.findAllActive();

        if (contestants.isEmpty()) {
            log.warn("Contestants list is empty.");
            return;
        }

        long delayMs = 0;

        for (Contestant contestant : contestants) {

            long randomInterval = ThreadLocalRandom.current().nextLong(240000, 600000);

            delayMs += randomInterval;

            Instant executionTime = Instant.now().plusMillis(delayMs);

            log.debug("Scheduling tweet for contestant: {} at {}", contestant.getName(), executionTime);

            taskScheduler.schedule(() -> publishTweetForContestant(contestant), executionTime);
        }

        log.info("Daily tweet post routine scheduled successfully for {} contestants.", contestants.size());
    }

    private void publishTweetForContestant(Contestant contestant) {
        try {
            log.debug("Publishing tweet from contestant: {}", contestant.getName());
            String tweetContent = twitterService.tweetGenerator(contestant.getId());
            twitterPublisher.publishTweet(tweetContent);
            log.info("Tweet published successfully for contestant: {}", contestant.getName());
        } catch (Exception e) {
            log.error("An unexpected error has occurred while publishing tweet for contestant: {}", contestant.getName(), e);
        }
    }
}

