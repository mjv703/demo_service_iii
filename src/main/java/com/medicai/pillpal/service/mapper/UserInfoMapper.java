package com.medicai.pillpal.service.mapper;

import com.medicai.pillpal.domain.*;
import com.medicai.pillpal.service.dto.UserInfoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserInfo} and its DTO {@link UserInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface UserInfoMapper extends EntityMapper<UserInfoDTO, UserInfo> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    UserInfoDTO toDto(UserInfo s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserInfoDTO toDtoId(UserInfo userInfo);
}
