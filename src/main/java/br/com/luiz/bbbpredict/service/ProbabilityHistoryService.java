package br.com.luiz.bbbpredict.service;

import br.com.luiz.bbbpredict.dto.probability.ContestantWinProbabilityResponse;
import br.com.luiz.bbbpredict.dto.probability.ProbabilityHistoryResponse;
import br.com.luiz.bbbpredict.infra.exception.ResourceNotFoundException;
import br.com.luiz.bbbpredict.mapper.ProbabilityHistoryMapper;
import br.com.luiz.bbbpredict.model.Contestant;
import br.com.luiz.bbbpredict.model.ProbabilityHistory;
import br.com.luiz.bbbpredict.repository.ContestantRepository;
import br.com.luiz.bbbpredict.repository.ProbabilityHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;

@Service
public class ProbabilityHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(ProbabilityHistoryService.class);
    private final ProbabilityHistoryRepository probabilityHistoryRepository;
    private final MarketService marketService;
    private final ContestantRepository contestantRepository;
    private final ProbabilityHistoryMapper probabilityHistoryMapper;

    public ProbabilityHistoryService(ProbabilityHistoryRepository probabilityHistoryRepository, MarketService marketService, ContestantRepository contestantRepository, ProbabilityHistoryMapper probabilityHistoryMapper) {
        this.probabilityHistoryRepository = probabilityHistoryRepository;
        this.marketService = marketService;
        this.contestantRepository = contestantRepository;
        this.probabilityHistoryMapper = probabilityHistoryMapper;
    }

    public List<ProbabilityHistoryResponse> findAll() {
        return probabilityHistoryMapper.toDtoList(probabilityHistoryRepository.findAll());
    }

    public ProbabilityHistoryResponse findById(Long id) {
        ProbabilityHistory probabilityHistory = probabilityHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        return probabilityHistoryMapper.toDto(probabilityHistory);
    }


    public ProbabilityHistoryResponse saveWinProbabilityByContestantId(Long contestantId) {
        Contestant contestant = contestantRepository.findById(contestantId)
                .orElseThrow(() -> new ResourceNotFoundException(contestantId));

        ProbabilityHistory probabilityHistoryPersisted = fetchAndSaveProbability(contestant);

        return probabilityHistoryMapper.toDto(probabilityHistoryPersisted);
    }

    @Scheduled(cron = "0 0 14 * * *", zone = "America/Sao_Paulo")
    public void updateAllProbabilities() {
        logger.info("Updating all contestants win probabilities");

        List<Contestant> activeContestants = contestantRepository.findByActiveTrue();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            activeContestants.forEach(contestant -> executor.submit(()-> {
                try {
                    fetchAndSaveProbability(contestant);
                    logger.info("Updated contestant: {}", contestant.getName());
                } catch (Exception e) {
                    logger.error("Error while updating {} probability", contestant.getName());
                }

            }));
        }
    }

    public void delete(Long id) {
        if (!probabilityHistoryRepository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        probabilityHistoryRepository.deleteById(id);
    }

    private ProbabilityHistory fetchAndSaveProbability(Contestant contestant) {
        Double buyPrice = marketService.fetchBuyPrice(contestant.getClobTokenId());
        Double sellPrice = marketService.fetchSellPrice(contestant.getClobTokenId());

        BigDecimal probability = calculateMidpoint(buyPrice, sellPrice);

        ProbabilityHistory probabilityHistory = new ProbabilityHistory();
        probabilityHistory.setProbability(probability);
        probabilityHistory.setContestant(contestant);
        probabilityHistory.setTimestamp(Instant.now());
        return probabilityHistoryRepository.save(probabilityHistory);
    }
    private BigDecimal calculateMidpoint(Double buy, Double sell) {
        // Converte para BigDecimal
        BigDecimal bBuy = BigDecimal.valueOf(buy != null ? buy : 0.0);
        BigDecimal bSell = BigDecimal.valueOf(sell != null ? sell : 0.0);

        // (Buy + Sell) / 2
        // RoundingMode.HALF_EVEN é o padrão bancário para arredondamento
        return bBuy.add(bSell)
                .divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_EVEN);
    }

}
