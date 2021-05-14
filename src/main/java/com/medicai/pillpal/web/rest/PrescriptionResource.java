package com.medicai.pillpal.web.rest;

import com.medicai.pillpal.repository.PrescriptionRepository;
import com.medicai.pillpal.service.PrescriptionQueryService;
import com.medicai.pillpal.service.PrescriptionService;
import com.medicai.pillpal.service.criteria.PrescriptionCriteria;
import com.medicai.pillpal.service.dto.PrescriptionDTO;
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
 * REST controller for managing {@link com.medicai.pillpal.domain.Prescription}.
 */
@RestController
@RequestMapping("/api")
public class PrescriptionResource {

    private final Logger log = LoggerFactory.getLogger(PrescriptionResource.class);

    private static final String ENTITY_NAME = "prescription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrescriptionService prescriptionService;

    private final PrescriptionRepository prescriptionRepository;

    private final PrescriptionQueryService prescriptionQueryService;

    public PrescriptionResource(
        PrescriptionService prescriptionService,
        PrescriptionRepository prescriptionRepository,
        PrescriptionQueryService prescriptionQueryService
    ) {
        this.prescriptionService = prescriptionService;
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionQueryService = prescriptionQueryService;
    }

    /**
     * {@code POST  /prescriptions} : Create a new prescription.
     *
     * @param prescriptionDTO the prescriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prescriptionDTO, or with status {@code 400 (Bad Request)} if the prescription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prescriptions")
    public ResponseEntity<PrescriptionDTO> createPrescription(@Valid @RequestBody PrescriptionDTO prescriptionDTO)
        throws URISyntaxException {
        log.debug("REST request to save Prescription : {}", prescriptionDTO);
        if (prescriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new prescription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrescriptionDTO result = prescriptionService.save(prescriptionDTO);
        return ResponseEntity
            .created(new URI("/api/prescriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prescriptions/:id} : Updates an existing prescription.
     *
     * @param id the id of the prescriptionDTO to save.
     * @param prescriptionDTO the prescriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prescriptionDTO,
     * or with status {@code 400 (Bad Request)} if the prescriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prescriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prescriptions/{id}")
    public ResponseEntity<PrescriptionDTO> updatePrescription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrescriptionDTO prescriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Prescription : {}, {}", id, prescriptionDTO);
        if (prescriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prescriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prescriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrescriptionDTO result = prescriptionService.save(prescriptionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prescriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prescriptions/:id} : Partial updates given fields of an existing prescription, field will ignore if it is null
     *
     * @param id the id of the prescriptionDTO to save.
     * @param prescriptionDTO the prescriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prescriptionDTO,
     * or with status {@code 400 (Bad Request)} if the prescriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prescriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prescriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prescriptions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PrescriptionDTO> partialUpdatePrescription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrescriptionDTO prescriptionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Prescription partially : {}, {}", id, prescriptionDTO);
        if (prescriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prescriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prescriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrescriptionDTO> result = prescriptionService.partialUpdate(prescriptionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prescriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prescriptions/count} : count all the prescriptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prescriptions/count")
    public ResponseEntity<Long> countPrescriptions(PrescriptionCriteria criteria) {
        log.debug("REST request to count Prescriptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(prescriptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prescriptions/:id} : get the "id" prescription.
     *
     * @param id the id of the prescriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prescriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prescriptions/{id}")
    public ResponseEntity<PrescriptionDTO> getPrescription(@PathVariable Long id) {
        log.debug("REST request to get Prescription : {}", id);
        Optional<PrescriptionDTO> prescriptionDTO = prescriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prescriptionDTO);
    }

    /**
     * {@code DELETE  /prescriptions/:id} : delete the "id" prescription.
     *
     * @param id the id of the prescriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prescriptions/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        log.debug("REST request to delete Prescription : {}", id);
        prescriptionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
