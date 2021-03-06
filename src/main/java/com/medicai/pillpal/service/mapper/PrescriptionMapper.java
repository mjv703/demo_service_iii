package com.medicai.pillpal.service.mapper;

import com.medicai.pillpal.domain.MobileDevice;
import com.medicai.pillpal.domain.Prescription;
import com.medicai.pillpal.service.dto.PrescriptionDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Prescription} and its DTO {@link PrescriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = { PatientInfoMapper.class, MedicineMapper.class, PharmacyMapper.class })
public interface PrescriptionMapper extends EntityMapper<PrescriptionDTO, Prescription> {
    @Mapping(target = "patientInfo", source = "patientInfo", qualifiedByName = "id")
    @Mapping(target = "medicine", source = "medicine", qualifiedByName = "id")
    @Mapping(target = "pharmacy", source = "pharmacy", qualifiedByName = "id")
    PrescriptionDTO toDto(Prescription s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PrescriptionDTO toDtoId(Prescription prescription);

    PrescriptionDTO toDto(MobileDevice mobileDevice);
}
