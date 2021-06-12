package com.medicai.pillpal.service.mapper;

import com.medicai.pillpal.domain.Pharmacy;
import com.medicai.pillpal.service.dto.PharmacyDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Pharmacy} and its DTO {@link PharmacyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PharmacyMapper extends EntityMapper<PharmacyDTO, Pharmacy> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PharmacyDTO toDtoId(Pharmacy pharmacy);
}
