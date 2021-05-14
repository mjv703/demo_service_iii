package com.medicai.pillpal.web.rest;

import com.medicai.pillpal.repository.TimeTableRepository;
import com.medicai.pillpal.service.TimeTableService;
import com.medicai.pillpal.service.dto.TimeTableDTO;
import com.medicai.pillpal.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.medicai.pillpal.domain.TimeTable}.
 */
@RestController
@RequestMapping("/api")
public class TimeTableResource {

    private final Logger log = LoggerFactory.getLogger(TimeTableResource.class);

    private static final String ENTITY_NAME = "timeTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TimeTableService timeTableService;

    private final TimeTableRepository timeTableRepository;

    public TimeTableResource(TimeTableService timeTableService, TimeTableRepository timeTableRepository) {
        this.timeTableService = timeTableService;
        this.timeTableRepository = timeTableRepository;
    }

    /**
     * {@code POST  /time-tables} : Create a new timeTable.
     *
     * @param timeTableDTO the timeTableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new timeTableDTO, or with status {@code 400 (Bad Request)} if the timeTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/time-tables")
    public ResponseEntity<TimeTableDTO> createTimeTable(@Valid @RequestBody TimeTableDTO timeTableDTO) throws URISyntaxException {
        log.debug("REST request to save TimeTable : {}", timeTableDTO);
        if (timeTableDTO.getId() != null) {
            throw new BadRequestAlertException("A new timeTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TimeTableDTO result = timeTableService.save(timeTableDTO);
        return ResponseEntity
            .created(new URI("/api/time-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /time-tables/:id} : Updates an existing timeTable.
     *
     * @param id           the id of the timeTableDTO to save.
     * @param timeTableDTO the timeTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeTableDTO,
     * or with status {@code 400 (Bad Request)} if the timeTableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the timeTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/time-tables/{id}")
    public ResponseEntity<TimeTableDTO> updateTimeTable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TimeTableDTO timeTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TimeTable : {}, {}", id, timeTableDTO);
        if (timeTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TimeTableDTO result = timeTableService.save(timeTableDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, timeTableDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /time-tables/:id} : Partial updates given fields of an existing timeTable, field will ignore if it is null
     *
     * @param id           the id of the timeTableDTO to save.
     * @param timeTableDTO the timeTableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated timeTableDTO,
     * or with status {@code 400 (Bad Request)} if the timeTableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the timeTableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the timeTableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/time-tables/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TimeTableDTO> partialUpdateTimeTable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TimeTableDTO timeTableDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TimeTable partially : {}, {}", id, timeTableDTO);
        if (timeTableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, timeTableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!timeTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TimeTableDTO> result = timeTableService.partialUpdate(timeTableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, timeTableDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /time-tables} : get all the timeTables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of timeTables in body.
     */
    @GetMapping("/time-tables")
    public ResponseEntity<List<TimeTableDTO>> getAllTimeTables(Pageable pageable) {
        log.debug("REST request to get a page of TimeTables");
        Page<TimeTableDTO> page = timeTableService.findAll(pageable, Long.valueOf(1));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /time-tables/:id} : get the "id" timeTable.
     *
     * @param id the id of the timeTableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the timeTableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/time-tables/{id}")
    public ResponseEntity<TimeTableDTO> getTimeTable(@PathVariable Long id) {
        log.debug("REST request to get TimeTable : {}", id);
        Optional<TimeTableDTO> timeTableDTO = timeTableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(timeTableDTO);
    }

    /**
     * {@code DELETE  /time-tables/:id} : delete the "id" timeTable.
     *
     * @param id the id of the timeTableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/time-tables/{id}")
    public ResponseEntity<Void> deleteTimeTable(@PathVariable Long id) {
        log.debug("REST request to delete TimeTable : {}", id);
        timeTableService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
