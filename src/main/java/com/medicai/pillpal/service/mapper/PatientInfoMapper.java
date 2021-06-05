package com.medicai.pillpal.service.mapper;

import com.medicai.pillpal.domain.*;
import com.medicai.pillpal.service.dto.PatientInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PatientInfo} and its DTO {@link PatientInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserInfoMapper.class })
public interface PatientInfoMapper extends EntityMapper<PatientInfoDTO, PatientInfo> {
    @Mapping(target = "userInfo", source = "userInfo", qualifiedByName = "id")
    PatientInfoDTO toDto(PatientInfo s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientInfoDTO toDtoId(PatientInfo patientInfo);

    PatientInfoDTO toDto(MobileDevice mobileDevice);
}
