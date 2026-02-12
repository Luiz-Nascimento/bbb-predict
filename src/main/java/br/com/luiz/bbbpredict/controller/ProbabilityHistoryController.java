package br.com.luiz.bbbpredict.controller;

import br.com.luiz.bbbpredict.dto.probability.ProbabilityHistoryResponse;
import br.com.luiz.bbbpredict.service.ProbabilityHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/probability-history")
public class ProbabilityHistoryController {

    private final ProbabilityHistoryService probabilityHistoryService;

    public ProbabilityHistoryController(ProbabilityHistoryService probabilityHistoryService) {
        this.probabilityHistoryService = probabilityHistoryService;
    }
    @GetMapping
    public List<ProbabilityHistoryResponse> listAll() {
        return probabilityHistoryService.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProbabilityHistoryResponse> findById(@PathVariable Long id) {
        ProbabilityHistoryResponse response = probabilityHistoryService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{contestantId}")
    public ResponseEntity<ProbabilityHistoryResponse> saveWinProbabilityHistoryByContestantId
            (@PathVariable Long contestantId){
        ProbabilityHistoryResponse response = probabilityHistoryService.saveWinProbabilityByContestantId(contestantId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        probabilityHistoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
