package com.medicai.pillpal.web.rest;

import com.medicai.pillpal.repository.MobileDeviceRepository;
import com.medicai.pillpal.service.MobileDeviceService;
import com.medicai.pillpal.service.dto.MobileDeviceDTO;
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
 * REST controller for managing {@link com.medicai.pillpal.domain.MobileDevice}.
 */
@RestController
@RequestMapping("/api")
public class MobileDeviceResource {

    private final Logger log = LoggerFactory.getLogger(MobileDeviceResource.class);

    private static final String ENTITY_NAME = "mobileDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MobileDeviceService mobileDeviceService;

    private final MobileDeviceRepository mobileDeviceRepository;

    public MobileDeviceResource(MobileDeviceService mobileDeviceService, MobileDeviceRepository mobileDeviceRepository) {
        this.mobileDeviceService = mobileDeviceService;
        this.mobileDeviceRepository = mobileDeviceRepository;
    }

    /**
     * {@code POST  /mobile-devices} : Create a new mobileDevice.
     *
     * @param mobileDeviceDTO the mobileDeviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mobileDeviceDTO, or with status {@code 400 (Bad Request)} if the mobileDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mobile-devices")
    public ResponseEntity<MobileDeviceDTO> createMobileDevice(@Valid @RequestBody MobileDeviceDTO mobileDeviceDTO)
        throws URISyntaxException {
        log.debug("REST request to save MobileDevice : {}", mobileDeviceDTO);
        if (mobileDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new mobileDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MobileDeviceDTO result = mobileDeviceService.save(mobileDeviceDTO);
        return ResponseEntity
            .created(new URI("/api/mobile-devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mobile-devices/:id} : Updates an existing mobileDevice.
     *
     * @param id the id of the mobileDeviceDTO to save.
     * @param mobileDeviceDTO the mobileDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mobileDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the mobileDeviceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mobileDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mobile-devices/{id}")
    public ResponseEntity<MobileDeviceDTO> updateMobileDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MobileDeviceDTO mobileDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MobileDevice : {}, {}", id, mobileDeviceDTO);
        if (mobileDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mobileDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mobileDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MobileDeviceDTO result = mobileDeviceService.save(mobileDeviceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mobileDeviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mobile-devices/:id} : Partial updates given fields of an existing mobileDevice, field will ignore if it is null
     *
     * @param id the id of the mobileDeviceDTO to save.
     * @param mobileDeviceDTO the mobileDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mobileDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the mobileDeviceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mobileDeviceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mobileDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mobile-devices/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MobileDeviceDTO> partialUpdateMobileDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MobileDeviceDTO mobileDeviceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MobileDevice partially : {}, {}", id, mobileDeviceDTO);
        if (mobileDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mobileDeviceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mobileDeviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MobileDeviceDTO> result = mobileDeviceService.partialUpdate(mobileDeviceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mobileDeviceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mobile-devices/:id} : get the "id" mobileDevice.
     *
     * @param id the id of the mobileDeviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mobileDeviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mobile-devices/{id}")
    public ResponseEntity<MobileDeviceDTO> getMobileDevice(@PathVariable Long id) {
        log.debug("REST request to get MobileDevice : {}", id);
        Optional<MobileDeviceDTO> mobileDeviceDTO = mobileDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mobileDeviceDTO);
    }

    /**
     * {@code DELETE  /mobile-devices/:id} : delete the "id" mobileDevice.
     *
     * @param id the id of the mobileDeviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mobile-devices/{id}")
    public ResponseEntity<Void> deleteMobileDevice(@PathVariable Long id) {
        log.debug("REST request to delete MobileDevice : {}", id);
        mobileDeviceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/mobile-devices-filter-by-account-id/{id}")
    public ResponseEntity<List<MobileDeviceDTO>> getMobileDevicesFilteredByAccountId(Pageable pageable, @PathVariable Long id) {
        log.debug("REST request to get MobileDevice : {}", id);
        Page<MobileDeviceDTO> page = mobileDeviceService.findAllByAccountId(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
