package br.com.luiz.bbbpredict.service;

import br.com.luiz.bbbpredict.dto.contestant.ContestantPatch;
import br.com.luiz.bbbpredict.dto.contestant.ContestantRequest;
import br.com.luiz.bbbpredict.dto.contestant.ContestantResponse;
import br.com.luiz.bbbpredict.infra.exception.ResourceNotFoundException;
import br.com.luiz.bbbpredict.mapper.ContestantMapper;
import br.com.luiz.bbbpredict.model.Contestant;
import br.com.luiz.bbbpredict.repository.ContestantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContestantService {

    private final ContestantRepository contestantRepository;
    private final ContestantMapper contestantMapper;

    public ContestantService(ContestantRepository contestantRepository, ContestantMapper contestantMapper) {
        this.contestantRepository = contestantRepository;
        this.contestantMapper = contestantMapper;
    }

    public ContestantResponse findById(Long id) {
        return contestantMapper.toDto(contestantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id)));
    }
    public List<ContestantResponse> findAll() {
        return contestantMapper.toDtoList(contestantRepository.findAll());
    }
    public ContestantResponse create(ContestantRequest request) {
        Contestant contestant = contestantMapper.toEntity(request);
        ContestantResponse response = contestantMapper.toDto(contestantRepository.save(contestant));
        return response;
    }
    @Transactional
    public ContestantResponse patch(Long id, ContestantPatch patch) {
        Contestant contestant = contestantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        Contestant contestantUpdated = contestantMapper.patchFromDto(patch, contestant);
        return contestantMapper.toDto(contestantRepository.save(contestantUpdated));

    }
    @Transactional
    public void deactivate(Long id) {
        Contestant contestant = contestantRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(id));

        contestant.setActive(false);

    }
}
