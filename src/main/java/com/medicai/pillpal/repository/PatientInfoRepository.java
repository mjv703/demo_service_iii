package com.medicai.pillpal.repository;

import com.medicai.pillpal.domain.MobileDevice;
import com.medicai.pillpal.domain.PatientInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PatientInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientInfoRepository extends JpaRepository<PatientInfo, Long> {
    Page<MobileDevice> findAllByUserInfoId(Pageable pageable, Long id);
}
