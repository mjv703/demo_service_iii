package com.medicai.pillpal.service.impl;

import com.medicai.pillpal.domain.Prescription;
import com.medicai.pillpal.repository.PrescriptionRepository;
import com.medicai.pillpal.service.PrescriptionService;
import com.medicai.pillpal.service.dto.PrescriptionDTO;
import com.medicai.pillpal.service.mapper.PrescriptionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Prescription}.
 */
@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final Logger log = LoggerFactory.getLogger(PrescriptionServiceImpl.class);

    private final PrescriptionRepository prescriptionRepository;

    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, PrescriptionMapper prescriptionMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionMapper = prescriptionMapper;
    }

    @Override
    public PrescriptionDTO save(PrescriptionDTO prescriptionDTO) {
        log.debug("Request to save Prescription : {}", prescriptionDTO);
        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);
        prescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toDto(prescription);
    }

    @Override
    public Optional<PrescriptionDTO> partialUpdate(PrescriptionDTO prescriptionDTO) {
        log.debug("Request to partially update Prescription : {}", prescriptionDTO);

        return prescriptionRepository
            .findById(prescriptionDTO.getId())
            .map(
                existingPrescription -> {
                    prescriptionMapper.partialUpdate(existingPrescription, prescriptionDTO);
                    return existingPrescription;
                }
            )
            .map(prescriptionRepository::save)
            .map(prescriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrescriptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Prescriptions");
        return prescriptionRepository.findAll(pageable).map(prescriptionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrescriptionDTO> findOne(Long id) {
        log.debug("Request to get Prescription : {}", id);
        return prescriptionRepository.findById(id).map(prescriptionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Prescription : {}", id);
        prescriptionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrescriptionDTO> findAllByPatientId(Pageable pageable, Long id) {
        log.debug("Request to get all Prescriptions");
        return prescriptionRepository.findAllByPatientInfoId(pageable, id).map(prescriptionMapper::toDto);
    }
}
