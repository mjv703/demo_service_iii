package com.medicai.pillpal.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class MedicineMapperTest {

    private MedicineMapper medicineMapper;

    @BeforeEach
    public void setUp() {
        medicineMapper = new MedicineMapperImpl();
    }
}
