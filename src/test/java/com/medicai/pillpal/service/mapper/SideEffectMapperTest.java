package com.medicai.pillpal.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SideEffectMapperTest {

    private SideEffectMapper sideEffectMapper;

    @BeforeEach
    public void setUp() {
        sideEffectMapper = new SideEffectMapperImpl();
    }
}
