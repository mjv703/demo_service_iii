package com.medicai.pillpal.service.impl;

import com.medicai.pillpal.domain.PatientInfo;
import com.medicai.pillpal.repository.PatientInfoRepository;
import com.medicai.pillpal.service.PatientInfoService;
import com.medicai.pillpal.service.dto.PatientInfoDTO;
import com.medicai.pillpal.service.mapper.PatientInfoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PatientInfo}.
 */
@Service
@Transactional
public class PatientInfoServiceImpl implements PatientInfoService {

    private final Logger log = LoggerFactory.getLogger(PatientInfoServiceImpl.class);

    private final PatientInfoRepository patientInfoRepository;

    private final PatientInfoMapper patientInfoMapper;

    public PatientInfoServiceImpl(PatientInfoRepository patientInfoRepository, PatientInfoMapper patientInfoMapper) {
        this.patientInfoRepository = patientInfoRepository;
        this.patientInfoMapper = patientInfoMapper;
    }

    @Override
    public PatientInfoDTO save(PatientInfoDTO patientInfoDTO) {
        log.debug("Request to save PatientInfo : {}", patientInfoDTO);
        PatientInfo patientInfo = patientInfoMapper.toEntity(patientInfoDTO);
        patientInfo = patientInfoRepository.save(patientInfo);
        return patientInfoMapper.toDto(patientInfo);
    }

    @Override
    public Optional<PatientInfoDTO> partialUpdate(PatientInfoDTO patientInfoDTO) {
        log.debug("Request to partially update PatientInfo : {}", patientInfoDTO);

        return patientInfoRepository
            .findById(patientInfoDTO.getId())
            .map(
                existingPatientInfo -> {
                    patientInfoMapper.partialUpdate(existingPatientInfo, patientInfoDTO);
                    return existingPatientInfo;
                }
            )
            .map(patientInfoRepository::save)
            .map(patientInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PatientInfos");
        return patientInfoRepository.findAll(pageable).map(patientInfoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PatientInfoDTO> findOne(Long id) {
        log.debug("Request to get PatientInfo : {}", id);
        return patientInfoRepository.findById(id).map(patientInfoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PatientInfo : {}", id);
        patientInfoRepository.deleteById(id);
    }
}
