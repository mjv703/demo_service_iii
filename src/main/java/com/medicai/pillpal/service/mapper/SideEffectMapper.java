package com.medicai.pillpal.service.mapper;

import com.medicai.pillpal.domain.SideEffect;
import com.medicai.pillpal.service.dto.SideEffectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link SideEffect} and its DTO {@link SideEffectDTO}.
 */
@Mapper(componentModel = "spring", uses = { MedicineMapper.class })
public interface SideEffectMapper extends EntityMapper<SideEffectDTO, SideEffect> {
    @Mapping(target = "medicine", source = "medicine", qualifiedByName = "id")
    SideEffectDTO toDto(SideEffect s);
}
