package com.medicai.pillpal.repository;

import com.medicai.pillpal.domain.TimeTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TimeTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    //    @Query("SELECT T FROM TIME_TABLE T " +
    //        "INNER JOIN PRESCRIPTION P ON T.PRESCRIPTION_ID = P.ID " +
    //        "INNER JOIN PATIENT_INFO PI ON P.PATIENT_INFO_ID = PI.ID " +
    //        "WHERE PI.ID = :patientId")
    //    Page<TimeTable> findByPatientInfo(Pageable pageable, @Param("patientId")  Long patientId);
}
