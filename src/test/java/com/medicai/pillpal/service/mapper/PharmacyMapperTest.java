package com.medicai.pillpal.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class PharmacyMapperTest {

    private PharmacyMapper pharmacyMapper;

    @BeforeEach
    public void setUp() {
        pharmacyMapper = new PharmacyMapperImpl();
    }
}
