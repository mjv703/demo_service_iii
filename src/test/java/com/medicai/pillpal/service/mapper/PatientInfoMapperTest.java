package com.medicai.pillpal.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PatientInfoMapperTest {

    private PatientInfoMapper patientInfoMapper;

    @BeforeEach
    public void setUp() {
        patientInfoMapper = new PatientInfoMapperImpl();
    }
}
