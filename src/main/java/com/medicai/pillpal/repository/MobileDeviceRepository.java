package com.medicai.pillpal.repository;

import com.medicai.pillpal.domain.MobileDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MobileDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MobileDeviceRepository extends JpaRepository<MobileDevice, Long> {
    Page<MobileDevice> findAllByUserInfoId(Pageable pageable, Long id);
}
