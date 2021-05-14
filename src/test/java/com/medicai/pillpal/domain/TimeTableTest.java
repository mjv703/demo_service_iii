package com.medicai.pillpal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.medicai.pillpal.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TimeTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TimeTable.class);
        TimeTable timeTable1 = new TimeTable();
        timeTable1.setId(1L);
        TimeTable timeTable2 = new TimeTable();
        timeTable2.setId(timeTable1.getId());
        assertThat(timeTable1).isEqualTo(timeTable2);
        timeTable2.setId(2L);
        assertThat(timeTable1).isNotEqualTo(timeTable2);
        timeTable1.setId(null);
        assertThat(timeTable1).isNotEqualTo(timeTable2);
    }
}
