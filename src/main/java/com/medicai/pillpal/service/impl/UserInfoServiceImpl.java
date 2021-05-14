package com.medicai.pillpal.service.impl;

import com.medicai.pillpal.domain.UserInfo;
import com.medicai.pillpal.repository.UserInfoRepository;
import com.medicai.pillpal.service.UserInfoService;
import com.medicai.pillpal.service.dto.UserInfoDTO;
import com.medicai.pillpal.service.mapper.UserInfoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserInfo}.
 */
@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    private final Logger log = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    private final UserInfoRepository userInfoRepository;

    private final UserInfoMapper userInfoMapper;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, UserInfoMapper userInfoMapper) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public UserInfoDTO save(UserInfoDTO userInfoDTO) {
        log.debug("Request to save UserInfo : {}", userInfoDTO);
        UserInfo userInfo = userInfoMapper.toEntity(userInfoDTO);
        userInfo = userInfoRepository.save(userInfo);
        return userInfoMapper.toDto(userInfo);
    }

    @Override
    public Optional<UserInfoDTO> partialUpdate(UserInfoDTO userInfoDTO) {
        log.debug("Request to partially update UserInfo : {}", userInfoDTO);

        return userInfoRepository
            .findById(userInfoDTO.getId())
            .map(
                existingUserInfo -> {
                    userInfoMapper.partialUpdate(existingUserInfo, userInfoDTO);
                    return existingUserInfo;
                }
            )
            .map(userInfoRepository::save)
            .map(userInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UserInfos");
        return userInfoRepository.findAll(pageable).map(userInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserInfoDTO> findOne(Long id) {
        log.debug("Request to get UserInfo : {}", id);
        return userInfoRepository.findById(id).map(userInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserInfo : {}", id);
        userInfoRepository.deleteById(id);
    }
}
