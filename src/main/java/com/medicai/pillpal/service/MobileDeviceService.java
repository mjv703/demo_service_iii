package com.medicai.pillpal.service;

import com.medicai.pillpal.service.dto.MobileDeviceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medicai.pillpal.domain.MobileDevice}.
 */
public interface MobileDeviceService {
    /**
     * Save a mobileDevice.
     *
     * @param mobileDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    MobileDeviceDTO save(MobileDeviceDTO mobileDeviceDTO);

    /**
     * Partially updates a mobileDevice.
     *
     * @param mobileDeviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MobileDeviceDTO> partialUpdate(MobileDeviceDTO mobileDeviceDTO);

    /**
     * Get all the mobileDevices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MobileDeviceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" mobileDevice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MobileDeviceDTO> findOne(Long id);

    /**
     * Delete the "id" mobileDevice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
