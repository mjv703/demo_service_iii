package com.medicai.pillpal.repository;

import com.medicai.pillpal.domain.Prescription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Prescription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long>, JpaSpecificationExecutor<Prescription> {}
