package com.medicai.pillpal.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedicineMapperTest {

    private MedicineMapper medicineMapper;

    @BeforeEach
    public void setUp() {
        medicineMapper = new MedicineMapperImpl();
    }
}
