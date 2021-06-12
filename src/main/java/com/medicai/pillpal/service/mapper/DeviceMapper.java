package com.medicai.pillpal.service.mapper;

import com.medicai.pillpal.domain.Device;
import com.medicai.pillpal.service.dto.DeviceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Device} and its DTO {@link DeviceDTO}.
 */
@Mapper(componentModel = "spring", uses = { PatientInfoMapper.class, PrescriptionMapper.class })
public interface DeviceMapper extends EntityMapper<DeviceDTO, Device> {
    @Mapping(target = "patientInfo", source = "patientInfo", qualifiedByName = "id")
    @Mapping(target = "prescription", source = "prescription", qualifiedByName = "id")
    DeviceDTO toDto(Device s);
}
