package com.medicai.pillpal.web.rest;

import com.medicai.pillpal.repository.MedicineRepository;
import com.medicai.pillpal.service.MedicineQueryService;
import com.medicai.pillpal.service.MedicineService;
import com.medicai.pillpal.service.criteria.MedicineCriteria;
import com.medicai.pillpal.service.dto.MedicineDTO;
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
 * REST controller for managing {@link com.medicai.pillpal.domain.Medicine}.
 */
@RestController
@RequestMapping("/api")
public class MedicineResource {

    private final Logger log = LoggerFactory.getLogger(MedicineResource.class);

    private static final String ENTITY_NAME = "medicine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicineService medicineService;

    private final MedicineRepository medicineRepository;

    private final MedicineQueryService medicineQueryService;

    public MedicineResource(
        MedicineService medicineService,
        MedicineRepository medicineRepository,
        MedicineQueryService medicineQueryService
    ) {
        this.medicineService = medicineService;
        this.medicineRepository = medicineRepository;
        this.medicineQueryService = medicineQueryService;
    }

    /**
     * {@code POST  /medicines} : Create a new medicine.
     *
     * @param medicineDTO the medicineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medicineDTO, or with status {@code 400 (Bad Request)} if the medicine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medicines")
    public ResponseEntity<MedicineDTO> createMedicine(@Valid @RequestBody MedicineDTO medicineDTO) throws URISyntaxException {
        log.debug("REST request to save Medicine : {}", medicineDTO);
        if (medicineDTO.getId() != null) {
            throw new BadRequestAlertException("A new medicine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MedicineDTO result = medicineService.save(medicineDTO);
        return ResponseEntity
            .created(new URI("/api/medicines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medicines/:id} : Updates an existing medicine.
     *
     * @param id the id of the medicineDTO to save.
     * @param medicineDTO the medicineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicineDTO,
     * or with status {@code 400 (Bad Request)} if the medicineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medicineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medicines/{id}")
    public ResponseEntity<MedicineDTO> updateMedicine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MedicineDTO medicineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Medicine : {}, {}", id, medicineDTO);
        if (medicineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MedicineDTO result = medicineService.save(medicineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /medicines/:id} : Partial updates given fields of an existing medicine, field will ignore if it is null
     *
     * @param id the id of the medicineDTO to save.
     * @param medicineDTO the medicineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medicineDTO,
     * or with status {@code 400 (Bad Request)} if the medicineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the medicineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the medicineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/medicines/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MedicineDTO> partialUpdateMedicine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MedicineDTO medicineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Medicine partially : {}, {}", id, medicineDTO);
        if (medicineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MedicineDTO> result = medicineService.partialUpdate(medicineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, medicineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /medicines/count} : count all the medicines.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/medicines/count")
    public ResponseEntity<Long> countMedicines(MedicineCriteria criteria) {
        log.debug("REST request to count Medicines by criteria: {}", criteria);
        return ResponseEntity.ok().body(medicineQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medicines/:id} : get the "id" medicine.
     *
     * @param id the id of the medicineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medicineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medicines/{id}")
    public ResponseEntity<MedicineDTO> getMedicine(@PathVariable Long id) {
        log.debug("REST request to get Medicine : {}", id);
        Optional<MedicineDTO> medicineDTO = medicineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicineDTO);
    }

    /**
     * {@code DELETE  /medicines/:id} : delete the "id" medicine.
     *
     * @param id the id of the medicineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medicines/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        log.debug("REST request to delete Medicine : {}", id);
        medicineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
