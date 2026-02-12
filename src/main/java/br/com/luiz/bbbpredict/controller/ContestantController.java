package br.com.luiz.bbbpredict.controller;

import br.com.luiz.bbbpredict.dto.contestant.ContestantPatch;
import br.com.luiz.bbbpredict.dto.contestant.ContestantRequest;
import br.com.luiz.bbbpredict.dto.contestant.ContestantResponse;
import br.com.luiz.bbbpredict.service.ContestantService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contestants")
public class ContestantController {

    private final ContestantService contestantService;

    public ContestantController(ContestantService contestantService) {
        this.contestantService = contestantService;
    }

    @GetMapping
    public List<ContestantResponse> findAll() {
        return contestantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContestantResponse> findById(@PathVariable Long id) {
        ContestantResponse response = contestantService.findById(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ContestantResponse> create(@RequestBody @Valid ContestantRequest request) {
        ContestantResponse response = contestantService.create(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ContestantResponse> patch(@PathVariable Long id,
                                                    @RequestBody ContestantPatch patch) {
        ContestantResponse response = contestantService.patch(id, patch);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        contestantService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
