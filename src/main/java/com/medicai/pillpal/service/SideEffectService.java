package com.medicai.pillpal.service;

import com.medicai.pillpal.service.dto.SideEffectDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medicai.pillpal.domain.SideEffect}.
 */
public interface SideEffectService {
    /**
     * Save a sideEffect.
     *
     * @param sideEffectDTO the entity to save.
     * @return the persisted entity.
     */
    SideEffectDTO save(SideEffectDTO sideEffectDTO);

    /**
     * Partially updates a sideEffect.
     *
     * @param sideEffectDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SideEffectDTO> partialUpdate(SideEffectDTO sideEffectDTO);

    /**
     * Get all the sideEffects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SideEffectDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sideEffect.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SideEffectDTO> findOne(Long id);

    /**
     * Delete the "id" sideEffect.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
