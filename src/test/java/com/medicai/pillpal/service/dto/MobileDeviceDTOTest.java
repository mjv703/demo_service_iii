package com.medicai.pillpal.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.medicai.pillpal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MobileDeviceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MobileDeviceDTO.class);
        MobileDeviceDTO mobileDeviceDTO1 = new MobileDeviceDTO();
        mobileDeviceDTO1.setId(1L);
        MobileDeviceDTO mobileDeviceDTO2 = new MobileDeviceDTO();
        assertThat(mobileDeviceDTO1).isNotEqualTo(mobileDeviceDTO2);
        mobileDeviceDTO2.setId(mobileDeviceDTO1.getId());
        assertThat(mobileDeviceDTO1).isEqualTo(mobileDeviceDTO2);
        mobileDeviceDTO2.setId(2L);
        assertThat(mobileDeviceDTO1).isNotEqualTo(mobileDeviceDTO2);
        mobileDeviceDTO1.setId(null);
        assertThat(mobileDeviceDTO1).isNotEqualTo(mobileDeviceDTO2);
    }
}
