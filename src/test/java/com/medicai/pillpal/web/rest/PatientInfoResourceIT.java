package com.medicai.pillpal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medicai.pillpal.IntegrationTest;
import com.medicai.pillpal.domain.PatientInfo;
import com.medicai.pillpal.domain.enumeration.BloodType;
import com.medicai.pillpal.domain.enumeration.MaritalStatusType;
import com.medicai.pillpal.repository.PatientInfoRepository;
import com.medicai.pillpal.service.dto.PatientInfoDTO;
import com.medicai.pillpal.service.mapper.PatientInfoMapper;
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
 * Integration tests for the {@link PatientInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientInfoResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ID_NO = "AAAAAAAAAA";
    private static final String UPDATED_ID_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER_1 = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER_1 = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER_2 = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER_2 = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final BloodType DEFAULT_BLOOD_TYPE = BloodType.A_p;
    private static final BloodType UPDATED_BLOOD_TYPE = BloodType.A_n;

    private static final MaritalStatusType DEFAULT_MARITAL_STATUS = MaritalStatusType.SINGLE;
    private static final MaritalStatusType UPDATED_MARITAL_STATUS = MaritalStatusType.SINGLE;

    private static final String DEFAULT_RELATIONSHIP_WITH_USER = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP_WITH_USER = "BBBBBBBBBB";

    private static final String DEFAULT_PATIENT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PATIENT_IMAGE_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/patient-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientInfoRepository patientInfoRepository;

    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientInfoMockMvc;

    private PatientInfo patientInfo;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientInfo createEntity(EntityManager em) {
        PatientInfo patientInfo = new PatientInfo()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .idNo(DEFAULT_ID_NO)
            .address(DEFAULT_ADDRESS)
            .phoneNumber1(DEFAULT_PHONE_NUMBER_1)
            .phoneNumber2(DEFAULT_PHONE_NUMBER_2)
            .email(DEFAULT_EMAIL)
            .height(DEFAULT_HEIGHT)
            .age(DEFAULT_AGE)
            .weight(DEFAULT_WEIGHT)
            .bloodType(DEFAULT_BLOOD_TYPE)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .relationshipWithUser(DEFAULT_RELATIONSHIP_WITH_USER)
            .patientImageUrl(DEFAULT_PATIENT_IMAGE_URL);
        return patientInfo;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientInfo createUpdatedEntity(EntityManager em) {
        PatientInfo patientInfo = new PatientInfo()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .idNo(UPDATED_ID_NO)
            .address(UPDATED_ADDRESS)
            .phoneNumber1(UPDATED_PHONE_NUMBER_1)
            .phoneNumber2(UPDATED_PHONE_NUMBER_2)
            .email(UPDATED_EMAIL)
            .height(UPDATED_HEIGHT)
            .age(UPDATED_AGE)
            .weight(UPDATED_WEIGHT)
            .bloodType(UPDATED_BLOOD_TYPE)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .relationshipWithUser(UPDATED_RELATIONSHIP_WITH_USER)
            .patientImageUrl(UPDATED_PATIENT_IMAGE_URL);
        return patientInfo;
    }

    @BeforeEach
    public void initTest() {
        patientInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createPatientInfo() throws Exception {
        int databaseSizeBeforeCreate = patientInfoRepository.findAll().size();
        // Create the PatientInfo
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(patientInfo);
        restPatientInfoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientInfoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PatientInfo in the database
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeCreate + 1);
        PatientInfo testPatientInfo = patientInfoList.get(patientInfoList.size() - 1);
        assertThat(testPatientInfo.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPatientInfo.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPatientInfo.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPatientInfo.getIdNo()).isEqualTo(DEFAULT_ID_NO);
        assertThat(testPatientInfo.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPatientInfo.getPhoneNumber1()).isEqualTo(DEFAULT_PHONE_NUMBER_1);
        assertThat(testPatientInfo.getPhoneNumber2()).isEqualTo(DEFAULT_PHONE_NUMBER_2);
        assertThat(testPatientInfo.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPatientInfo.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testPatientInfo.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPatientInfo.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testPatientInfo.getBloodType()).isEqualTo(DEFAULT_BLOOD_TYPE);
        assertThat(testPatientInfo.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testPatientInfo.getRelationshipWithUser()).isEqualTo(DEFAULT_RELATIONSHIP_WITH_USER);
        assertThat(testPatientInfo.getPatientImageUrl()).isEqualTo(DEFAULT_PATIENT_IMAGE_URL);
    }

    @Test
    @Transactional
    void createPatientInfoWithExistingId() throws Exception {
        // Create the PatientInfo with an existing ID
        patientInfo.setId(1L);
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(patientInfo);

        int databaseSizeBeforeCreate = patientInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientInfoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientInfo in the database
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientInfoRepository.findAll().size();
        // set the field null
        patientInfo.setFirstName(null);

        // Create the PatientInfo, which fails.
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(patientInfo);

        restPatientInfoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientInfoRepository.findAll().size();
        // set the field null
        patientInfo.setLastName(null);

        // Create the PatientInfo, which fails.
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(patientInfo);

        restPatientInfoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientInfoRepository.findAll().size();
        // set the field null
        patientInfo.setBirthDate(null);

        // Create the PatientInfo, which fails.
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(patientInfo);

        restPatientInfoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPatientInfos() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        // Get all the patientInfoList
        restPatientInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].idNo").value(hasItem(DEFAULT_ID_NO)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phoneNumber1").value(hasItem(DEFAULT_PHONE_NUMBER_1)))
            .andExpect(jsonPath("$.[*].phoneNumber2").value(hasItem(DEFAULT_PHONE_NUMBER_2)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].bloodType").value(hasItem(DEFAULT_BLOOD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].relationshipWithUser").value(hasItem(DEFAULT_RELATIONSHIP_WITH_USER)))
            .andExpect(jsonPath("$.[*].patientImageUrl").value(hasItem(DEFAULT_PATIENT_IMAGE_URL)));
    }

    @Test
    @Transactional
    void getPatientInfo() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        // Get the patientInfo
        restPatientInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, patientInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patientInfo.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.idNo").value(DEFAULT_ID_NO))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phoneNumber1").value(DEFAULT_PHONE_NUMBER_1))
            .andExpect(jsonPath("$.phoneNumber2").value(DEFAULT_PHONE_NUMBER_2))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.bloodType").value(DEFAULT_BLOOD_TYPE.toString()))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.relationshipWithUser").value(DEFAULT_RELATIONSHIP_WITH_USER))
            .andExpect(jsonPath("$.patientImageUrl").value(DEFAULT_PATIENT_IMAGE_URL));
    }

    @Test
    @Transactional
    void getNonExistingPatientInfo() throws Exception {
        // Get the patientInfo
        restPatientInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPatientInfo() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        int databaseSizeBeforeUpdate = patientInfoRepository.findAll().size();

        // Update the patientInfo
        PatientInfo updatedPatientInfo = patientInfoRepository.findById(patientInfo.getId()).get();
        // Disconnect from session so that the updates on updatedPatientInfo are not directly saved in db
        em.detach(updatedPatientInfo);
        updatedPatientInfo
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .idNo(UPDATED_ID_NO)
            .address(UPDATED_ADDRESS)
            .phoneNumber1(UPDATED_PHONE_NUMBER_1)
            .phoneNumber2(UPDATED_PHONE_NUMBER_2)
            .email(UPDATED_EMAIL)
            .height(UPDATED_HEIGHT)
            .age(UPDATED_AGE)
            .weight(UPDATED_WEIGHT)
            .bloodType(UPDATED_BLOOD_TYPE)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .relationshipWithUser(UPDATED_RELATIONSHIP_WITH_USER)
            .patientImageUrl(UPDATED_PATIENT_IMAGE_URL);
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(updatedPatientInfo);

        restPatientInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientInfoDTO))
            )
            .andExpect(status().isOk());

        // Validate the PatientInfo in the database
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeUpdate);
        PatientInfo testPatientInfo = patientInfoList.get(patientInfoList.size() - 1);
        assertThat(testPatientInfo.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPatientInfo.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPatientInfo.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPatientInfo.getIdNo()).isEqualTo(UPDATED_ID_NO);
        assertThat(testPatientInfo.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPatientInfo.getPhoneNumber1()).isEqualTo(UPDATED_PHONE_NUMBER_1);
        assertThat(testPatientInfo.getPhoneNumber2()).isEqualTo(UPDATED_PHONE_NUMBER_2);
        assertThat(testPatientInfo.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatientInfo.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testPatientInfo.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPatientInfo.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testPatientInfo.getBloodType()).isEqualTo(UPDATED_BLOOD_TYPE);
        assertThat(testPatientInfo.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testPatientInfo.getRelationshipWithUser()).isEqualTo(UPDATED_RELATIONSHIP_WITH_USER);
        assertThat(testPatientInfo.getPatientImageUrl()).isEqualTo(UPDATED_PATIENT_IMAGE_URL);
    }

    @Test
    @Transactional
    void putNonExistingPatientInfo() throws Exception {
        int databaseSizeBeforeUpdate = patientInfoRepository.findAll().size();
        patientInfo.setId(count.incrementAndGet());

        // Create the PatientInfo
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(patientInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientInfoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientInfo in the database
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatientInfo() throws Exception {
        int databaseSizeBeforeUpdate = patientInfoRepository.findAll().size();
        patientInfo.setId(count.incrementAndGet());

        // Create the PatientInfo
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(patientInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientInfo in the database
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatientInfo() throws Exception {
        int databaseSizeBeforeUpdate = patientInfoRepository.findAll().size();
        patientInfo.setId(count.incrementAndGet());

        // Create the PatientInfo
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(patientInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientInfoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientInfo in the database
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientInfoWithPatch() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        int databaseSizeBeforeUpdate = patientInfoRepository.findAll().size();

        // Update the patientInfo using partial update
        PatientInfo partialUpdatedPatientInfo = new PatientInfo();
        partialUpdatedPatientInfo.setId(patientInfo.getId());

        partialUpdatedPatientInfo.firstName(UPDATED_FIRST_NAME).email(UPDATED_EMAIL).age(UPDATED_AGE).weight(UPDATED_WEIGHT);

        restPatientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientInfo))
            )
            .andExpect(status().isOk());

        // Validate the PatientInfo in the database
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeUpdate);
        PatientInfo testPatientInfo = patientInfoList.get(patientInfoList.size() - 1);
        assertThat(testPatientInfo.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPatientInfo.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPatientInfo.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testPatientInfo.getIdNo()).isEqualTo(DEFAULT_ID_NO);
        assertThat(testPatientInfo.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testPatientInfo.getPhoneNumber1()).isEqualTo(DEFAULT_PHONE_NUMBER_1);
        assertThat(testPatientInfo.getPhoneNumber2()).isEqualTo(DEFAULT_PHONE_NUMBER_2);
        assertThat(testPatientInfo.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatientInfo.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testPatientInfo.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPatientInfo.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testPatientInfo.getBloodType()).isEqualTo(DEFAULT_BLOOD_TYPE);
        assertThat(testPatientInfo.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testPatientInfo.getRelationshipWithUser()).isEqualTo(DEFAULT_RELATIONSHIP_WITH_USER);
        assertThat(testPatientInfo.getPatientImageUrl()).isEqualTo(DEFAULT_PATIENT_IMAGE_URL);
    }

    @Test
    @Transactional
    void fullUpdatePatientInfoWithPatch() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        int databaseSizeBeforeUpdate = patientInfoRepository.findAll().size();

        // Update the patientInfo using partial update
        PatientInfo partialUpdatedPatientInfo = new PatientInfo();
        partialUpdatedPatientInfo.setId(patientInfo.getId());

        partialUpdatedPatientInfo
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .idNo(UPDATED_ID_NO)
            .address(UPDATED_ADDRESS)
            .phoneNumber1(UPDATED_PHONE_NUMBER_1)
            .phoneNumber2(UPDATED_PHONE_NUMBER_2)
            .email(UPDATED_EMAIL)
            .height(UPDATED_HEIGHT)
            .age(UPDATED_AGE)
            .weight(UPDATED_WEIGHT)
            .bloodType(UPDATED_BLOOD_TYPE)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .relationshipWithUser(UPDATED_RELATIONSHIP_WITH_USER)
            .patientImageUrl(UPDATED_PATIENT_IMAGE_URL);

        restPatientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientInfo))
            )
            .andExpect(status().isOk());

        // Validate the PatientInfo in the database
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeUpdate);
        PatientInfo testPatientInfo = patientInfoList.get(patientInfoList.size() - 1);
        assertThat(testPatientInfo.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPatientInfo.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPatientInfo.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testPatientInfo.getIdNo()).isEqualTo(UPDATED_ID_NO);
        assertThat(testPatientInfo.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testPatientInfo.getPhoneNumber1()).isEqualTo(UPDATED_PHONE_NUMBER_1);
        assertThat(testPatientInfo.getPhoneNumber2()).isEqualTo(UPDATED_PHONE_NUMBER_2);
        assertThat(testPatientInfo.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPatientInfo.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testPatientInfo.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPatientInfo.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testPatientInfo.getBloodType()).isEqualTo(UPDATED_BLOOD_TYPE);
        assertThat(testPatientInfo.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testPatientInfo.getRelationshipWithUser()).isEqualTo(UPDATED_RELATIONSHIP_WITH_USER);
        assertThat(testPatientInfo.getPatientImageUrl()).isEqualTo(UPDATED_PATIENT_IMAGE_URL);
    }

    @Test
    @Transactional
    void patchNonExistingPatientInfo() throws Exception {
        int databaseSizeBeforeUpdate = patientInfoRepository.findAll().size();
        patientInfo.setId(count.incrementAndGet());

        // Create the PatientInfo
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(patientInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientInfoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientInfo in the database
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatientInfo() throws Exception {
        int databaseSizeBeforeUpdate = patientInfoRepository.findAll().size();
        patientInfo.setId(count.incrementAndGet());

        // Create the PatientInfo
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(patientInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientInfoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientInfo in the database
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatientInfo() throws Exception {
        int databaseSizeBeforeUpdate = patientInfoRepository.findAll().size();
        patientInfo.setId(count.incrementAndGet());

        // Create the PatientInfo
        PatientInfoDTO patientInfoDTO = patientInfoMapper.toDto(patientInfo);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(patientInfoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientInfo in the database
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatientInfo() throws Exception {
        // Initialize the database
        patientInfoRepository.saveAndFlush(patientInfo);

        int databaseSizeBeforeDelete = patientInfoRepository.findAll().size();

        // Delete the patientInfo
        restPatientInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, patientInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PatientInfo> patientInfoList = patientInfoRepository.findAll();
        assertThat(patientInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
