package br.com.luiz.bbbpredict.mapper;

import br.com.luiz.bbbpredict.dto.probability.ProbabilityHistoryResponse;
import br.com.luiz.bbbpredict.model.ProbabilityHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProbabilityHistoryMapper {

    @Mapping(source = "contestant.name", target = "contestantName")
    ProbabilityHistoryResponse toDto(ProbabilityHistory probabilityHistory);
    List<ProbabilityHistoryResponse> toDtoList(List<ProbabilityHistory> probabilityHistoryList);
}
