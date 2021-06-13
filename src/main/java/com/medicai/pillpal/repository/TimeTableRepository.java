package com.medicai.pillpal.repository;

import com.medicai.pillpal.domain.TimeTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TimeTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    @Query(
        value = "SELECT T.* FROM TIME_TABLE T " +
        "        INNER JOIN PRESCRIPTION P ON T.PRESCRIPTION_ID = P.ID " +
        "        INNER JOIN PATIENT_INFO PI ON P.PATIENT_INFO_ID = PI.ID " +
        "        WHERE PI.ID = :patientId " +
        "        ORDER BY T.ID   ",
        countQuery = "SELECT T.* FROM TIME_TABLE T " +
        "        INNER JOIN PRESCRIPTION P ON T.PRESCRIPTION_ID = P.ID " +
        "        INNER JOIN PATIENT_INFO PI ON P.PATIENT_INFO_ID = PI.ID " +
        "        WHERE PI.ID = :patientId",
        nativeQuery = true
    )
    Page<TimeTable> findByPatientInfo(Pageable pageable, @Param("patientId") Long patientId);
}
