package com.medicai.pillpal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medicai.pillpal.IntegrationTest;
import com.medicai.pillpal.domain.Device;
import com.medicai.pillpal.domain.Medicine;
import com.medicai.pillpal.domain.PatientInfo;
import com.medicai.pillpal.domain.Pharmacy;
import com.medicai.pillpal.domain.Prescription;
import com.medicai.pillpal.domain.TimeTable;
import com.medicai.pillpal.domain.enumeration.ColorContentType;
import com.medicai.pillpal.domain.enumeration.MedicType;
import com.medicai.pillpal.domain.enumeration.PrescriptionStatusType;
import com.medicai.pillpal.repository.PrescriptionRepository;
import com.medicai.pillpal.service.criteria.PrescriptionCriteria;
import com.medicai.pillpal.service.dto.PrescriptionDTO;
import com.medicai.pillpal.service.mapper.PrescriptionMapper;
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
 * Integration tests for the {@link PrescriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PrescriptionResourceIT {

    private static final String DEFAULT_PRESCRIPTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRESCRIPTION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_BAR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BAR_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_USAGE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_USAGE_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IMPORTANT_INFO = "AAAAAAAAAA";
    private static final String UPDATED_IMPORTANT_INFO = "BBBBBBBBBB";

    private static final Integer DEFAULT_QTY = 1;
    private static final Integer UPDATED_QTY = 2;
    private static final Integer SMALLER_QTY = 1 - 1;

    private static final Boolean DEFAULT_HAS_REFILL = false;
    private static final Boolean UPDATED_HAS_REFILL = true;

    private static final Instant DEFAULT_REFILL_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REFILL_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PRESCRIPTION_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PRESCRIPTION_IMAGE_URL = "BBBBBBBBBB";

    private static final MedicType DEFAULT_MEDIC_TYPE = MedicType.OTHER;
    private static final MedicType UPDATED_MEDIC_TYPE = MedicType.INJECTION;

    private static final ColorContentType DEFAULT_MEDIC_COLOR = ColorContentType.C_RED;
    private static final ColorContentType UPDATED_MEDIC_COLOR = ColorContentType.C_BLUE;

    private static final PrescriptionStatusType DEFAULT_STATUS = PrescriptionStatusType.ACTIVE;
    private static final PrescriptionStatusType UPDATED_STATUS = PrescriptionStatusType.INACTIVE;

    private static final String ENTITY_API_URL = "/api/prescriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionMapper prescriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPrescriptionMockMvc;

    private Prescription prescription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prescription createEntity(EntityManager em) {
        Prescription prescription = new Prescription()
            .prescriptionCode(DEFAULT_PRESCRIPTION_CODE)
            .barCode(DEFAULT_BAR_CODE)
            .issueDate(DEFAULT_ISSUE_DATE)
            .usageDescription(DEFAULT_USAGE_DESCRIPTION)
            .importantInfo(DEFAULT_IMPORTANT_INFO)
            .qty(DEFAULT_QTY)
            .hasRefill(DEFAULT_HAS_REFILL)
            .refillTime(DEFAULT_REFILL_TIME)
            .prescriptionImageUrl(DEFAULT_PRESCRIPTION_IMAGE_URL)
            .medicType(DEFAULT_MEDIC_TYPE)
            .medicColor(DEFAULT_MEDIC_COLOR)
            .status(DEFAULT_STATUS);
        return prescription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Prescription createUpdatedEntity(EntityManager em) {
        Prescription prescription = new Prescription()
            .prescriptionCode(UPDATED_PRESCRIPTION_CODE)
            .barCode(UPDATED_BAR_CODE)
            .issueDate(UPDATED_ISSUE_DATE)
            .usageDescription(UPDATED_USAGE_DESCRIPTION)
            .importantInfo(UPDATED_IMPORTANT_INFO)
            .qty(UPDATED_QTY)
            .hasRefill(UPDATED_HAS_REFILL)
            .refillTime(UPDATED_REFILL_TIME)
            .prescriptionImageUrl(UPDATED_PRESCRIPTION_IMAGE_URL)
            .medicType(UPDATED_MEDIC_TYPE)
            .medicColor(UPDATED_MEDIC_COLOR)
            .status(UPDATED_STATUS);
        return prescription;
    }

    @BeforeEach
    public void initTest() {
        prescription = createEntity(em);
    }

    @Test
    @Transactional
    void createPrescription() throws Exception {
        int databaseSizeBeforeCreate = prescriptionRepository.findAll().size();
        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);
        restPrescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getPrescriptionCode()).isEqualTo(DEFAULT_PRESCRIPTION_CODE);
        assertThat(testPrescription.getBarCode()).isEqualTo(DEFAULT_BAR_CODE);
        assertThat(testPrescription.getIssueDate()).isEqualTo(DEFAULT_ISSUE_DATE);
        assertThat(testPrescription.getUsageDescription()).isEqualTo(DEFAULT_USAGE_DESCRIPTION);
        assertThat(testPrescription.getImportantInfo()).isEqualTo(DEFAULT_IMPORTANT_INFO);
        assertThat(testPrescription.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testPrescription.getHasRefill()).isEqualTo(DEFAULT_HAS_REFILL);
        assertThat(testPrescription.getRefillTime()).isEqualTo(DEFAULT_REFILL_TIME);
        assertThat(testPrescription.getPrescriptionImageUrl()).isEqualTo(DEFAULT_PRESCRIPTION_IMAGE_URL);
        assertThat(testPrescription.getMedicType()).isEqualTo(DEFAULT_MEDIC_TYPE);
        assertThat(testPrescription.getMedicColor()).isEqualTo(DEFAULT_MEDIC_COLOR);
        assertThat(testPrescription.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createPrescriptionWithExistingId() throws Exception {
        // Create the Prescription with an existing ID
        prescription.setId(1L);
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        int databaseSizeBeforeCreate = prescriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIssueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = prescriptionRepository.findAll().size();
        // set the field null
        prescription.setIssueDate(null);

        // Create the Prescription, which fails.
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        restPrescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsageDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = prescriptionRepository.findAll().size();
        // set the field null
        prescription.setUsageDescription(null);

        // Create the Prescription, which fails.
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        restPrescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImportantInfoIsRequired() throws Exception {
        int databaseSizeBeforeTest = prescriptionRepository.findAll().size();
        // set the field null
        prescription.setImportantInfo(null);

        // Create the Prescription, which fails.
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        restPrescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQtyIsRequired() throws Exception {
        int databaseSizeBeforeTest = prescriptionRepository.findAll().size();
        // set the field null
        prescription.setQty(null);

        // Create the Prescription, which fails.
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        restPrescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHasRefillIsRequired() throws Exception {
        int databaseSizeBeforeTest = prescriptionRepository.findAll().size();
        // set the field null
        prescription.setHasRefill(null);

        // Create the Prescription, which fails.
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        restPrescriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrescriptions() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList
        restPrescriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].prescriptionCode").value(hasItem(DEFAULT_PRESCRIPTION_CODE)))
            .andExpect(jsonPath("$.[*].barCode").value(hasItem(DEFAULT_BAR_CODE)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].usageDescription").value(hasItem(DEFAULT_USAGE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].importantInfo").value(hasItem(DEFAULT_IMPORTANT_INFO)))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].hasRefill").value(hasItem(DEFAULT_HAS_REFILL.booleanValue())))
            .andExpect(jsonPath("$.[*].refillTime").value(hasItem(DEFAULT_REFILL_TIME.toString())))
            .andExpect(jsonPath("$.[*].prescriptionImageUrl").value(hasItem(DEFAULT_PRESCRIPTION_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].medicType").value(hasItem(DEFAULT_MEDIC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].medicColor").value(hasItem(DEFAULT_MEDIC_COLOR.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get the prescription
        restPrescriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, prescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(prescription.getId().intValue()))
            .andExpect(jsonPath("$.prescriptionCode").value(DEFAULT_PRESCRIPTION_CODE))
            .andExpect(jsonPath("$.barCode").value(DEFAULT_BAR_CODE))
            .andExpect(jsonPath("$.issueDate").value(DEFAULT_ISSUE_DATE.toString()))
            .andExpect(jsonPath("$.usageDescription").value(DEFAULT_USAGE_DESCRIPTION))
            .andExpect(jsonPath("$.importantInfo").value(DEFAULT_IMPORTANT_INFO))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY))
            .andExpect(jsonPath("$.hasRefill").value(DEFAULT_HAS_REFILL.booleanValue()))
            .andExpect(jsonPath("$.refillTime").value(DEFAULT_REFILL_TIME.toString()))
            .andExpect(jsonPath("$.prescriptionImageUrl").value(DEFAULT_PRESCRIPTION_IMAGE_URL))
            .andExpect(jsonPath("$.medicType").value(DEFAULT_MEDIC_TYPE.toString()))
            .andExpect(jsonPath("$.medicColor").value(DEFAULT_MEDIC_COLOR.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getPrescriptionsByIdFiltering() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        Long id = prescription.getId();

        defaultPrescriptionShouldBeFound("id.equals=" + id);
        defaultPrescriptionShouldNotBeFound("id.notEquals=" + id);

        defaultPrescriptionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPrescriptionShouldNotBeFound("id.greaterThan=" + id);

        defaultPrescriptionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPrescriptionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionCode equals to DEFAULT_PRESCRIPTION_CODE
        defaultPrescriptionShouldBeFound("prescriptionCode.equals=" + DEFAULT_PRESCRIPTION_CODE);

        // Get all the prescriptionList where prescriptionCode equals to UPDATED_PRESCRIPTION_CODE
        defaultPrescriptionShouldNotBeFound("prescriptionCode.equals=" + UPDATED_PRESCRIPTION_CODE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionCode not equals to DEFAULT_PRESCRIPTION_CODE
        defaultPrescriptionShouldNotBeFound("prescriptionCode.notEquals=" + DEFAULT_PRESCRIPTION_CODE);

        // Get all the prescriptionList where prescriptionCode not equals to UPDATED_PRESCRIPTION_CODE
        defaultPrescriptionShouldBeFound("prescriptionCode.notEquals=" + UPDATED_PRESCRIPTION_CODE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionCodeIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionCode in DEFAULT_PRESCRIPTION_CODE or UPDATED_PRESCRIPTION_CODE
        defaultPrescriptionShouldBeFound("prescriptionCode.in=" + DEFAULT_PRESCRIPTION_CODE + "," + UPDATED_PRESCRIPTION_CODE);

        // Get all the prescriptionList where prescriptionCode equals to UPDATED_PRESCRIPTION_CODE
        defaultPrescriptionShouldNotBeFound("prescriptionCode.in=" + UPDATED_PRESCRIPTION_CODE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionCode is not null
        defaultPrescriptionShouldBeFound("prescriptionCode.specified=true");

        // Get all the prescriptionList where prescriptionCode is null
        defaultPrescriptionShouldNotBeFound("prescriptionCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionCodeContainsSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionCode contains DEFAULT_PRESCRIPTION_CODE
        defaultPrescriptionShouldBeFound("prescriptionCode.contains=" + DEFAULT_PRESCRIPTION_CODE);

        // Get all the prescriptionList where prescriptionCode contains UPDATED_PRESCRIPTION_CODE
        defaultPrescriptionShouldNotBeFound("prescriptionCode.contains=" + UPDATED_PRESCRIPTION_CODE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionCodeNotContainsSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionCode does not contain DEFAULT_PRESCRIPTION_CODE
        defaultPrescriptionShouldNotBeFound("prescriptionCode.doesNotContain=" + DEFAULT_PRESCRIPTION_CODE);

        // Get all the prescriptionList where prescriptionCode does not contain UPDATED_PRESCRIPTION_CODE
        defaultPrescriptionShouldBeFound("prescriptionCode.doesNotContain=" + UPDATED_PRESCRIPTION_CODE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByBarCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where barCode equals to DEFAULT_BAR_CODE
        defaultPrescriptionShouldBeFound("barCode.equals=" + DEFAULT_BAR_CODE);

        // Get all the prescriptionList where barCode equals to UPDATED_BAR_CODE
        defaultPrescriptionShouldNotBeFound("barCode.equals=" + UPDATED_BAR_CODE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByBarCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where barCode not equals to DEFAULT_BAR_CODE
        defaultPrescriptionShouldNotBeFound("barCode.notEquals=" + DEFAULT_BAR_CODE);

        // Get all the prescriptionList where barCode not equals to UPDATED_BAR_CODE
        defaultPrescriptionShouldBeFound("barCode.notEquals=" + UPDATED_BAR_CODE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByBarCodeIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where barCode in DEFAULT_BAR_CODE or UPDATED_BAR_CODE
        defaultPrescriptionShouldBeFound("barCode.in=" + DEFAULT_BAR_CODE + "," + UPDATED_BAR_CODE);

        // Get all the prescriptionList where barCode equals to UPDATED_BAR_CODE
        defaultPrescriptionShouldNotBeFound("barCode.in=" + UPDATED_BAR_CODE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByBarCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where barCode is not null
        defaultPrescriptionShouldBeFound("barCode.specified=true");

        // Get all the prescriptionList where barCode is null
        defaultPrescriptionShouldNotBeFound("barCode.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByBarCodeContainsSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where barCode contains DEFAULT_BAR_CODE
        defaultPrescriptionShouldBeFound("barCode.contains=" + DEFAULT_BAR_CODE);

        // Get all the prescriptionList where barCode contains UPDATED_BAR_CODE
        defaultPrescriptionShouldNotBeFound("barCode.contains=" + UPDATED_BAR_CODE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByBarCodeNotContainsSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where barCode does not contain DEFAULT_BAR_CODE
        defaultPrescriptionShouldNotBeFound("barCode.doesNotContain=" + DEFAULT_BAR_CODE);

        // Get all the prescriptionList where barCode does not contain UPDATED_BAR_CODE
        defaultPrescriptionShouldBeFound("barCode.doesNotContain=" + UPDATED_BAR_CODE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByIssueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where issueDate equals to DEFAULT_ISSUE_DATE
        defaultPrescriptionShouldBeFound("issueDate.equals=" + DEFAULT_ISSUE_DATE);

        // Get all the prescriptionList where issueDate equals to UPDATED_ISSUE_DATE
        defaultPrescriptionShouldNotBeFound("issueDate.equals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByIssueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where issueDate not equals to DEFAULT_ISSUE_DATE
        defaultPrescriptionShouldNotBeFound("issueDate.notEquals=" + DEFAULT_ISSUE_DATE);

        // Get all the prescriptionList where issueDate not equals to UPDATED_ISSUE_DATE
        defaultPrescriptionShouldBeFound("issueDate.notEquals=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByIssueDateIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where issueDate in DEFAULT_ISSUE_DATE or UPDATED_ISSUE_DATE
        defaultPrescriptionShouldBeFound("issueDate.in=" + DEFAULT_ISSUE_DATE + "," + UPDATED_ISSUE_DATE);

        // Get all the prescriptionList where issueDate equals to UPDATED_ISSUE_DATE
        defaultPrescriptionShouldNotBeFound("issueDate.in=" + UPDATED_ISSUE_DATE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByIssueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where issueDate is not null
        defaultPrescriptionShouldBeFound("issueDate.specified=true");

        // Get all the prescriptionList where issueDate is null
        defaultPrescriptionShouldNotBeFound("issueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByUsageDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where usageDescription equals to DEFAULT_USAGE_DESCRIPTION
        defaultPrescriptionShouldBeFound("usageDescription.equals=" + DEFAULT_USAGE_DESCRIPTION);

        // Get all the prescriptionList where usageDescription equals to UPDATED_USAGE_DESCRIPTION
        defaultPrescriptionShouldNotBeFound("usageDescription.equals=" + UPDATED_USAGE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByUsageDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where usageDescription not equals to DEFAULT_USAGE_DESCRIPTION
        defaultPrescriptionShouldNotBeFound("usageDescription.notEquals=" + DEFAULT_USAGE_DESCRIPTION);

        // Get all the prescriptionList where usageDescription not equals to UPDATED_USAGE_DESCRIPTION
        defaultPrescriptionShouldBeFound("usageDescription.notEquals=" + UPDATED_USAGE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByUsageDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where usageDescription in DEFAULT_USAGE_DESCRIPTION or UPDATED_USAGE_DESCRIPTION
        defaultPrescriptionShouldBeFound("usageDescription.in=" + DEFAULT_USAGE_DESCRIPTION + "," + UPDATED_USAGE_DESCRIPTION);

        // Get all the prescriptionList where usageDescription equals to UPDATED_USAGE_DESCRIPTION
        defaultPrescriptionShouldNotBeFound("usageDescription.in=" + UPDATED_USAGE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByUsageDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where usageDescription is not null
        defaultPrescriptionShouldBeFound("usageDescription.specified=true");

        // Get all the prescriptionList where usageDescription is null
        defaultPrescriptionShouldNotBeFound("usageDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByUsageDescriptionContainsSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where usageDescription contains DEFAULT_USAGE_DESCRIPTION
        defaultPrescriptionShouldBeFound("usageDescription.contains=" + DEFAULT_USAGE_DESCRIPTION);

        // Get all the prescriptionList where usageDescription contains UPDATED_USAGE_DESCRIPTION
        defaultPrescriptionShouldNotBeFound("usageDescription.contains=" + UPDATED_USAGE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByUsageDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where usageDescription does not contain DEFAULT_USAGE_DESCRIPTION
        defaultPrescriptionShouldNotBeFound("usageDescription.doesNotContain=" + DEFAULT_USAGE_DESCRIPTION);

        // Get all the prescriptionList where usageDescription does not contain UPDATED_USAGE_DESCRIPTION
        defaultPrescriptionShouldBeFound("usageDescription.doesNotContain=" + UPDATED_USAGE_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByImportantInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where importantInfo equals to DEFAULT_IMPORTANT_INFO
        defaultPrescriptionShouldBeFound("importantInfo.equals=" + DEFAULT_IMPORTANT_INFO);

        // Get all the prescriptionList where importantInfo equals to UPDATED_IMPORTANT_INFO
        defaultPrescriptionShouldNotBeFound("importantInfo.equals=" + UPDATED_IMPORTANT_INFO);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByImportantInfoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where importantInfo not equals to DEFAULT_IMPORTANT_INFO
        defaultPrescriptionShouldNotBeFound("importantInfo.notEquals=" + DEFAULT_IMPORTANT_INFO);

        // Get all the prescriptionList where importantInfo not equals to UPDATED_IMPORTANT_INFO
        defaultPrescriptionShouldBeFound("importantInfo.notEquals=" + UPDATED_IMPORTANT_INFO);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByImportantInfoIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where importantInfo in DEFAULT_IMPORTANT_INFO or UPDATED_IMPORTANT_INFO
        defaultPrescriptionShouldBeFound("importantInfo.in=" + DEFAULT_IMPORTANT_INFO + "," + UPDATED_IMPORTANT_INFO);

        // Get all the prescriptionList where importantInfo equals to UPDATED_IMPORTANT_INFO
        defaultPrescriptionShouldNotBeFound("importantInfo.in=" + UPDATED_IMPORTANT_INFO);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByImportantInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where importantInfo is not null
        defaultPrescriptionShouldBeFound("importantInfo.specified=true");

        // Get all the prescriptionList where importantInfo is null
        defaultPrescriptionShouldNotBeFound("importantInfo.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByImportantInfoContainsSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where importantInfo contains DEFAULT_IMPORTANT_INFO
        defaultPrescriptionShouldBeFound("importantInfo.contains=" + DEFAULT_IMPORTANT_INFO);

        // Get all the prescriptionList where importantInfo contains UPDATED_IMPORTANT_INFO
        defaultPrescriptionShouldNotBeFound("importantInfo.contains=" + UPDATED_IMPORTANT_INFO);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByImportantInfoNotContainsSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where importantInfo does not contain DEFAULT_IMPORTANT_INFO
        defaultPrescriptionShouldNotBeFound("importantInfo.doesNotContain=" + DEFAULT_IMPORTANT_INFO);

        // Get all the prescriptionList where importantInfo does not contain UPDATED_IMPORTANT_INFO
        defaultPrescriptionShouldBeFound("importantInfo.doesNotContain=" + UPDATED_IMPORTANT_INFO);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByQtyIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where qty equals to DEFAULT_QTY
        defaultPrescriptionShouldBeFound("qty.equals=" + DEFAULT_QTY);

        // Get all the prescriptionList where qty equals to UPDATED_QTY
        defaultPrescriptionShouldNotBeFound("qty.equals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByQtyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where qty not equals to DEFAULT_QTY
        defaultPrescriptionShouldNotBeFound("qty.notEquals=" + DEFAULT_QTY);

        // Get all the prescriptionList where qty not equals to UPDATED_QTY
        defaultPrescriptionShouldBeFound("qty.notEquals=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByQtyIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where qty in DEFAULT_QTY or UPDATED_QTY
        defaultPrescriptionShouldBeFound("qty.in=" + DEFAULT_QTY + "," + UPDATED_QTY);

        // Get all the prescriptionList where qty equals to UPDATED_QTY
        defaultPrescriptionShouldNotBeFound("qty.in=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByQtyIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where qty is not null
        defaultPrescriptionShouldBeFound("qty.specified=true");

        // Get all the prescriptionList where qty is null
        defaultPrescriptionShouldNotBeFound("qty.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByQtyIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where qty is greater than or equal to DEFAULT_QTY
        defaultPrescriptionShouldBeFound("qty.greaterThanOrEqual=" + DEFAULT_QTY);

        // Get all the prescriptionList where qty is greater than or equal to UPDATED_QTY
        defaultPrescriptionShouldNotBeFound("qty.greaterThanOrEqual=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByQtyIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where qty is less than or equal to DEFAULT_QTY
        defaultPrescriptionShouldBeFound("qty.lessThanOrEqual=" + DEFAULT_QTY);

        // Get all the prescriptionList where qty is less than or equal to SMALLER_QTY
        defaultPrescriptionShouldNotBeFound("qty.lessThanOrEqual=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByQtyIsLessThanSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where qty is less than DEFAULT_QTY
        defaultPrescriptionShouldNotBeFound("qty.lessThan=" + DEFAULT_QTY);

        // Get all the prescriptionList where qty is less than UPDATED_QTY
        defaultPrescriptionShouldBeFound("qty.lessThan=" + UPDATED_QTY);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByQtyIsGreaterThanSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where qty is greater than DEFAULT_QTY
        defaultPrescriptionShouldNotBeFound("qty.greaterThan=" + DEFAULT_QTY);

        // Get all the prescriptionList where qty is greater than SMALLER_QTY
        defaultPrescriptionShouldBeFound("qty.greaterThan=" + SMALLER_QTY);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByHasRefillIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where hasRefill equals to DEFAULT_HAS_REFILL
        defaultPrescriptionShouldBeFound("hasRefill.equals=" + DEFAULT_HAS_REFILL);

        // Get all the prescriptionList where hasRefill equals to UPDATED_HAS_REFILL
        defaultPrescriptionShouldNotBeFound("hasRefill.equals=" + UPDATED_HAS_REFILL);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByHasRefillIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where hasRefill not equals to DEFAULT_HAS_REFILL
        defaultPrescriptionShouldNotBeFound("hasRefill.notEquals=" + DEFAULT_HAS_REFILL);

        // Get all the prescriptionList where hasRefill not equals to UPDATED_HAS_REFILL
        defaultPrescriptionShouldBeFound("hasRefill.notEquals=" + UPDATED_HAS_REFILL);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByHasRefillIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where hasRefill in DEFAULT_HAS_REFILL or UPDATED_HAS_REFILL
        defaultPrescriptionShouldBeFound("hasRefill.in=" + DEFAULT_HAS_REFILL + "," + UPDATED_HAS_REFILL);

        // Get all the prescriptionList where hasRefill equals to UPDATED_HAS_REFILL
        defaultPrescriptionShouldNotBeFound("hasRefill.in=" + UPDATED_HAS_REFILL);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByHasRefillIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where hasRefill is not null
        defaultPrescriptionShouldBeFound("hasRefill.specified=true");

        // Get all the prescriptionList where hasRefill is null
        defaultPrescriptionShouldNotBeFound("hasRefill.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByRefillTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where refillTime equals to DEFAULT_REFILL_TIME
        defaultPrescriptionShouldBeFound("refillTime.equals=" + DEFAULT_REFILL_TIME);

        // Get all the prescriptionList where refillTime equals to UPDATED_REFILL_TIME
        defaultPrescriptionShouldNotBeFound("refillTime.equals=" + UPDATED_REFILL_TIME);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByRefillTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where refillTime not equals to DEFAULT_REFILL_TIME
        defaultPrescriptionShouldNotBeFound("refillTime.notEquals=" + DEFAULT_REFILL_TIME);

        // Get all the prescriptionList where refillTime not equals to UPDATED_REFILL_TIME
        defaultPrescriptionShouldBeFound("refillTime.notEquals=" + UPDATED_REFILL_TIME);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByRefillTimeIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where refillTime in DEFAULT_REFILL_TIME or UPDATED_REFILL_TIME
        defaultPrescriptionShouldBeFound("refillTime.in=" + DEFAULT_REFILL_TIME + "," + UPDATED_REFILL_TIME);

        // Get all the prescriptionList where refillTime equals to UPDATED_REFILL_TIME
        defaultPrescriptionShouldNotBeFound("refillTime.in=" + UPDATED_REFILL_TIME);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByRefillTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where refillTime is not null
        defaultPrescriptionShouldBeFound("refillTime.specified=true");

        // Get all the prescriptionList where refillTime is null
        defaultPrescriptionShouldNotBeFound("refillTime.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionImageUrl equals to DEFAULT_PRESCRIPTION_IMAGE_URL
        defaultPrescriptionShouldBeFound("prescriptionImageUrl.equals=" + DEFAULT_PRESCRIPTION_IMAGE_URL);

        // Get all the prescriptionList where prescriptionImageUrl equals to UPDATED_PRESCRIPTION_IMAGE_URL
        defaultPrescriptionShouldNotBeFound("prescriptionImageUrl.equals=" + UPDATED_PRESCRIPTION_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionImageUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionImageUrl not equals to DEFAULT_PRESCRIPTION_IMAGE_URL
        defaultPrescriptionShouldNotBeFound("prescriptionImageUrl.notEquals=" + DEFAULT_PRESCRIPTION_IMAGE_URL);

        // Get all the prescriptionList where prescriptionImageUrl not equals to UPDATED_PRESCRIPTION_IMAGE_URL
        defaultPrescriptionShouldBeFound("prescriptionImageUrl.notEquals=" + UPDATED_PRESCRIPTION_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionImageUrl in DEFAULT_PRESCRIPTION_IMAGE_URL or UPDATED_PRESCRIPTION_IMAGE_URL
        defaultPrescriptionShouldBeFound(
            "prescriptionImageUrl.in=" + DEFAULT_PRESCRIPTION_IMAGE_URL + "," + UPDATED_PRESCRIPTION_IMAGE_URL
        );

        // Get all the prescriptionList where prescriptionImageUrl equals to UPDATED_PRESCRIPTION_IMAGE_URL
        defaultPrescriptionShouldNotBeFound("prescriptionImageUrl.in=" + UPDATED_PRESCRIPTION_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionImageUrl is not null
        defaultPrescriptionShouldBeFound("prescriptionImageUrl.specified=true");

        // Get all the prescriptionList where prescriptionImageUrl is null
        defaultPrescriptionShouldNotBeFound("prescriptionImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionImageUrlContainsSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionImageUrl contains DEFAULT_PRESCRIPTION_IMAGE_URL
        defaultPrescriptionShouldBeFound("prescriptionImageUrl.contains=" + DEFAULT_PRESCRIPTION_IMAGE_URL);

        // Get all the prescriptionList where prescriptionImageUrl contains UPDATED_PRESCRIPTION_IMAGE_URL
        defaultPrescriptionShouldNotBeFound("prescriptionImageUrl.contains=" + UPDATED_PRESCRIPTION_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPrescriptionImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where prescriptionImageUrl does not contain DEFAULT_PRESCRIPTION_IMAGE_URL
        defaultPrescriptionShouldNotBeFound("prescriptionImageUrl.doesNotContain=" + DEFAULT_PRESCRIPTION_IMAGE_URL);

        // Get all the prescriptionList where prescriptionImageUrl does not contain UPDATED_PRESCRIPTION_IMAGE_URL
        defaultPrescriptionShouldBeFound("prescriptionImageUrl.doesNotContain=" + UPDATED_PRESCRIPTION_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByMedicTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where medicType equals to DEFAULT_MEDIC_TYPE
        defaultPrescriptionShouldBeFound("medicType.equals=" + DEFAULT_MEDIC_TYPE);

        // Get all the prescriptionList where medicType equals to UPDATED_MEDIC_TYPE
        defaultPrescriptionShouldNotBeFound("medicType.equals=" + UPDATED_MEDIC_TYPE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByMedicTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where medicType not equals to DEFAULT_MEDIC_TYPE
        defaultPrescriptionShouldNotBeFound("medicType.notEquals=" + DEFAULT_MEDIC_TYPE);

        // Get all the prescriptionList where medicType not equals to UPDATED_MEDIC_TYPE
        defaultPrescriptionShouldBeFound("medicType.notEquals=" + UPDATED_MEDIC_TYPE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByMedicTypeIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where medicType in DEFAULT_MEDIC_TYPE or UPDATED_MEDIC_TYPE
        defaultPrescriptionShouldBeFound("medicType.in=" + DEFAULT_MEDIC_TYPE + "," + UPDATED_MEDIC_TYPE);

        // Get all the prescriptionList where medicType equals to UPDATED_MEDIC_TYPE
        defaultPrescriptionShouldNotBeFound("medicType.in=" + UPDATED_MEDIC_TYPE);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByMedicTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where medicType is not null
        defaultPrescriptionShouldBeFound("medicType.specified=true");

        // Get all the prescriptionList where medicType is null
        defaultPrescriptionShouldNotBeFound("medicType.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByMedicColorIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where medicColor equals to DEFAULT_MEDIC_COLOR
        defaultPrescriptionShouldBeFound("medicColor.equals=" + DEFAULT_MEDIC_COLOR);

        // Get all the prescriptionList where medicColor equals to UPDATED_MEDIC_COLOR
        defaultPrescriptionShouldNotBeFound("medicColor.equals=" + UPDATED_MEDIC_COLOR);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByMedicColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where medicColor not equals to DEFAULT_MEDIC_COLOR
        defaultPrescriptionShouldNotBeFound("medicColor.notEquals=" + DEFAULT_MEDIC_COLOR);

        // Get all the prescriptionList where medicColor not equals to UPDATED_MEDIC_COLOR
        defaultPrescriptionShouldBeFound("medicColor.notEquals=" + UPDATED_MEDIC_COLOR);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByMedicColorIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where medicColor in DEFAULT_MEDIC_COLOR or UPDATED_MEDIC_COLOR
        defaultPrescriptionShouldBeFound("medicColor.in=" + DEFAULT_MEDIC_COLOR + "," + UPDATED_MEDIC_COLOR);

        // Get all the prescriptionList where medicColor equals to UPDATED_MEDIC_COLOR
        defaultPrescriptionShouldNotBeFound("medicColor.in=" + UPDATED_MEDIC_COLOR);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByMedicColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where medicColor is not null
        defaultPrescriptionShouldBeFound("medicColor.specified=true");

        // Get all the prescriptionList where medicColor is null
        defaultPrescriptionShouldNotBeFound("medicColor.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where status equals to DEFAULT_STATUS
        defaultPrescriptionShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the prescriptionList where status equals to UPDATED_STATUS
        defaultPrescriptionShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where status not equals to DEFAULT_STATUS
        defaultPrescriptionShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the prescriptionList where status not equals to UPDATED_STATUS
        defaultPrescriptionShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultPrescriptionShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the prescriptionList where status equals to UPDATED_STATUS
        defaultPrescriptionShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPrescriptionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        // Get all the prescriptionList where status is not null
        defaultPrescriptionShouldBeFound("status.specified=true");

        // Get all the prescriptionList where status is null
        defaultPrescriptionShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllPrescriptionsByDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);
        Device device = DeviceResourceIT.createEntity(em);
        em.persist(device);
        em.flush();
        prescription.addDevice(device);
        prescriptionRepository.saveAndFlush(prescription);
        Long deviceId = device.getId();

        // Get all the prescriptionList where device equals to deviceId
        defaultPrescriptionShouldBeFound("deviceId.equals=" + deviceId);

        // Get all the prescriptionList where device equals to (deviceId + 1)
        defaultPrescriptionShouldNotBeFound("deviceId.equals=" + (deviceId + 1));
    }

    @Test
    @Transactional
    void getAllPrescriptionsByTimeTableIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);
        TimeTable timeTable = TimeTableResourceIT.createEntity(em);
        em.persist(timeTable);
        em.flush();
        prescription.addTimeTable(timeTable);
        prescriptionRepository.saveAndFlush(prescription);
        Long timeTableId = timeTable.getId();

        // Get all the prescriptionList where timeTable equals to timeTableId
        defaultPrescriptionShouldBeFound("timeTableId.equals=" + timeTableId);

        // Get all the prescriptionList where timeTable equals to (timeTableId + 1)
        defaultPrescriptionShouldNotBeFound("timeTableId.equals=" + (timeTableId + 1));
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPatientInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);
        PatientInfo patientInfo = PatientInfoResourceIT.createEntity(em);
        em.persist(patientInfo);
        em.flush();
        prescription.setPatientInfo(patientInfo);
        prescriptionRepository.saveAndFlush(prescription);
        Long patientInfoId = patientInfo.getId();

        // Get all the prescriptionList where patientInfo equals to patientInfoId
        defaultPrescriptionShouldBeFound("patientInfoId.equals=" + patientInfoId);

        // Get all the prescriptionList where patientInfo equals to (patientInfoId + 1)
        defaultPrescriptionShouldNotBeFound("patientInfoId.equals=" + (patientInfoId + 1));
    }

    @Test
    @Transactional
    void getAllPrescriptionsByMedicineIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);
        Medicine medicine = MedicineResourceIT.createEntity(em);
        em.persist(medicine);
        em.flush();
        prescription.setMedicine(medicine);
        prescriptionRepository.saveAndFlush(prescription);
        Long medicineId = medicine.getId();

        // Get all the prescriptionList where medicine equals to medicineId
        defaultPrescriptionShouldBeFound("medicineId.equals=" + medicineId);

        // Get all the prescriptionList where medicine equals to (medicineId + 1)
        defaultPrescriptionShouldNotBeFound("medicineId.equals=" + (medicineId + 1));
    }

    @Test
    @Transactional
    void getAllPrescriptionsByPharmacyIsEqualToSomething() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);
        Pharmacy pharmacy = PharmacyResourceIT.createEntity(em);
        em.persist(pharmacy);
        em.flush();
        prescription.setPharmacy(pharmacy);
        prescriptionRepository.saveAndFlush(prescription);
        Long pharmacyId = pharmacy.getId();

        // Get all the prescriptionList where pharmacy equals to pharmacyId
        defaultPrescriptionShouldBeFound("pharmacyId.equals=" + pharmacyId);

        // Get all the prescriptionList where pharmacy equals to (pharmacyId + 1)
        defaultPrescriptionShouldNotBeFound("pharmacyId.equals=" + (pharmacyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPrescriptionShouldBeFound(String filter) throws Exception {
        restPrescriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].prescriptionCode").value(hasItem(DEFAULT_PRESCRIPTION_CODE)))
            .andExpect(jsonPath("$.[*].barCode").value(hasItem(DEFAULT_BAR_CODE)))
            .andExpect(jsonPath("$.[*].issueDate").value(hasItem(DEFAULT_ISSUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].usageDescription").value(hasItem(DEFAULT_USAGE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].importantInfo").value(hasItem(DEFAULT_IMPORTANT_INFO)))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY)))
            .andExpect(jsonPath("$.[*].hasRefill").value(hasItem(DEFAULT_HAS_REFILL.booleanValue())))
            .andExpect(jsonPath("$.[*].refillTime").value(hasItem(DEFAULT_REFILL_TIME.toString())))
            .andExpect(jsonPath("$.[*].prescriptionImageUrl").value(hasItem(DEFAULT_PRESCRIPTION_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].medicType").value(hasItem(DEFAULT_MEDIC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].medicColor").value(hasItem(DEFAULT_MEDIC_COLOR.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restPrescriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPrescriptionShouldNotBeFound(String filter) throws Exception {
        restPrescriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPrescriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPrescription() throws Exception {
        // Get the prescription
        restPrescriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();

        // Update the prescription
        Prescription updatedPrescription = prescriptionRepository.findById(prescription.getId()).get();
        // Disconnect from session so that the updates on updatedPrescription are not directly saved in db
        em.detach(updatedPrescription);
        updatedPrescription
            .prescriptionCode(UPDATED_PRESCRIPTION_CODE)
            .barCode(UPDATED_BAR_CODE)
            .issueDate(UPDATED_ISSUE_DATE)
            .usageDescription(UPDATED_USAGE_DESCRIPTION)
            .importantInfo(UPDATED_IMPORTANT_INFO)
            .qty(UPDATED_QTY)
            .hasRefill(UPDATED_HAS_REFILL)
            .refillTime(UPDATED_REFILL_TIME)
            .prescriptionImageUrl(UPDATED_PRESCRIPTION_IMAGE_URL)
            .medicType(UPDATED_MEDIC_TYPE)
            .medicColor(UPDATED_MEDIC_COLOR)
            .status(UPDATED_STATUS);
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(updatedPrescription);

        restPrescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prescriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getPrescriptionCode()).isEqualTo(UPDATED_PRESCRIPTION_CODE);
        assertThat(testPrescription.getBarCode()).isEqualTo(UPDATED_BAR_CODE);
        assertThat(testPrescription.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testPrescription.getUsageDescription()).isEqualTo(UPDATED_USAGE_DESCRIPTION);
        assertThat(testPrescription.getImportantInfo()).isEqualTo(UPDATED_IMPORTANT_INFO);
        assertThat(testPrescription.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testPrescription.getHasRefill()).isEqualTo(UPDATED_HAS_REFILL);
        assertThat(testPrescription.getRefillTime()).isEqualTo(UPDATED_REFILL_TIME);
        assertThat(testPrescription.getPrescriptionImageUrl()).isEqualTo(UPDATED_PRESCRIPTION_IMAGE_URL);
        assertThat(testPrescription.getMedicType()).isEqualTo(UPDATED_MEDIC_TYPE);
        assertThat(testPrescription.getMedicColor()).isEqualTo(UPDATED_MEDIC_COLOR);
        assertThat(testPrescription.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, prescriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePrescriptionWithPatch() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();

        // Update the prescription using partial update
        Prescription partialUpdatedPrescription = new Prescription();
        partialUpdatedPrescription.setId(prescription.getId());

        partialUpdatedPrescription
            .barCode(UPDATED_BAR_CODE)
            .issueDate(UPDATED_ISSUE_DATE)
            .importantInfo(UPDATED_IMPORTANT_INFO)
            .hasRefill(UPDATED_HAS_REFILL)
            .refillTime(UPDATED_REFILL_TIME)
            .medicType(UPDATED_MEDIC_TYPE)
            .medicColor(UPDATED_MEDIC_COLOR);

        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrescription))
            )
            .andExpect(status().isOk());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getPrescriptionCode()).isEqualTo(DEFAULT_PRESCRIPTION_CODE);
        assertThat(testPrescription.getBarCode()).isEqualTo(UPDATED_BAR_CODE);
        assertThat(testPrescription.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testPrescription.getUsageDescription()).isEqualTo(DEFAULT_USAGE_DESCRIPTION);
        assertThat(testPrescription.getImportantInfo()).isEqualTo(UPDATED_IMPORTANT_INFO);
        assertThat(testPrescription.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testPrescription.getHasRefill()).isEqualTo(UPDATED_HAS_REFILL);
        assertThat(testPrescription.getRefillTime()).isEqualTo(UPDATED_REFILL_TIME);
        assertThat(testPrescription.getPrescriptionImageUrl()).isEqualTo(DEFAULT_PRESCRIPTION_IMAGE_URL);
        assertThat(testPrescription.getMedicType()).isEqualTo(UPDATED_MEDIC_TYPE);
        assertThat(testPrescription.getMedicColor()).isEqualTo(UPDATED_MEDIC_COLOR);
        assertThat(testPrescription.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdatePrescriptionWithPatch() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();

        // Update the prescription using partial update
        Prescription partialUpdatedPrescription = new Prescription();
        partialUpdatedPrescription.setId(prescription.getId());

        partialUpdatedPrescription
            .prescriptionCode(UPDATED_PRESCRIPTION_CODE)
            .barCode(UPDATED_BAR_CODE)
            .issueDate(UPDATED_ISSUE_DATE)
            .usageDescription(UPDATED_USAGE_DESCRIPTION)
            .importantInfo(UPDATED_IMPORTANT_INFO)
            .qty(UPDATED_QTY)
            .hasRefill(UPDATED_HAS_REFILL)
            .refillTime(UPDATED_REFILL_TIME)
            .prescriptionImageUrl(UPDATED_PRESCRIPTION_IMAGE_URL)
            .medicType(UPDATED_MEDIC_TYPE)
            .medicColor(UPDATED_MEDIC_COLOR)
            .status(UPDATED_STATUS);

        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPrescription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPrescription))
            )
            .andExpect(status().isOk());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
        Prescription testPrescription = prescriptionList.get(prescriptionList.size() - 1);
        assertThat(testPrescription.getPrescriptionCode()).isEqualTo(UPDATED_PRESCRIPTION_CODE);
        assertThat(testPrescription.getBarCode()).isEqualTo(UPDATED_BAR_CODE);
        assertThat(testPrescription.getIssueDate()).isEqualTo(UPDATED_ISSUE_DATE);
        assertThat(testPrescription.getUsageDescription()).isEqualTo(UPDATED_USAGE_DESCRIPTION);
        assertThat(testPrescription.getImportantInfo()).isEqualTo(UPDATED_IMPORTANT_INFO);
        assertThat(testPrescription.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testPrescription.getHasRefill()).isEqualTo(UPDATED_HAS_REFILL);
        assertThat(testPrescription.getRefillTime()).isEqualTo(UPDATED_REFILL_TIME);
        assertThat(testPrescription.getPrescriptionImageUrl()).isEqualTo(UPDATED_PRESCRIPTION_IMAGE_URL);
        assertThat(testPrescription.getMedicType()).isEqualTo(UPDATED_MEDIC_TYPE);
        assertThat(testPrescription.getMedicColor()).isEqualTo(UPDATED_MEDIC_COLOR);
        assertThat(testPrescription.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, prescriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPrescription() throws Exception {
        int databaseSizeBeforeUpdate = prescriptionRepository.findAll().size();
        prescription.setId(count.incrementAndGet());

        // Create the Prescription
        PrescriptionDTO prescriptionDTO = prescriptionMapper.toDto(prescription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPrescriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(prescriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Prescription in the database
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePrescription() throws Exception {
        // Initialize the database
        prescriptionRepository.saveAndFlush(prescription);

        int databaseSizeBeforeDelete = prescriptionRepository.findAll().size();

        // Delete the prescription
        restPrescriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, prescription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Prescription> prescriptionList = prescriptionRepository.findAll();
        assertThat(prescriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
