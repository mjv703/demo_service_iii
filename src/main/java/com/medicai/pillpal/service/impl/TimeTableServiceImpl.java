package com.medicai.pillpal.service.impl;

import com.medicai.pillpal.domain.TimeTable;
import com.medicai.pillpal.repository.TimeTableRepository;
import com.medicai.pillpal.service.TimeTableService;
import com.medicai.pillpal.service.dto.TimeTableDTO;
import com.medicai.pillpal.service.mapper.TimeTableMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TimeTable}.
 */
@Service
@Transactional
public class TimeTableServiceImpl implements TimeTableService {

    private final Logger log = LoggerFactory.getLogger(TimeTableServiceImpl.class);

    private final TimeTableRepository timeTableRepository;

    private final TimeTableMapper timeTableMapper;

    public TimeTableServiceImpl(TimeTableRepository timeTableRepository, TimeTableMapper timeTableMapper) {
        this.timeTableRepository = timeTableRepository;
        this.timeTableMapper = timeTableMapper;
    }

    @Override
    public TimeTableDTO save(TimeTableDTO timeTableDTO) {
        log.debug("Request to save TimeTable : {}", timeTableDTO);
        TimeTable timeTable = timeTableMapper.toEntity(timeTableDTO);
        timeTable = timeTableRepository.save(timeTable);
        return timeTableMapper.toDto(timeTable);
    }

    @Override
    public Optional<TimeTableDTO> partialUpdate(TimeTableDTO timeTableDTO) {
        log.debug("Request to partially update TimeTable : {}", timeTableDTO);

        return timeTableRepository
            .findById(timeTableDTO.getId())
            .map(
                existingTimeTable -> {
                    timeTableMapper.partialUpdate(existingTimeTable, timeTableDTO);
                    return existingTimeTable;
                }
            )
            .map(timeTableRepository::save)
            .map(timeTableMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TimeTableDTO> findAll(Pageable pageable, Long patientId) {
        log.debug("Request to get all TimeTables");
        //return timeTableRepository.findByPatientInfo(pageable , patientId).map(timeTableMapper::toDto);
        return timeTableRepository.findAll(pageable).map(timeTableMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TimeTableDTO> findOne(Long id) {
        log.debug("Request to get TimeTable : {}", id);
        return timeTableRepository.findById(id).map(timeTableMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TimeTable : {}", id);
        timeTableRepository.deleteById(id);
    }
}
