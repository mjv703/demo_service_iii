package com.medicai.pillpal.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PharmacyMapperTest {

    private PharmacyMapper pharmacyMapper;

    @BeforeEach
    public void setUp() {
        pharmacyMapper = new PharmacyMapperImpl();
    }
}
