package br.com.luiz.bbbpredict.mapper;

import br.com.luiz.bbbpredict.dto.contestant.ContestantPatch;
import br.com.luiz.bbbpredict.dto.contestant.ContestantRequest;
import br.com.luiz.bbbpredict.dto.contestant.ContestantResponse;
import br.com.luiz.bbbpredict.model.Contestant;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContestantMapper {

    ContestantResponse toDto(Contestant contestant);
    List<ContestantResponse> toDtoList(List<Contestant> contestants);
    Contestant toEntity(ContestantRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Contestant patchFromDto(ContestantPatch contestantPatch, @MappingTarget Contestant contestant);

}
