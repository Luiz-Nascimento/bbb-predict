package br.com.luiz.bbbpredict.service;

import br.com.luiz.bbbpredict.dto.probability.ContestantWinProbabilityResponse;
import br.com.luiz.bbbpredict.dto.probability.ProbabilityHistoryResponse;
import br.com.luiz.bbbpredict.mapper.ProbabilityHistoryMapper;
import br.com.luiz.bbbpredict.model.Contestant;
import br.com.luiz.bbbpredict.model.ProbabilityHistory;
import br.com.luiz.bbbpredict.repository.ContestantRepository;
import br.com.luiz.bbbpredict.repository.ProbabilityHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Service
public class ProbabilityHistoryService {

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


    public ProbabilityHistoryResponse saveWinProbabilityByContestantId(Long contestantId) {
        Contestant contestant = contestantRepository.findById(contestantId)
                .orElseThrow(() -> new EntityNotFoundException("Contestant not found with given id"));
        Double buyPrice = marketService.fetchBuyPrice(contestant.getClobTokenId());
        Double sellPrice = marketService.fetchSellPrice(contestant.getClobTokenId());
        BigDecimal probability = calculateMidpoint(buyPrice, sellPrice);
        ProbabilityHistory probabilityHistory = new ProbabilityHistory();
        probabilityHistory.setProbability(probability);
        probabilityHistory.setContestant(contestant);
        probabilityHistory.setTimestamp(Instant.now());

        ProbabilityHistory probabilityHistoryPersisted = probabilityHistoryRepository.save(probabilityHistory);

        return probabilityHistoryMapper.toDto(probabilityHistoryPersisted);


    }
    public BigDecimal calculateMidpoint(Double buy, Double sell) {
        // Converte para BigDecimal
        BigDecimal bBuy = BigDecimal.valueOf(buy != null ? buy : 0.0);
        BigDecimal bSell = BigDecimal.valueOf(sell != null ? sell : 0.0);

        // (Buy + Sell) / 2
        // RoundingMode.HALF_EVEN é o padrão bancário para arredondamento
        return bBuy.add(bSell)
                .divide(BigDecimal.valueOf(2), 4, RoundingMode.HALF_EVEN);
    }
}
