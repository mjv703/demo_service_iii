package com.medicai.pillpal.repository;

import com.medicai.pillpal.domain.MobileDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MobileDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MobileDeviceRepository extends JpaRepository<MobileDevice, Long> {
    @Query(
        value = "select md.* from Mobile_Device md where md.User_Info_Id = :uid order by md.Id \n-- #pageable\n",
        countQuery = "select count(*) from Mobile_Device md where md.User_Info_Id = :uId",
        nativeQuery = true
    )
    Page<MobileDevice> findAllByUserInfoId(Pageable pageable, @Param("uid") Long uid);
}
