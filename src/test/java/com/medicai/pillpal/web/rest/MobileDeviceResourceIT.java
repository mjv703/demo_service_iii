package com.medicai.pillpal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medicai.pillpal.IntegrationTest;
import com.medicai.pillpal.domain.MobileDevice;
import com.medicai.pillpal.repository.MobileDeviceRepository;
import com.medicai.pillpal.service.dto.MobileDeviceDTO;
import com.medicai.pillpal.service.mapper.MobileDeviceMapper;
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
 * Integration tests for the {@link MobileDeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MobileDeviceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_OS = "AAAAAAAAAA";
    private static final String UPDATED_OS = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mobile-devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MobileDeviceRepository mobileDeviceRepository;

    @Autowired
    private MobileDeviceMapper mobileDeviceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMobileDeviceMockMvc;

    private MobileDevice mobileDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileDevice createEntity(EntityManager em) {
        MobileDevice mobileDevice = new MobileDevice().name(DEFAULT_NAME).os(DEFAULT_OS).deviceId(DEFAULT_DEVICE_ID);
        return mobileDevice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MobileDevice createUpdatedEntity(EntityManager em) {
        MobileDevice mobileDevice = new MobileDevice().name(UPDATED_NAME).os(UPDATED_OS).deviceId(UPDATED_DEVICE_ID);
        return mobileDevice;
    }

    @BeforeEach
    public void initTest() {
        mobileDevice = createEntity(em);
    }

    @Test
    @Transactional
    void createMobileDevice() throws Exception {
        int databaseSizeBeforeCreate = mobileDeviceRepository.findAll().size();
        // Create the MobileDevice
        MobileDeviceDTO mobileDeviceDTO = mobileDeviceMapper.toDto(mobileDevice);
        restMobileDeviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mobileDeviceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MobileDevice in the database
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeCreate + 1);
        MobileDevice testMobileDevice = mobileDeviceList.get(mobileDeviceList.size() - 1);
        assertThat(testMobileDevice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMobileDevice.getOs()).isEqualTo(DEFAULT_OS);
        assertThat(testMobileDevice.getDeviceId()).isEqualTo(DEFAULT_DEVICE_ID);
    }

    @Test
    @Transactional
    void createMobileDeviceWithExistingId() throws Exception {
        // Create the MobileDevice with an existing ID
        mobileDevice.setId(1L);
        MobileDeviceDTO mobileDeviceDTO = mobileDeviceMapper.toDto(mobileDevice);

        int databaseSizeBeforeCreate = mobileDeviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMobileDeviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mobileDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileDevice in the database
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileDeviceRepository.findAll().size();
        // set the field null
        mobileDevice.setName(null);

        // Create the MobileDevice, which fails.
        MobileDeviceDTO mobileDeviceDTO = mobileDeviceMapper.toDto(mobileDevice);

        restMobileDeviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mobileDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOsIsRequired() throws Exception {
        int databaseSizeBeforeTest = mobileDeviceRepository.findAll().size();
        // set the field null
        mobileDevice.setOs(null);

        // Create the MobileDevice, which fails.
        MobileDeviceDTO mobileDeviceDTO = mobileDeviceMapper.toDto(mobileDevice);

        restMobileDeviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mobileDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMobileDevices() throws Exception {
        // Initialize the database
        mobileDeviceRepository.saveAndFlush(mobileDevice);

        // Get all the mobileDeviceList
        restMobileDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mobileDevice.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].os").value(hasItem(DEFAULT_OS)))
            .andExpect(jsonPath("$.[*].deviceId").value(hasItem(DEFAULT_DEVICE_ID)));
    }

    @Test
    @Transactional
    void getMobileDevice() throws Exception {
        // Initialize the database
        mobileDeviceRepository.saveAndFlush(mobileDevice);

        // Get the mobileDevice
        restMobileDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, mobileDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mobileDevice.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.os").value(DEFAULT_OS))
            .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID));
    }

    @Test
    @Transactional
    void getNonExistingMobileDevice() throws Exception {
        // Get the mobileDevice
        restMobileDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMobileDevice() throws Exception {
        // Initialize the database
        mobileDeviceRepository.saveAndFlush(mobileDevice);

        int databaseSizeBeforeUpdate = mobileDeviceRepository.findAll().size();

        // Update the mobileDevice
        MobileDevice updatedMobileDevice = mobileDeviceRepository.findById(mobileDevice.getId()).get();
        // Disconnect from session so that the updates on updatedMobileDevice are not directly saved in db
        em.detach(updatedMobileDevice);
        updatedMobileDevice.name(UPDATED_NAME).os(UPDATED_OS).deviceId(UPDATED_DEVICE_ID);
        MobileDeviceDTO mobileDeviceDTO = mobileDeviceMapper.toDto(updatedMobileDevice);

        restMobileDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mobileDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDeviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the MobileDevice in the database
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeUpdate);
        MobileDevice testMobileDevice = mobileDeviceList.get(mobileDeviceList.size() - 1);
        assertThat(testMobileDevice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMobileDevice.getOs()).isEqualTo(UPDATED_OS);
        assertThat(testMobileDevice.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void putNonExistingMobileDevice() throws Exception {
        int databaseSizeBeforeUpdate = mobileDeviceRepository.findAll().size();
        mobileDevice.setId(count.incrementAndGet());

        // Create the MobileDevice
        MobileDeviceDTO mobileDeviceDTO = mobileDeviceMapper.toDto(mobileDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMobileDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mobileDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileDevice in the database
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMobileDevice() throws Exception {
        int databaseSizeBeforeUpdate = mobileDeviceRepository.findAll().size();
        mobileDevice.setId(count.incrementAndGet());

        // Create the MobileDevice
        MobileDeviceDTO mobileDeviceDTO = mobileDeviceMapper.toDto(mobileDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mobileDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileDevice in the database
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMobileDevice() throws Exception {
        int databaseSizeBeforeUpdate = mobileDeviceRepository.findAll().size();
        mobileDevice.setId(count.incrementAndGet());

        // Create the MobileDevice
        MobileDeviceDTO mobileDeviceDTO = mobileDeviceMapper.toDto(mobileDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileDeviceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mobileDeviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MobileDevice in the database
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMobileDeviceWithPatch() throws Exception {
        // Initialize the database
        mobileDeviceRepository.saveAndFlush(mobileDevice);

        int databaseSizeBeforeUpdate = mobileDeviceRepository.findAll().size();

        // Update the mobileDevice using partial update
        MobileDevice partialUpdatedMobileDevice = new MobileDevice();
        partialUpdatedMobileDevice.setId(mobileDevice.getId());

        partialUpdatedMobileDevice.deviceId(UPDATED_DEVICE_ID);

        restMobileDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMobileDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMobileDevice))
            )
            .andExpect(status().isOk());

        // Validate the MobileDevice in the database
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeUpdate);
        MobileDevice testMobileDevice = mobileDeviceList.get(mobileDeviceList.size() - 1);
        assertThat(testMobileDevice.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMobileDevice.getOs()).isEqualTo(DEFAULT_OS);
        assertThat(testMobileDevice.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void fullUpdateMobileDeviceWithPatch() throws Exception {
        // Initialize the database
        mobileDeviceRepository.saveAndFlush(mobileDevice);

        int databaseSizeBeforeUpdate = mobileDeviceRepository.findAll().size();

        // Update the mobileDevice using partial update
        MobileDevice partialUpdatedMobileDevice = new MobileDevice();
        partialUpdatedMobileDevice.setId(mobileDevice.getId());

        partialUpdatedMobileDevice.name(UPDATED_NAME).os(UPDATED_OS).deviceId(UPDATED_DEVICE_ID);

        restMobileDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMobileDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMobileDevice))
            )
            .andExpect(status().isOk());

        // Validate the MobileDevice in the database
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeUpdate);
        MobileDevice testMobileDevice = mobileDeviceList.get(mobileDeviceList.size() - 1);
        assertThat(testMobileDevice.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMobileDevice.getOs()).isEqualTo(UPDATED_OS);
        assertThat(testMobileDevice.getDeviceId()).isEqualTo(UPDATED_DEVICE_ID);
    }

    @Test
    @Transactional
    void patchNonExistingMobileDevice() throws Exception {
        int databaseSizeBeforeUpdate = mobileDeviceRepository.findAll().size();
        mobileDevice.setId(count.incrementAndGet());

        // Create the MobileDevice
        MobileDeviceDTO mobileDeviceDTO = mobileDeviceMapper.toDto(mobileDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMobileDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mobileDeviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mobileDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileDevice in the database
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMobileDevice() throws Exception {
        int databaseSizeBeforeUpdate = mobileDeviceRepository.findAll().size();
        mobileDevice.setId(count.incrementAndGet());

        // Create the MobileDevice
        MobileDeviceDTO mobileDeviceDTO = mobileDeviceMapper.toDto(mobileDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mobileDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MobileDevice in the database
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMobileDevice() throws Exception {
        int databaseSizeBeforeUpdate = mobileDeviceRepository.findAll().size();
        mobileDevice.setId(count.incrementAndGet());

        // Create the MobileDevice
        MobileDeviceDTO mobileDeviceDTO = mobileDeviceMapper.toDto(mobileDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMobileDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mobileDeviceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MobileDevice in the database
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMobileDevice() throws Exception {
        // Initialize the database
        mobileDeviceRepository.saveAndFlush(mobileDevice);

        int databaseSizeBeforeDelete = mobileDeviceRepository.findAll().size();

        // Delete the mobileDevice
        restMobileDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, mobileDevice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MobileDevice> mobileDeviceList = mobileDeviceRepository.findAll();
        assertThat(mobileDeviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
