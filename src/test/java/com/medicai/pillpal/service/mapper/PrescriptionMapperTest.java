package com.medicai.pillpal.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PrescriptionMapperTest {

    private PrescriptionMapper prescriptionMapper;

    @BeforeEach
    public void setUp() {
        prescriptionMapper = new PrescriptionMapperImpl();
    }
}
