package com.medicai.pillpal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medicai.pillpal.IntegrationTest;
import com.medicai.pillpal.domain.Pharmacy;
import com.medicai.pillpal.repository.PharmacyRepository;
import com.medicai.pillpal.service.dto.PharmacyDTO;
import com.medicai.pillpal.service.mapper.PharmacyMapper;
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
 * Integration tests for the {@link PharmacyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PharmacyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STORE = "AAAAAAAAAA";
    private static final String UPDATED_STORE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pharmacies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PharmacyMapper pharmacyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPharmacyMockMvc;

    private Pharmacy pharmacy;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pharmacy createEntity(EntityManager em) {
        Pharmacy pharmacy = new Pharmacy()
            .name(DEFAULT_NAME)
            .store(DEFAULT_STORE)
            .address(DEFAULT_ADDRESS)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return pharmacy;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pharmacy createUpdatedEntity(EntityManager em) {
        Pharmacy pharmacy = new Pharmacy()
            .name(UPDATED_NAME)
            .store(UPDATED_STORE)
            .address(UPDATED_ADDRESS)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return pharmacy;
    }

    @BeforeEach
    public void initTest() {
        pharmacy = createEntity(em);
    }

    @Test
    @Transactional
    void createPharmacy() throws Exception {
        int databaseSizeBeforeCreate = pharmacyRepository.findAll().size();
        // Create the Pharmacy
        PharmacyDTO pharmacyDTO = pharmacyMapper.toDto(pharmacy);
        restPharmacyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pharmacyDTO)))
            .andExpect(status().isCreated());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeCreate + 1);
        Pharmacy testPharmacy = pharmacyList.get(pharmacyList.size() - 1);
        assertThat(testPharmacy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPharmacy.getStore()).isEqualTo(DEFAULT_STORE);
        assertThat(testPharmacy.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPharmacy.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void createPharmacyWithExistingId() throws Exception {
        // Create the Pharmacy with an existing ID
        pharmacy.setId(1L);
        PharmacyDTO pharmacyDTO = pharmacyMapper.toDto(pharmacy);

        int databaseSizeBeforeCreate = pharmacyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPharmacyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pharmacyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = pharmacyRepository.findAll().size();
        // set the field null
        pharmacy.setName(null);

        // Create the Pharmacy, which fails.
        PharmacyDTO pharmacyDTO = pharmacyMapper.toDto(pharmacy);

        restPharmacyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pharmacyDTO)))
            .andExpect(status().isBadRequest());

        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = pharmacyRepository.findAll().size();
        // set the field null
        pharmacy.setStore(null);

        // Create the Pharmacy, which fails.
        PharmacyDTO pharmacyDTO = pharmacyMapper.toDto(pharmacy);

        restPharmacyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pharmacyDTO)))
            .andExpect(status().isBadRequest());

        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPharmacies() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        // Get all the pharmacyList
        restPharmacyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pharmacy.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].store").value(hasItem(DEFAULT_STORE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)));
    }

    @Test
    @Transactional
    void getPharmacy() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        // Get the pharmacy
        restPharmacyMockMvc
            .perform(get(ENTITY_API_URL_ID, pharmacy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pharmacy.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.store").value(DEFAULT_STORE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingPharmacy() throws Exception {
        // Get the pharmacy
        restPharmacyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPharmacy() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();

        // Update the pharmacy
        Pharmacy updatedPharmacy = pharmacyRepository.findById(pharmacy.getId()).get();
        // Disconnect from session so that the updates on updatedPharmacy are not directly saved in db
        em.detach(updatedPharmacy);
        updatedPharmacy.name(UPDATED_NAME).store(UPDATED_STORE).address(UPDATED_ADDRESS).phoneNumber(UPDATED_PHONE_NUMBER);
        PharmacyDTO pharmacyDTO = pharmacyMapper.toDto(updatedPharmacy);

        restPharmacyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pharmacyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pharmacyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
        Pharmacy testPharmacy = pharmacyList.get(pharmacyList.size() - 1);
        assertThat(testPharmacy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPharmacy.getStore()).isEqualTo(UPDATED_STORE);
        assertThat(testPharmacy.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPharmacy.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // Create the Pharmacy
        PharmacyDTO pharmacyDTO = pharmacyMapper.toDto(pharmacy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pharmacyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pharmacyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // Create the Pharmacy
        PharmacyDTO pharmacyDTO = pharmacyMapper.toDto(pharmacy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pharmacyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // Create the Pharmacy
        PharmacyDTO pharmacyDTO = pharmacyMapper.toDto(pharmacy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pharmacyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePharmacyWithPatch() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();

        // Update the pharmacy using partial update
        Pharmacy partialUpdatedPharmacy = new Pharmacy();
        partialUpdatedPharmacy.setId(pharmacy.getId());

        partialUpdatedPharmacy.name(UPDATED_NAME).store(UPDATED_STORE).address(UPDATED_ADDRESS);

        restPharmacyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPharmacy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPharmacy))
            )
            .andExpect(status().isOk());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
        Pharmacy testPharmacy = pharmacyList.get(pharmacyList.size() - 1);
        assertThat(testPharmacy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPharmacy.getStore()).isEqualTo(UPDATED_STORE);
        assertThat(testPharmacy.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPharmacy.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdatePharmacyWithPatch() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();

        // Update the pharmacy using partial update
        Pharmacy partialUpdatedPharmacy = new Pharmacy();
        partialUpdatedPharmacy.setId(pharmacy.getId());

        partialUpdatedPharmacy.name(UPDATED_NAME).store(UPDATED_STORE).address(UPDATED_ADDRESS).phoneNumber(UPDATED_PHONE_NUMBER);

        restPharmacyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPharmacy.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPharmacy))
            )
            .andExpect(status().isOk());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
        Pharmacy testPharmacy = pharmacyList.get(pharmacyList.size() - 1);
        assertThat(testPharmacy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPharmacy.getStore()).isEqualTo(UPDATED_STORE);
        assertThat(testPharmacy.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPharmacy.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // Create the Pharmacy
        PharmacyDTO pharmacyDTO = pharmacyMapper.toDto(pharmacy);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pharmacyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pharmacyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // Create the Pharmacy
        PharmacyDTO pharmacyDTO = pharmacyMapper.toDto(pharmacy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pharmacyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPharmacy() throws Exception {
        int databaseSizeBeforeUpdate = pharmacyRepository.findAll().size();
        pharmacy.setId(count.incrementAndGet());

        // Create the Pharmacy
        PharmacyDTO pharmacyDTO = pharmacyMapper.toDto(pharmacy);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPharmacyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pharmacyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pharmacy in the database
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePharmacy() throws Exception {
        // Initialize the database
        pharmacyRepository.saveAndFlush(pharmacy);

        int databaseSizeBeforeDelete = pharmacyRepository.findAll().size();

        // Delete the pharmacy
        restPharmacyMockMvc
            .perform(delete(ENTITY_API_URL_ID, pharmacy.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pharmacy> pharmacyList = pharmacyRepository.findAll();
        assertThat(pharmacyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
