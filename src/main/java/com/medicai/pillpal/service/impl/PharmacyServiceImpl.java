package com.medicai.pillpal.service.impl;

import com.medicai.pillpal.domain.Pharmacy;
import com.medicai.pillpal.repository.PharmacyRepository;
import com.medicai.pillpal.service.PharmacyService;
import com.medicai.pillpal.service.dto.PharmacyDTO;
import com.medicai.pillpal.service.mapper.PharmacyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pharmacy}.
 */
@Service
@Transactional
public class PharmacyServiceImpl implements PharmacyService {

    private final Logger log = LoggerFactory.getLogger(PharmacyServiceImpl.class);

    private final PharmacyRepository pharmacyRepository;

    private final PharmacyMapper pharmacyMapper;

    public PharmacyServiceImpl(PharmacyRepository pharmacyRepository, PharmacyMapper pharmacyMapper) {
        this.pharmacyRepository = pharmacyRepository;
        this.pharmacyMapper = pharmacyMapper;
    }

    @Override
    public PharmacyDTO save(PharmacyDTO pharmacyDTO) {
        log.debug("Request to save Pharmacy : {}", pharmacyDTO);
        Pharmacy pharmacy = pharmacyMapper.toEntity(pharmacyDTO);
        pharmacy = pharmacyRepository.save(pharmacy);
        return pharmacyMapper.toDto(pharmacy);
    }

    @Override
    public Optional<PharmacyDTO> partialUpdate(PharmacyDTO pharmacyDTO) {
        log.debug("Request to partially update Pharmacy : {}", pharmacyDTO);

        return pharmacyRepository
            .findById(pharmacyDTO.getId())
            .map(
                existingPharmacy -> {
                    pharmacyMapper.partialUpdate(existingPharmacy, pharmacyDTO);
                    return existingPharmacy;
                }
            )
            .map(pharmacyRepository::save)
            .map(pharmacyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PharmacyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pharmacies");
        return pharmacyRepository.findAll(pageable).map(pharmacyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PharmacyDTO> findOne(Long id) {
        log.debug("Request to get Pharmacy : {}", id);
        return pharmacyRepository.findById(id).map(pharmacyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pharmacy : {}", id);
        pharmacyRepository.deleteById(id);
    }
}
