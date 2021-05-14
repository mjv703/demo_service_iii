package com.medicai.pillpal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medicai.pillpal.IntegrationTest;
import com.medicai.pillpal.domain.TimeTable;
import com.medicai.pillpal.domain.enumeration.TakenStatus;
import com.medicai.pillpal.repository.TimeTableRepository;
import com.medicai.pillpal.service.dto.TimeTableDTO;
import com.medicai.pillpal.service.mapper.TimeTableMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TimeTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TimeTableResourceIT {

    private static final Instant DEFAULT_START_DATETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TakenStatus DEFAULT_IS_TAKEN = TakenStatus.TAKEN;
    private static final TakenStatus UPDATED_IS_TAKEN = TakenStatus.NOT_TAKEN;

    private static final String ENTITY_API_URL = "/api/time-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private TimeTableMapper timeTableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTimeTableMockMvc;

    private TimeTable timeTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeTable createEntity(EntityManager em) {
        TimeTable timeTable = new TimeTable()
            .startDatetime(DEFAULT_START_DATETIME)
            .endDateTime(DEFAULT_END_DATE_TIME)
            .isTaken(DEFAULT_IS_TAKEN);
        return timeTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TimeTable createUpdatedEntity(EntityManager em) {
        TimeTable timeTable = new TimeTable()
            .startDatetime(UPDATED_START_DATETIME)
            .endDateTime(UPDATED_END_DATE_TIME)
            .isTaken(UPDATED_IS_TAKEN);
        return timeTable;
    }

    @BeforeEach
    public void initTest() {
        timeTable = createEntity(em);
    }

    @Test
    @Transactional
    void createTimeTable() throws Exception {
        int databaseSizeBeforeCreate = timeTableRepository.findAll().size();
        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);
        restTimeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeTableDTO)))
            .andExpect(status().isCreated());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeCreate + 1);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getStartDatetime()).isEqualTo(DEFAULT_START_DATETIME);
        assertThat(testTimeTable.getEndDateTime()).isEqualTo(DEFAULT_END_DATE_TIME);
        assertThat(testTimeTable.getIsTaken()).isEqualTo(DEFAULT_IS_TAKEN);
    }

    @Test
    @Transactional
    void createTimeTableWithExistingId() throws Exception {
        // Create the TimeTable with an existing ID
        timeTable.setId(1L);
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        int databaseSizeBeforeCreate = timeTableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeTableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStartDatetimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeTableRepository.findAll().size();
        // set the field null
        timeTable.setStartDatetime(null);

        // Create the TimeTable, which fails.
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        restTimeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeTableDTO)))
            .andExpect(status().isBadRequest());

        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeTableRepository.findAll().size();
        // set the field null
        timeTable.setEndDateTime(null);

        // Create the TimeTable, which fails.
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        restTimeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeTableDTO)))
            .andExpect(status().isBadRequest());

        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsTakenIsRequired() throws Exception {
        int databaseSizeBeforeTest = timeTableRepository.findAll().size();
        // set the field null
        timeTable.setIsTaken(null);

        // Create the TimeTable, which fails.
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        restTimeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeTableDTO)))
            .andExpect(status().isBadRequest());

        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTimeTables() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);

        // Get all the timeTableList
        restTimeTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timeTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDatetime").value(hasItem(DEFAULT_START_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].endDateTime").value(hasItem(DEFAULT_END_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].isTaken").value(hasItem(DEFAULT_IS_TAKEN.toString())));
    }

    @Test
    @Transactional
    void getTimeTable() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);

        // Get the timeTable
        restTimeTableMockMvc
            .perform(get(ENTITY_API_URL_ID, timeTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(timeTable.getId().intValue()))
            .andExpect(jsonPath("$.startDatetime").value(DEFAULT_START_DATETIME.toString()))
            .andExpect(jsonPath("$.endDateTime").value(DEFAULT_END_DATE_TIME.toString()))
            .andExpect(jsonPath("$.isTaken").value(DEFAULT_IS_TAKEN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTimeTable() throws Exception {
        // Get the timeTable
        restTimeTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTimeTable() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);

        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();

        // Update the timeTable
        TimeTable updatedTimeTable = timeTableRepository.findById(timeTable.getId()).get();
        // Disconnect from session so that the updates on updatedTimeTable are not directly saved in db
        em.detach(updatedTimeTable);
        updatedTimeTable.startDatetime(UPDATED_START_DATETIME).endDateTime(UPDATED_END_DATE_TIME).isTaken(UPDATED_IS_TAKEN);
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(updatedTimeTable);

        restTimeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeTableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            )
            .andExpect(status().isOk());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getStartDatetime()).isEqualTo(UPDATED_START_DATETIME);
        assertThat(testTimeTable.getEndDateTime()).isEqualTo(UPDATED_END_DATE_TIME);
        assertThat(testTimeTable.getIsTaken()).isEqualTo(UPDATED_IS_TAKEN);
    }

    @Test
    @Transactional
    void putNonExistingTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, timeTableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(timeTableDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTimeTableWithPatch() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);

        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();

        // Update the timeTable using partial update
        TimeTable partialUpdatedTimeTable = new TimeTable();
        partialUpdatedTimeTable.setId(timeTable.getId());

        partialUpdatedTimeTable.isTaken(UPDATED_IS_TAKEN);

        restTimeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeTable))
            )
            .andExpect(status().isOk());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getStartDatetime()).isEqualTo(DEFAULT_START_DATETIME);
        assertThat(testTimeTable.getEndDateTime()).isEqualTo(DEFAULT_END_DATE_TIME);
        assertThat(testTimeTable.getIsTaken()).isEqualTo(UPDATED_IS_TAKEN);
    }

    @Test
    @Transactional
    void fullUpdateTimeTableWithPatch() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);

        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();

        // Update the timeTable using partial update
        TimeTable partialUpdatedTimeTable = new TimeTable();
        partialUpdatedTimeTable.setId(timeTable.getId());

        partialUpdatedTimeTable.startDatetime(UPDATED_START_DATETIME).endDateTime(UPDATED_END_DATE_TIME).isTaken(UPDATED_IS_TAKEN);

        restTimeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTimeTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTimeTable))
            )
            .andExpect(status().isOk());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
        TimeTable testTimeTable = timeTableList.get(timeTableList.size() - 1);
        assertThat(testTimeTable.getStartDatetime()).isEqualTo(UPDATED_START_DATETIME);
        assertThat(testTimeTable.getEndDateTime()).isEqualTo(UPDATED_END_DATE_TIME);
        assertThat(testTimeTable.getIsTaken()).isEqualTo(UPDATED_IS_TAKEN);
    }

    @Test
    @Transactional
    void patchNonExistingTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTimeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, timeTableDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = timeTableRepository.findAll().size();
        timeTable.setId(count.incrementAndGet());

        // Create the TimeTable
        TimeTableDTO timeTableDTO = timeTableMapper.toDto(timeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTimeTableMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(timeTableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TimeTable in the database
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTimeTable() throws Exception {
        // Initialize the database
        timeTableRepository.saveAndFlush(timeTable);

        int databaseSizeBeforeDelete = timeTableRepository.findAll().size();

        // Delete the timeTable
        restTimeTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, timeTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TimeTable> timeTableList = timeTableRepository.findAll();
        assertThat(timeTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
