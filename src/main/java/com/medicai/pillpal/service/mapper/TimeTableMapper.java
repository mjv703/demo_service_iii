package com.medicai.pillpal.service.mapper;

import com.medicai.pillpal.domain.TimeTable;
import com.medicai.pillpal.service.dto.TimeTableDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link TimeTable} and its DTO {@link TimeTableDTO}.
 */
@Mapper(componentModel = "spring", uses = { PrescriptionMapper.class })
public interface TimeTableMapper extends EntityMapper<TimeTableDTO, TimeTable> {
    @Mapping(target = "prescription", source = "prescription", qualifiedByName = "id")
    TimeTableDTO toDto(TimeTable s);
}
