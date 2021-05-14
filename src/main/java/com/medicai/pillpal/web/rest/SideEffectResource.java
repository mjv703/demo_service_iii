package com.medicai.pillpal.web.rest;

import com.medicai.pillpal.repository.SideEffectRepository;
import com.medicai.pillpal.service.SideEffectService;
import com.medicai.pillpal.service.dto.SideEffectDTO;
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
 * REST controller for managing {@link com.medicai.pillpal.domain.SideEffect}.
 */
@RestController
@RequestMapping("/api")
public class SideEffectResource {

    private final Logger log = LoggerFactory.getLogger(SideEffectResource.class);

    private static final String ENTITY_NAME = "sideEffect";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SideEffectService sideEffectService;

    private final SideEffectRepository sideEffectRepository;

    public SideEffectResource(SideEffectService sideEffectService, SideEffectRepository sideEffectRepository) {
        this.sideEffectService = sideEffectService;
        this.sideEffectRepository = sideEffectRepository;
    }

    /**
     * {@code POST  /side-effects} : Create a new sideEffect.
     *
     * @param sideEffectDTO the sideEffectDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sideEffectDTO, or with status {@code 400 (Bad Request)} if the sideEffect has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/side-effects")
    public ResponseEntity<SideEffectDTO> createSideEffect(@Valid @RequestBody SideEffectDTO sideEffectDTO) throws URISyntaxException {
        log.debug("REST request to save SideEffect : {}", sideEffectDTO);
        if (sideEffectDTO.getId() != null) {
            throw new BadRequestAlertException("A new sideEffect cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SideEffectDTO result = sideEffectService.save(sideEffectDTO);
        return ResponseEntity
            .created(new URI("/api/side-effects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /side-effects/:id} : Updates an existing sideEffect.
     *
     * @param id the id of the sideEffectDTO to save.
     * @param sideEffectDTO the sideEffectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sideEffectDTO,
     * or with status {@code 400 (Bad Request)} if the sideEffectDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sideEffectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/side-effects/{id}")
    public ResponseEntity<SideEffectDTO> updateSideEffect(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SideEffectDTO sideEffectDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SideEffect : {}, {}", id, sideEffectDTO);
        if (sideEffectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sideEffectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sideEffectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SideEffectDTO result = sideEffectService.save(sideEffectDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sideEffectDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /side-effects/:id} : Partial updates given fields of an existing sideEffect, field will ignore if it is null
     *
     * @param id the id of the sideEffectDTO to save.
     * @param sideEffectDTO the sideEffectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sideEffectDTO,
     * or with status {@code 400 (Bad Request)} if the sideEffectDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sideEffectDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sideEffectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/side-effects/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SideEffectDTO> partialUpdateSideEffect(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SideEffectDTO sideEffectDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SideEffect partially : {}, {}", id, sideEffectDTO);
        if (sideEffectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sideEffectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sideEffectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SideEffectDTO> result = sideEffectService.partialUpdate(sideEffectDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sideEffectDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /side-effects/:id} : get the "id" sideEffect.
     *
     * @param id the id of the sideEffectDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sideEffectDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/side-effects/{id}")
    public ResponseEntity<SideEffectDTO> getSideEffect(@PathVariable Long id) {
        log.debug("REST request to get SideEffect : {}", id);
        Optional<SideEffectDTO> sideEffectDTO = sideEffectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sideEffectDTO);
    }

    /**
     * {@code DELETE  /side-effects/:id} : delete the "id" sideEffect.
     *
     * @param id the id of the sideEffectDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/side-effects/{id}")
    public ResponseEntity<Void> deleteSideEffect(@PathVariable Long id) {
        log.debug("REST request to delete SideEffect : {}", id);
        sideEffectService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
