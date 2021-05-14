package com.medicai.pillpal.repository;

import com.medicai.pillpal.domain.Medicine;
import com.medicai.pillpal.domain.User;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Medicine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long>, JpaSpecificationExecutor<Medicine> {}
