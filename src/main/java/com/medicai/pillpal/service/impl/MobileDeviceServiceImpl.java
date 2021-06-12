package com.medicai.pillpal.service.impl;

import com.medicai.pillpal.domain.MobileDevice;
import com.medicai.pillpal.repository.MobileDeviceRepository;
import com.medicai.pillpal.service.MobileDeviceService;
import com.medicai.pillpal.service.dto.MobileDeviceDTO;
import com.medicai.pillpal.service.mapper.MobileDeviceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MobileDevice}.
 */
@Service
@Transactional
public class MobileDeviceServiceImpl implements MobileDeviceService {

    private final Logger log = LoggerFactory.getLogger(MobileDeviceServiceImpl.class);

    private final MobileDeviceRepository mobileDeviceRepository;

    private final MobileDeviceMapper mobileDeviceMapper;

    public MobileDeviceServiceImpl(MobileDeviceRepository mobileDeviceRepository, MobileDeviceMapper mobileDeviceMapper) {
        this.mobileDeviceRepository = mobileDeviceRepository;
        this.mobileDeviceMapper = mobileDeviceMapper;
    }

    @Override
    public MobileDeviceDTO save(MobileDeviceDTO mobileDeviceDTO) {
        log.debug("Request to save MobileDevice : {}", mobileDeviceDTO);
        MobileDevice mobileDevice = mobileDeviceMapper.toEntity(mobileDeviceDTO);
        mobileDevice = mobileDeviceRepository.save(mobileDevice);
        return mobileDeviceMapper.toDto(mobileDevice);
    }

    @Override
    public Optional<MobileDeviceDTO> partialUpdate(MobileDeviceDTO mobileDeviceDTO) {
        log.debug("Request to partially update MobileDevice : {}", mobileDeviceDTO);

        return mobileDeviceRepository
            .findById(mobileDeviceDTO.getId())
            .map(
                existingMobileDevice -> {
                    mobileDeviceMapper.partialUpdate(existingMobileDevice, mobileDeviceDTO);
                    return existingMobileDevice;
                }
            )
            .map(mobileDeviceRepository::save)
            .map(mobileDeviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MobileDeviceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MobileDevices");
        return mobileDeviceRepository.findAll(pageable).map(mobileDeviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MobileDeviceDTO> findOne(Long id) {
        log.debug("Request to get MobileDevice : {}", id);
        return mobileDeviceRepository.findById(id).map(mobileDeviceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MobileDevice : {}", id);
        mobileDeviceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MobileDeviceDTO> findAllByAccountId(Pageable pageable, Long id) {
        log.debug("Request to get all MobileDevices");
        return mobileDeviceRepository.findAllByUserInfoId(pageable, id).map(mobileDeviceMapper::toDto);
    }
}
