package com.medicai.pillpal.service.mapper;

import com.medicai.pillpal.domain.*;
import com.medicai.pillpal.service.dto.MedicineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medicine} and its DTO {@link MedicineDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MedicineMapper extends EntityMapper<MedicineDTO, Medicine> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedicineDTO toDtoId(Medicine medicine);
}
