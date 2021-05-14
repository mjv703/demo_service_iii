package com.medicai.pillpal.service.mapper;

import com.medicai.pillpal.domain.*;
import com.medicai.pillpal.service.dto.MobileDeviceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MobileDevice} and its DTO {@link MobileDeviceDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserInfoMapper.class })
public interface MobileDeviceMapper extends EntityMapper<MobileDeviceDTO, MobileDevice> {
    @Mapping(target = "userInfo", source = "userInfo", qualifiedByName = "id")
    MobileDeviceDTO toDto(MobileDevice s);
}
