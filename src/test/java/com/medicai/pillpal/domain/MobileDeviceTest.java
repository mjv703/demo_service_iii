package com.medicai.pillpal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.medicai.pillpal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MobileDeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MobileDevice.class);
        MobileDevice mobileDevice1 = new MobileDevice();
        mobileDevice1.setId(1L);
        MobileDevice mobileDevice2 = new MobileDevice();
        mobileDevice2.setId(mobileDevice1.getId());
        assertThat(mobileDevice1).isEqualTo(mobileDevice2);
        mobileDevice2.setId(2L);
        assertThat(mobileDevice1).isNotEqualTo(mobileDevice2);
        mobileDevice1.setId(null);
        assertThat(mobileDevice1).isNotEqualTo(mobileDevice2);
    }
}
