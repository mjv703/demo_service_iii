package com.medicai.pillpal.service;

import com.medicai.pillpal.domain.*; // for static metamodels
import com.medicai.pillpal.domain.Medicine;
import com.medicai.pillpal.repository.MedicineRepository;
import com.medicai.pillpal.service.criteria.MedicineCriteria;
import com.medicai.pillpal.service.dto.MedicineDTO;
import com.medicai.pillpal.service.mapper.MedicineMapper;
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
 * Service for executing complex queries for {@link Medicine} entities in the database.
 * The main input is a {@link MedicineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MedicineDTO} or a {@link Page} of {@link MedicineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicineQueryService extends QueryService<Medicine> {

    private final Logger log = LoggerFactory.getLogger(MedicineQueryService.class);

    private final MedicineRepository medicineRepository;

    private final MedicineMapper medicineMapper;

    public MedicineQueryService(MedicineRepository medicineRepository, MedicineMapper medicineMapper) {
        this.medicineRepository = medicineRepository;
        this.medicineMapper = medicineMapper;
    }

    /**
     * Return a {@link List} of {@link MedicineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MedicineDTO> findByCriteria(MedicineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Medicine> specification = createSpecification(criteria);
        return medicineMapper.toDto(medicineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MedicineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedicineDTO> findByCriteria(MedicineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medicine> specification = createSpecification(criteria);
        return medicineRepository.findAll(specification, page).map(medicineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicineCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Medicine> specification = createSpecification(criteria);
        return medicineRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medicine> createSpecification(MedicineCriteria criteria) {
        Specification<Medicine> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Medicine_.id));
            }
            if (criteria.getBrandName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrandName(), Medicine_.brandName));
            }
            if (criteria.getGenericName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGenericName(), Medicine_.genericName));
            }
            if (criteria.getSubstanceName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubstanceName(), Medicine_.substanceName));
            }
            if (criteria.getManufacturerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getManufacturerName(), Medicine_.manufacturerName));
            }
            if (criteria.getProductNDC() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductNDC(), Medicine_.productNDC));
            }
            if (criteria.getPackageNDC() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPackageNDC(), Medicine_.packageNDC));
            }
            if (criteria.getRx() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRx(), Medicine_.rx));
            }
            if (criteria.getMedicRout() != null) {
                specification = specification.and(buildSpecification(criteria.getMedicRout(), Medicine_.medicRout));
            }
            if (criteria.getMedicImageUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMedicImageUrl(), Medicine_.medicImageUrl));
            }
            if (criteria.getSideEffectId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSideEffectId(),
                            root -> root.join(Medicine_.sideEffects, JoinType.LEFT).get(SideEffect_.id)
                        )
                    );
            }
            if (criteria.getPrescriptionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrescriptionId(),
                            root -> root.join(Medicine_.prescriptions, JoinType.LEFT).get(Prescription_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
