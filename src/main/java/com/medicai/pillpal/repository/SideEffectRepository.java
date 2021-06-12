package com.medicai.pillpal.repository;

import com.medicai.pillpal.domain.SideEffect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SideEffect entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SideEffectRepository extends JpaRepository<SideEffect, Long> {}
