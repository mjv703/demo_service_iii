package com.medicai.pillpal.repository;

import com.medicai.pillpal.domain.MobileDevice;
import com.medicai.pillpal.domain.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Prescription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long>, JpaSpecificationExecutor<Prescription> {
    @Query(
        value = "select p.* from Prescription p where p.Patient_Info_Id = :pid order by p.Id \n-- #pageable\n",
        countQuery = "select count(*) from Prescription p where p.Patient_Info_Id = :pid",
        nativeQuery = true
    )
    Page<Prescription> findAllByPatientInfoId(Pageable pageable, @Param("pid") Long pid);
}
