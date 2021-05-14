package com.medicai.pillpal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.medicai.pillpal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SideEffectDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SideEffectDTO.class);
        SideEffectDTO sideEffectDTO1 = new SideEffectDTO();
        sideEffectDTO1.setId(1L);
        SideEffectDTO sideEffectDTO2 = new SideEffectDTO();
        assertThat(sideEffectDTO1).isNotEqualTo(sideEffectDTO2);
        sideEffectDTO2.setId(sideEffectDTO1.getId());
        assertThat(sideEffectDTO1).isEqualTo(sideEffectDTO2);
        sideEffectDTO2.setId(2L);
        assertThat(sideEffectDTO1).isNotEqualTo(sideEffectDTO2);
        sideEffectDTO1.setId(null);
        assertThat(sideEffectDTO1).isNotEqualTo(sideEffectDTO2);
    }
}
