package com.medicai.pillpal.service;

import com.medicai.pillpal.service.dto.PharmacyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medicai.pillpal.domain.Pharmacy}.
 */
public interface PharmacyService {
    /**
     * Save a pharmacy.
     *
     * @param pharmacyDTO the entity to save.
     * @return the persisted entity.
     */
    PharmacyDTO save(PharmacyDTO pharmacyDTO);

    /**
     * Partially updates a pharmacy.
     *
     * @param pharmacyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PharmacyDTO> partialUpdate(PharmacyDTO pharmacyDTO);

    /**
     * Get all the pharmacies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PharmacyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pharmacy.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PharmacyDTO> findOne(Long id);

    /**
     * Delete the "id" pharmacy.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
