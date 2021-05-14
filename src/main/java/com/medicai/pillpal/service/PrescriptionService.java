package com.medicai.pillpal.service;

import com.medicai.pillpal.service.dto.PrescriptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medicai.pillpal.domain.Prescription}.
 */
public interface PrescriptionService {
    /**
     * Save a prescription.
     *
     * @param prescriptionDTO the entity to save.
     * @return the persisted entity.
     */
    PrescriptionDTO save(PrescriptionDTO prescriptionDTO);

    /**
     * Partially updates a prescription.
     *
     * @param prescriptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PrescriptionDTO> partialUpdate(PrescriptionDTO prescriptionDTO);

    /**
     * Get all the prescriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PrescriptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" prescription.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PrescriptionDTO> findOne(Long id);

    /**
     * Delete the "id" prescription.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
