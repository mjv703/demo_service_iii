package com.medicai.pillpal.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MobileDeviceMapperTest {

    private MobileDeviceMapper mobileDeviceMapper;

    @BeforeEach
    public void setUp() {
        mobileDeviceMapper = new MobileDeviceMapperImpl();
    }
}
