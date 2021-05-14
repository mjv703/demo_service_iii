package com.medicai.pillpal.service;

import com.medicai.pillpal.domain.*; // for static metamodels
import com.medicai.pillpal.domain.Prescription;
import com.medicai.pillpal.repository.PrescriptionRepository;
import com.medicai.pillpal.service.criteria.PrescriptionCriteria;
import com.medicai.pillpal.service.dto.PrescriptionDTO;
import com.medicai.pillpal.service.mapper.PrescriptionMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Prescription} entities in the database.
 * The main input is a {@link PrescriptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrescriptionDTO} or a {@link Page} of {@link PrescriptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrescriptionQueryService extends QueryService<Prescription> {

    private final Logger log = LoggerFactory.getLogger(PrescriptionQueryService.class);

    private final PrescriptionRepository prescriptionRepository;

    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionQueryService(PrescriptionRepository prescriptionRepository, PrescriptionMapper prescriptionMapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.prescriptionMapper = prescriptionMapper;
    }

    /**
     * Return a {@link List} of {@link PrescriptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrescriptionDTO> findByCriteria(PrescriptionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Prescription> specification = createSpecification(criteria);
        return prescriptionMapper.toDto(prescriptionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrescriptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrescriptionDTO> findByCriteria(PrescriptionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Prescription> specification = createSpecification(criteria);
        return prescriptionRepository.findAll(specification, page).map(prescriptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrescriptionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Prescription> specification = createSpecification(criteria);
        return prescriptionRepository.count(specification);
    }

    /**
     * Function to convert {@link PrescriptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Prescription> createSpecification(PrescriptionCriteria criteria) {
        Specification<Prescription> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Prescription_.id));
            }
            if (criteria.getPrescriptionCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrescriptionCode(), Prescription_.prescriptionCode));
            }
            if (criteria.getBarCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBarCode(), Prescription_.barCode));
            }
            if (criteria.getIssueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIssueDate(), Prescription_.issueDate));
            }
            if (criteria.getUsageDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsageDescription(), Prescription_.usageDescription));
            }
            if (criteria.getImportantInfo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImportantInfo(), Prescription_.importantInfo));
            }
            if (criteria.getQty() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQty(), Prescription_.qty));
            }
            if (criteria.getHasRefill() != null) {
                specification = specification.and(buildSpecification(criteria.getHasRefill(), Prescription_.hasRefill));
            }
            if (criteria.getRefillTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRefillTime(), Prescription_.refillTime));
            }
            if (criteria.getPrescriptionImageUrl() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPrescriptionImageUrl(), Prescription_.prescriptionImageUrl));
            }
            if (criteria.getMedicType() != null) {
                specification = specification.and(buildSpecification(criteria.getMedicType(), Prescription_.medicType));
            }
            if (criteria.getMedicColor() != null) {
                specification = specification.and(buildSpecification(criteria.getMedicColor(), Prescription_.medicColor));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Prescription_.status));
            }
            if (criteria.getDeviceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDeviceId(), root -> root.join(Prescription_.devices, JoinType.LEFT).get(Device_.id))
                    );
            }
            if (criteria.getTimeTableId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTimeTableId(),
                            root -> root.join(Prescription_.timeTables, JoinType.LEFT).get(TimeTable_.id)
                        )
                    );
            }
            if (criteria.getPatientInfoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPatientInfoId(),
                            root -> root.join(Prescription_.patientInfo, JoinType.LEFT).get(PatientInfo_.id)
                        )
                    );
            }
            if (criteria.getMedicineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMedicineId(),
                            root -> root.join(Prescription_.medicine, JoinType.LEFT).get(Medicine_.id)
                        )
                    );
            }
            if (criteria.getPharmacyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPharmacyId(),
                            root -> root.join(Prescription_.pharmacy, JoinType.LEFT).get(Pharmacy_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
