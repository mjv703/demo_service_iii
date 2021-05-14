package com.medicai.pillpal.service;

import com.medicai.pillpal.service.dto.TimeTableDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.medicai.pillpal.domain.TimeTable}.
 */
public interface TimeTableService {
    /**
     * Save a timeTable.
     *
     * @param timeTableDTO the entity to save.
     * @return the persisted entity.
     */
    TimeTableDTO save(TimeTableDTO timeTableDTO);

    /**
     * Partially updates a timeTable.
     *
     * @param timeTableDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TimeTableDTO> partialUpdate(TimeTableDTO timeTableDTO);

    /**
     * Get all the timeTables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TimeTableDTO> findAll(Pageable pageable, Long patientId);

    /**
     * Get the "id" timeTable.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TimeTableDTO> findOne(Long id);

    /**
     * Delete the "id" timeTable.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
