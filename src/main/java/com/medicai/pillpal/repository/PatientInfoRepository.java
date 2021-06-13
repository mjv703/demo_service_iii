package com.medicai.pillpal.repository;

import com.medicai.pillpal.domain.MobileDevice;
import com.medicai.pillpal.domain.PatientInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PatientInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientInfoRepository extends JpaRepository<PatientInfo, Long> {
    @Query(
        value = "select pi.* from Patient_Info pi where pi.User_Info_Id = :uid order by pi.Id \n-- #pageable\n",
        countQuery = "select count(*) from Patient_Info pi where pi.User_Info_Id = :uId",
        nativeQuery = true
    )
    Page<PatientInfo> findAllByUserInfoId(Pageable pageable, @Param("uid") Long id);
}
