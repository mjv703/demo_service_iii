package com.medicai.pillpal.service.impl;

import com.medicai.pillpal.domain.SideEffect;
import com.medicai.pillpal.repository.SideEffectRepository;
import com.medicai.pillpal.service.SideEffectService;
import com.medicai.pillpal.service.dto.SideEffectDTO;
import com.medicai.pillpal.service.mapper.SideEffectMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SideEffect}.
 */
@Service
@Transactional
public class SideEffectServiceImpl implements SideEffectService {

    private final Logger log = LoggerFactory.getLogger(SideEffectServiceImpl.class);

    private final SideEffectRepository sideEffectRepository;

    private final SideEffectMapper sideEffectMapper;

    public SideEffectServiceImpl(SideEffectRepository sideEffectRepository, SideEffectMapper sideEffectMapper) {
        this.sideEffectRepository = sideEffectRepository;
        this.sideEffectMapper = sideEffectMapper;
    }

    @Override
    public SideEffectDTO save(SideEffectDTO sideEffectDTO) {
        log.debug("Request to save SideEffect : {}", sideEffectDTO);
        SideEffect sideEffect = sideEffectMapper.toEntity(sideEffectDTO);
        sideEffect = sideEffectRepository.save(sideEffect);
        return sideEffectMapper.toDto(sideEffect);
    }

    @Override
    public Optional<SideEffectDTO> partialUpdate(SideEffectDTO sideEffectDTO) {
        log.debug("Request to partially update SideEffect : {}", sideEffectDTO);

        return sideEffectRepository
            .findById(sideEffectDTO.getId())
            .map(
                existingSideEffect -> {
                    sideEffectMapper.partialUpdate(existingSideEffect, sideEffectDTO);
                    return existingSideEffect;
                }
            )
            .map(sideEffectRepository::save)
            .map(sideEffectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SideEffectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SideEffects");
        return sideEffectRepository.findAll(pageable).map(sideEffectMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SideEffectDTO> findOne(Long id) {
        log.debug("Request to get SideEffect : {}", id);
        return sideEffectRepository.findById(id).map(sideEffectMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SideEffect : {}", id);
        sideEffectRepository.deleteById(id);
    }
}
