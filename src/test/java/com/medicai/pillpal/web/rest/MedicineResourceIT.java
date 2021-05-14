package com.medicai.pillpal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medicai.pillpal.IntegrationTest;
import com.medicai.pillpal.domain.Medicine;
import com.medicai.pillpal.domain.Prescription;
import com.medicai.pillpal.domain.SideEffect;
import com.medicai.pillpal.domain.enumeration.MedicAppearanceType;
import com.medicai.pillpal.repository.MedicineRepository;
import com.medicai.pillpal.service.criteria.MedicineCriteria;
import com.medicai.pillpal.service.dto.MedicineDTO;
import com.medicai.pillpal.service.mapper.MedicineMapper;
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
 * Integration tests for the {@link MedicineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MedicineResourceIT {

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENERIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GENERIC_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUBSTANCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUBSTANCE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MANUFACTURER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_NDC = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NDC = "BBBBBBBBBB";

    private static final String DEFAULT_PACKAGE_NDC = "AAAAAAAAAA";
    private static final String UPDATED_PACKAGE_NDC = "BBBBBBBBBB";

    private static final String DEFAULT_RX = "AAAAAAAAAA";
    private static final String UPDATED_RX = "BBBBBBBBBB";

    private static final MedicAppearanceType DEFAULT_MEDIC_ROUT = MedicAppearanceType.TABLET;
    private static final MedicAppearanceType UPDATED_MEDIC_ROUT = MedicAppearanceType.CAPSULE;

    private static final String DEFAULT_MEDIC_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_MEDIC_IMAGE_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/medicines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedicineMapper medicineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicineMockMvc;

    private Medicine medicine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicine createEntity(EntityManager em) {
        Medicine medicine = new Medicine()
            .brandName(DEFAULT_BRAND_NAME)
            .genericName(DEFAULT_GENERIC_NAME)
            .substanceName(DEFAULT_SUBSTANCE_NAME)
            .manufacturerName(DEFAULT_MANUFACTURER_NAME)
            .productNDC(DEFAULT_PRODUCT_NDC)
            .packageNDC(DEFAULT_PACKAGE_NDC)
            .rx(DEFAULT_RX)
            .medicRout(DEFAULT_MEDIC_ROUT)
            .medicImageUrl(DEFAULT_MEDIC_IMAGE_URL);
        return medicine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicine createUpdatedEntity(EntityManager em) {
        Medicine medicine = new Medicine()
            .brandName(UPDATED_BRAND_NAME)
            .genericName(UPDATED_GENERIC_NAME)
            .substanceName(UPDATED_SUBSTANCE_NAME)
            .manufacturerName(UPDATED_MANUFACTURER_NAME)
            .productNDC(UPDATED_PRODUCT_NDC)
            .packageNDC(UPDATED_PACKAGE_NDC)
            .rx(UPDATED_RX)
            .medicRout(UPDATED_MEDIC_ROUT)
            .medicImageUrl(UPDATED_MEDIC_IMAGE_URL);
        return medicine;
    }

    @BeforeEach
    public void initTest() {
        medicine = createEntity(em);
    }

    @Test
    @Transactional
    void createMedicine() throws Exception {
        int databaseSizeBeforeCreate = medicineRepository.findAll().size();
        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);
        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isCreated());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeCreate + 1);
        Medicine testMedicine = medicineList.get(medicineList.size() - 1);
        assertThat(testMedicine.getBrandName()).isEqualTo(DEFAULT_BRAND_NAME);
        assertThat(testMedicine.getGenericName()).isEqualTo(DEFAULT_GENERIC_NAME);
        assertThat(testMedicine.getSubstanceName()).isEqualTo(DEFAULT_SUBSTANCE_NAME);
        assertThat(testMedicine.getManufacturerName()).isEqualTo(DEFAULT_MANUFACTURER_NAME);
        assertThat(testMedicine.getProductNDC()).isEqualTo(DEFAULT_PRODUCT_NDC);
        assertThat(testMedicine.getPackageNDC()).isEqualTo(DEFAULT_PACKAGE_NDC);
        assertThat(testMedicine.getRx()).isEqualTo(DEFAULT_RX);
        assertThat(testMedicine.getMedicRout()).isEqualTo(DEFAULT_MEDIC_ROUT);
        assertThat(testMedicine.getMedicImageUrl()).isEqualTo(DEFAULT_MEDIC_IMAGE_URL);
    }

    @Test
    @Transactional
    void createMedicineWithExistingId() throws Exception {
        // Create the Medicine with an existing ID
        medicine.setId(1L);
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        int databaseSizeBeforeCreate = medicineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBrandNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicineRepository.findAll().size();
        // set the field null
        medicine.setBrandName(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenericNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicineRepository.findAll().size();
        // set the field null
        medicine.setGenericName(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubstanceNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicineRepository.findAll().size();
        // set the field null
        medicine.setSubstanceName(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkManufacturerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicineRepository.findAll().size();
        // set the field null
        medicine.setManufacturerName(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProductNDCIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicineRepository.findAll().size();
        // set the field null
        medicine.setProductNDC(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPackageNDCIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicineRepository.findAll().size();
        // set the field null
        medicine.setPackageNDC(null);

        // Create the Medicine, which fails.
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        restMedicineMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isBadRequest());

        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMedicines() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList
        restMedicineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicine.getId().intValue())))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].genericName").value(hasItem(DEFAULT_GENERIC_NAME)))
            .andExpect(jsonPath("$.[*].substanceName").value(hasItem(DEFAULT_SUBSTANCE_NAME)))
            .andExpect(jsonPath("$.[*].manufacturerName").value(hasItem(DEFAULT_MANUFACTURER_NAME)))
            .andExpect(jsonPath("$.[*].productNDC").value(hasItem(DEFAULT_PRODUCT_NDC)))
            .andExpect(jsonPath("$.[*].packageNDC").value(hasItem(DEFAULT_PACKAGE_NDC)))
            .andExpect(jsonPath("$.[*].rx").value(hasItem(DEFAULT_RX)))
            .andExpect(jsonPath("$.[*].medicRout").value(hasItem(DEFAULT_MEDIC_ROUT.toString())))
            .andExpect(jsonPath("$.[*].medicImageUrl").value(hasItem(DEFAULT_MEDIC_IMAGE_URL)));
    }

    @Test
    @Transactional
    void getMedicine() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get the medicine
        restMedicineMockMvc
            .perform(get(ENTITY_API_URL_ID, medicine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicine.getId().intValue()))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME))
            .andExpect(jsonPath("$.genericName").value(DEFAULT_GENERIC_NAME))
            .andExpect(jsonPath("$.substanceName").value(DEFAULT_SUBSTANCE_NAME))
            .andExpect(jsonPath("$.manufacturerName").value(DEFAULT_MANUFACTURER_NAME))
            .andExpect(jsonPath("$.productNDC").value(DEFAULT_PRODUCT_NDC))
            .andExpect(jsonPath("$.packageNDC").value(DEFAULT_PACKAGE_NDC))
            .andExpect(jsonPath("$.rx").value(DEFAULT_RX))
            .andExpect(jsonPath("$.medicRout").value(DEFAULT_MEDIC_ROUT.toString()))
            .andExpect(jsonPath("$.medicImageUrl").value(DEFAULT_MEDIC_IMAGE_URL));
    }

    @Test
    @Transactional
    void getMedicinesByIdFiltering() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        Long id = medicine.getId();

        defaultMedicineShouldBeFound("id.equals=" + id);
        defaultMedicineShouldNotBeFound("id.notEquals=" + id);

        defaultMedicineShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMedicineShouldNotBeFound("id.greaterThan=" + id);

        defaultMedicineShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMedicineShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMedicinesByBrandNameIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where brandName equals to DEFAULT_BRAND_NAME
        defaultMedicineShouldBeFound("brandName.equals=" + DEFAULT_BRAND_NAME);

        // Get all the medicineList where brandName equals to UPDATED_BRAND_NAME
        defaultMedicineShouldNotBeFound("brandName.equals=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByBrandNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where brandName not equals to DEFAULT_BRAND_NAME
        defaultMedicineShouldNotBeFound("brandName.notEquals=" + DEFAULT_BRAND_NAME);

        // Get all the medicineList where brandName not equals to UPDATED_BRAND_NAME
        defaultMedicineShouldBeFound("brandName.notEquals=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByBrandNameIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where brandName in DEFAULT_BRAND_NAME or UPDATED_BRAND_NAME
        defaultMedicineShouldBeFound("brandName.in=" + DEFAULT_BRAND_NAME + "," + UPDATED_BRAND_NAME);

        // Get all the medicineList where brandName equals to UPDATED_BRAND_NAME
        defaultMedicineShouldNotBeFound("brandName.in=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByBrandNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where brandName is not null
        defaultMedicineShouldBeFound("brandName.specified=true");

        // Get all the medicineList where brandName is null
        defaultMedicineShouldNotBeFound("brandName.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicinesByBrandNameContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where brandName contains DEFAULT_BRAND_NAME
        defaultMedicineShouldBeFound("brandName.contains=" + DEFAULT_BRAND_NAME);

        // Get all the medicineList where brandName contains UPDATED_BRAND_NAME
        defaultMedicineShouldNotBeFound("brandName.contains=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByBrandNameNotContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where brandName does not contain DEFAULT_BRAND_NAME
        defaultMedicineShouldNotBeFound("brandName.doesNotContain=" + DEFAULT_BRAND_NAME);

        // Get all the medicineList where brandName does not contain UPDATED_BRAND_NAME
        defaultMedicineShouldBeFound("brandName.doesNotContain=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByGenericNameIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where genericName equals to DEFAULT_GENERIC_NAME
        defaultMedicineShouldBeFound("genericName.equals=" + DEFAULT_GENERIC_NAME);

        // Get all the medicineList where genericName equals to UPDATED_GENERIC_NAME
        defaultMedicineShouldNotBeFound("genericName.equals=" + UPDATED_GENERIC_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByGenericNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where genericName not equals to DEFAULT_GENERIC_NAME
        defaultMedicineShouldNotBeFound("genericName.notEquals=" + DEFAULT_GENERIC_NAME);

        // Get all the medicineList where genericName not equals to UPDATED_GENERIC_NAME
        defaultMedicineShouldBeFound("genericName.notEquals=" + UPDATED_GENERIC_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByGenericNameIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where genericName in DEFAULT_GENERIC_NAME or UPDATED_GENERIC_NAME
        defaultMedicineShouldBeFound("genericName.in=" + DEFAULT_GENERIC_NAME + "," + UPDATED_GENERIC_NAME);

        // Get all the medicineList where genericName equals to UPDATED_GENERIC_NAME
        defaultMedicineShouldNotBeFound("genericName.in=" + UPDATED_GENERIC_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByGenericNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where genericName is not null
        defaultMedicineShouldBeFound("genericName.specified=true");

        // Get all the medicineList where genericName is null
        defaultMedicineShouldNotBeFound("genericName.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicinesByGenericNameContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where genericName contains DEFAULT_GENERIC_NAME
        defaultMedicineShouldBeFound("genericName.contains=" + DEFAULT_GENERIC_NAME);

        // Get all the medicineList where genericName contains UPDATED_GENERIC_NAME
        defaultMedicineShouldNotBeFound("genericName.contains=" + UPDATED_GENERIC_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByGenericNameNotContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where genericName does not contain DEFAULT_GENERIC_NAME
        defaultMedicineShouldNotBeFound("genericName.doesNotContain=" + DEFAULT_GENERIC_NAME);

        // Get all the medicineList where genericName does not contain UPDATED_GENERIC_NAME
        defaultMedicineShouldBeFound("genericName.doesNotContain=" + UPDATED_GENERIC_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesBySubstanceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where substanceName equals to DEFAULT_SUBSTANCE_NAME
        defaultMedicineShouldBeFound("substanceName.equals=" + DEFAULT_SUBSTANCE_NAME);

        // Get all the medicineList where substanceName equals to UPDATED_SUBSTANCE_NAME
        defaultMedicineShouldNotBeFound("substanceName.equals=" + UPDATED_SUBSTANCE_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesBySubstanceNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where substanceName not equals to DEFAULT_SUBSTANCE_NAME
        defaultMedicineShouldNotBeFound("substanceName.notEquals=" + DEFAULT_SUBSTANCE_NAME);

        // Get all the medicineList where substanceName not equals to UPDATED_SUBSTANCE_NAME
        defaultMedicineShouldBeFound("substanceName.notEquals=" + UPDATED_SUBSTANCE_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesBySubstanceNameIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where substanceName in DEFAULT_SUBSTANCE_NAME or UPDATED_SUBSTANCE_NAME
        defaultMedicineShouldBeFound("substanceName.in=" + DEFAULT_SUBSTANCE_NAME + "," + UPDATED_SUBSTANCE_NAME);

        // Get all the medicineList where substanceName equals to UPDATED_SUBSTANCE_NAME
        defaultMedicineShouldNotBeFound("substanceName.in=" + UPDATED_SUBSTANCE_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesBySubstanceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where substanceName is not null
        defaultMedicineShouldBeFound("substanceName.specified=true");

        // Get all the medicineList where substanceName is null
        defaultMedicineShouldNotBeFound("substanceName.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicinesBySubstanceNameContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where substanceName contains DEFAULT_SUBSTANCE_NAME
        defaultMedicineShouldBeFound("substanceName.contains=" + DEFAULT_SUBSTANCE_NAME);

        // Get all the medicineList where substanceName contains UPDATED_SUBSTANCE_NAME
        defaultMedicineShouldNotBeFound("substanceName.contains=" + UPDATED_SUBSTANCE_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesBySubstanceNameNotContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where substanceName does not contain DEFAULT_SUBSTANCE_NAME
        defaultMedicineShouldNotBeFound("substanceName.doesNotContain=" + DEFAULT_SUBSTANCE_NAME);

        // Get all the medicineList where substanceName does not contain UPDATED_SUBSTANCE_NAME
        defaultMedicineShouldBeFound("substanceName.doesNotContain=" + UPDATED_SUBSTANCE_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByManufacturerNameIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where manufacturerName equals to DEFAULT_MANUFACTURER_NAME
        defaultMedicineShouldBeFound("manufacturerName.equals=" + DEFAULT_MANUFACTURER_NAME);

        // Get all the medicineList where manufacturerName equals to UPDATED_MANUFACTURER_NAME
        defaultMedicineShouldNotBeFound("manufacturerName.equals=" + UPDATED_MANUFACTURER_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByManufacturerNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where manufacturerName not equals to DEFAULT_MANUFACTURER_NAME
        defaultMedicineShouldNotBeFound("manufacturerName.notEquals=" + DEFAULT_MANUFACTURER_NAME);

        // Get all the medicineList where manufacturerName not equals to UPDATED_MANUFACTURER_NAME
        defaultMedicineShouldBeFound("manufacturerName.notEquals=" + UPDATED_MANUFACTURER_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByManufacturerNameIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where manufacturerName in DEFAULT_MANUFACTURER_NAME or UPDATED_MANUFACTURER_NAME
        defaultMedicineShouldBeFound("manufacturerName.in=" + DEFAULT_MANUFACTURER_NAME + "," + UPDATED_MANUFACTURER_NAME);

        // Get all the medicineList where manufacturerName equals to UPDATED_MANUFACTURER_NAME
        defaultMedicineShouldNotBeFound("manufacturerName.in=" + UPDATED_MANUFACTURER_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByManufacturerNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where manufacturerName is not null
        defaultMedicineShouldBeFound("manufacturerName.specified=true");

        // Get all the medicineList where manufacturerName is null
        defaultMedicineShouldNotBeFound("manufacturerName.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicinesByManufacturerNameContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where manufacturerName contains DEFAULT_MANUFACTURER_NAME
        defaultMedicineShouldBeFound("manufacturerName.contains=" + DEFAULT_MANUFACTURER_NAME);

        // Get all the medicineList where manufacturerName contains UPDATED_MANUFACTURER_NAME
        defaultMedicineShouldNotBeFound("manufacturerName.contains=" + UPDATED_MANUFACTURER_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByManufacturerNameNotContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where manufacturerName does not contain DEFAULT_MANUFACTURER_NAME
        defaultMedicineShouldNotBeFound("manufacturerName.doesNotContain=" + DEFAULT_MANUFACTURER_NAME);

        // Get all the medicineList where manufacturerName does not contain UPDATED_MANUFACTURER_NAME
        defaultMedicineShouldBeFound("manufacturerName.doesNotContain=" + UPDATED_MANUFACTURER_NAME);
    }

    @Test
    @Transactional
    void getAllMedicinesByProductNDCIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where productNDC equals to DEFAULT_PRODUCT_NDC
        defaultMedicineShouldBeFound("productNDC.equals=" + DEFAULT_PRODUCT_NDC);

        // Get all the medicineList where productNDC equals to UPDATED_PRODUCT_NDC
        defaultMedicineShouldNotBeFound("productNDC.equals=" + UPDATED_PRODUCT_NDC);
    }

    @Test
    @Transactional
    void getAllMedicinesByProductNDCIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where productNDC not equals to DEFAULT_PRODUCT_NDC
        defaultMedicineShouldNotBeFound("productNDC.notEquals=" + DEFAULT_PRODUCT_NDC);

        // Get all the medicineList where productNDC not equals to UPDATED_PRODUCT_NDC
        defaultMedicineShouldBeFound("productNDC.notEquals=" + UPDATED_PRODUCT_NDC);
    }

    @Test
    @Transactional
    void getAllMedicinesByProductNDCIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where productNDC in DEFAULT_PRODUCT_NDC or UPDATED_PRODUCT_NDC
        defaultMedicineShouldBeFound("productNDC.in=" + DEFAULT_PRODUCT_NDC + "," + UPDATED_PRODUCT_NDC);

        // Get all the medicineList where productNDC equals to UPDATED_PRODUCT_NDC
        defaultMedicineShouldNotBeFound("productNDC.in=" + UPDATED_PRODUCT_NDC);
    }

    @Test
    @Transactional
    void getAllMedicinesByProductNDCIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where productNDC is not null
        defaultMedicineShouldBeFound("productNDC.specified=true");

        // Get all the medicineList where productNDC is null
        defaultMedicineShouldNotBeFound("productNDC.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicinesByProductNDCContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where productNDC contains DEFAULT_PRODUCT_NDC
        defaultMedicineShouldBeFound("productNDC.contains=" + DEFAULT_PRODUCT_NDC);

        // Get all the medicineList where productNDC contains UPDATED_PRODUCT_NDC
        defaultMedicineShouldNotBeFound("productNDC.contains=" + UPDATED_PRODUCT_NDC);
    }

    @Test
    @Transactional
    void getAllMedicinesByProductNDCNotContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where productNDC does not contain DEFAULT_PRODUCT_NDC
        defaultMedicineShouldNotBeFound("productNDC.doesNotContain=" + DEFAULT_PRODUCT_NDC);

        // Get all the medicineList where productNDC does not contain UPDATED_PRODUCT_NDC
        defaultMedicineShouldBeFound("productNDC.doesNotContain=" + UPDATED_PRODUCT_NDC);
    }

    @Test
    @Transactional
    void getAllMedicinesByPackageNDCIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where packageNDC equals to DEFAULT_PACKAGE_NDC
        defaultMedicineShouldBeFound("packageNDC.equals=" + DEFAULT_PACKAGE_NDC);

        // Get all the medicineList where packageNDC equals to UPDATED_PACKAGE_NDC
        defaultMedicineShouldNotBeFound("packageNDC.equals=" + UPDATED_PACKAGE_NDC);
    }

    @Test
    @Transactional
    void getAllMedicinesByPackageNDCIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where packageNDC not equals to DEFAULT_PACKAGE_NDC
        defaultMedicineShouldNotBeFound("packageNDC.notEquals=" + DEFAULT_PACKAGE_NDC);

        // Get all the medicineList where packageNDC not equals to UPDATED_PACKAGE_NDC
        defaultMedicineShouldBeFound("packageNDC.notEquals=" + UPDATED_PACKAGE_NDC);
    }

    @Test
    @Transactional
    void getAllMedicinesByPackageNDCIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where packageNDC in DEFAULT_PACKAGE_NDC or UPDATED_PACKAGE_NDC
        defaultMedicineShouldBeFound("packageNDC.in=" + DEFAULT_PACKAGE_NDC + "," + UPDATED_PACKAGE_NDC);

        // Get all the medicineList where packageNDC equals to UPDATED_PACKAGE_NDC
        defaultMedicineShouldNotBeFound("packageNDC.in=" + UPDATED_PACKAGE_NDC);
    }

    @Test
    @Transactional
    void getAllMedicinesByPackageNDCIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where packageNDC is not null
        defaultMedicineShouldBeFound("packageNDC.specified=true");

        // Get all the medicineList where packageNDC is null
        defaultMedicineShouldNotBeFound("packageNDC.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicinesByPackageNDCContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where packageNDC contains DEFAULT_PACKAGE_NDC
        defaultMedicineShouldBeFound("packageNDC.contains=" + DEFAULT_PACKAGE_NDC);

        // Get all the medicineList where packageNDC contains UPDATED_PACKAGE_NDC
        defaultMedicineShouldNotBeFound("packageNDC.contains=" + UPDATED_PACKAGE_NDC);
    }

    @Test
    @Transactional
    void getAllMedicinesByPackageNDCNotContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where packageNDC does not contain DEFAULT_PACKAGE_NDC
        defaultMedicineShouldNotBeFound("packageNDC.doesNotContain=" + DEFAULT_PACKAGE_NDC);

        // Get all the medicineList where packageNDC does not contain UPDATED_PACKAGE_NDC
        defaultMedicineShouldBeFound("packageNDC.doesNotContain=" + UPDATED_PACKAGE_NDC);
    }

    @Test
    @Transactional
    void getAllMedicinesByRxIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where rx equals to DEFAULT_RX
        defaultMedicineShouldBeFound("rx.equals=" + DEFAULT_RX);

        // Get all the medicineList where rx equals to UPDATED_RX
        defaultMedicineShouldNotBeFound("rx.equals=" + UPDATED_RX);
    }

    @Test
    @Transactional
    void getAllMedicinesByRxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where rx not equals to DEFAULT_RX
        defaultMedicineShouldNotBeFound("rx.notEquals=" + DEFAULT_RX);

        // Get all the medicineList where rx not equals to UPDATED_RX
        defaultMedicineShouldBeFound("rx.notEquals=" + UPDATED_RX);
    }

    @Test
    @Transactional
    void getAllMedicinesByRxIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where rx in DEFAULT_RX or UPDATED_RX
        defaultMedicineShouldBeFound("rx.in=" + DEFAULT_RX + "," + UPDATED_RX);

        // Get all the medicineList where rx equals to UPDATED_RX
        defaultMedicineShouldNotBeFound("rx.in=" + UPDATED_RX);
    }

    @Test
    @Transactional
    void getAllMedicinesByRxIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where rx is not null
        defaultMedicineShouldBeFound("rx.specified=true");

        // Get all the medicineList where rx is null
        defaultMedicineShouldNotBeFound("rx.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicinesByRxContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where rx contains DEFAULT_RX
        defaultMedicineShouldBeFound("rx.contains=" + DEFAULT_RX);

        // Get all the medicineList where rx contains UPDATED_RX
        defaultMedicineShouldNotBeFound("rx.contains=" + UPDATED_RX);
    }

    @Test
    @Transactional
    void getAllMedicinesByRxNotContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where rx does not contain DEFAULT_RX
        defaultMedicineShouldNotBeFound("rx.doesNotContain=" + DEFAULT_RX);

        // Get all the medicineList where rx does not contain UPDATED_RX
        defaultMedicineShouldBeFound("rx.doesNotContain=" + UPDATED_RX);
    }

    @Test
    @Transactional
    void getAllMedicinesByMedicRoutIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where medicRout equals to DEFAULT_MEDIC_ROUT
        defaultMedicineShouldBeFound("medicRout.equals=" + DEFAULT_MEDIC_ROUT);

        // Get all the medicineList where medicRout equals to UPDATED_MEDIC_ROUT
        defaultMedicineShouldNotBeFound("medicRout.equals=" + UPDATED_MEDIC_ROUT);
    }

    @Test
    @Transactional
    void getAllMedicinesByMedicRoutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where medicRout not equals to DEFAULT_MEDIC_ROUT
        defaultMedicineShouldNotBeFound("medicRout.notEquals=" + DEFAULT_MEDIC_ROUT);

        // Get all the medicineList where medicRout not equals to UPDATED_MEDIC_ROUT
        defaultMedicineShouldBeFound("medicRout.notEquals=" + UPDATED_MEDIC_ROUT);
    }

    @Test
    @Transactional
    void getAllMedicinesByMedicRoutIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where medicRout in DEFAULT_MEDIC_ROUT or UPDATED_MEDIC_ROUT
        defaultMedicineShouldBeFound("medicRout.in=" + DEFAULT_MEDIC_ROUT + "," + UPDATED_MEDIC_ROUT);

        // Get all the medicineList where medicRout equals to UPDATED_MEDIC_ROUT
        defaultMedicineShouldNotBeFound("medicRout.in=" + UPDATED_MEDIC_ROUT);
    }

    @Test
    @Transactional
    void getAllMedicinesByMedicRoutIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where medicRout is not null
        defaultMedicineShouldBeFound("medicRout.specified=true");

        // Get all the medicineList where medicRout is null
        defaultMedicineShouldNotBeFound("medicRout.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicinesByMedicImageUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where medicImageUrl equals to DEFAULT_MEDIC_IMAGE_URL
        defaultMedicineShouldBeFound("medicImageUrl.equals=" + DEFAULT_MEDIC_IMAGE_URL);

        // Get all the medicineList where medicImageUrl equals to UPDATED_MEDIC_IMAGE_URL
        defaultMedicineShouldNotBeFound("medicImageUrl.equals=" + UPDATED_MEDIC_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllMedicinesByMedicImageUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where medicImageUrl not equals to DEFAULT_MEDIC_IMAGE_URL
        defaultMedicineShouldNotBeFound("medicImageUrl.notEquals=" + DEFAULT_MEDIC_IMAGE_URL);

        // Get all the medicineList where medicImageUrl not equals to UPDATED_MEDIC_IMAGE_URL
        defaultMedicineShouldBeFound("medicImageUrl.notEquals=" + UPDATED_MEDIC_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllMedicinesByMedicImageUrlIsInShouldWork() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where medicImageUrl in DEFAULT_MEDIC_IMAGE_URL or UPDATED_MEDIC_IMAGE_URL
        defaultMedicineShouldBeFound("medicImageUrl.in=" + DEFAULT_MEDIC_IMAGE_URL + "," + UPDATED_MEDIC_IMAGE_URL);

        // Get all the medicineList where medicImageUrl equals to UPDATED_MEDIC_IMAGE_URL
        defaultMedicineShouldNotBeFound("medicImageUrl.in=" + UPDATED_MEDIC_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllMedicinesByMedicImageUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where medicImageUrl is not null
        defaultMedicineShouldBeFound("medicImageUrl.specified=true");

        // Get all the medicineList where medicImageUrl is null
        defaultMedicineShouldNotBeFound("medicImageUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllMedicinesByMedicImageUrlContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where medicImageUrl contains DEFAULT_MEDIC_IMAGE_URL
        defaultMedicineShouldBeFound("medicImageUrl.contains=" + DEFAULT_MEDIC_IMAGE_URL);

        // Get all the medicineList where medicImageUrl contains UPDATED_MEDIC_IMAGE_URL
        defaultMedicineShouldNotBeFound("medicImageUrl.contains=" + UPDATED_MEDIC_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllMedicinesByMedicImageUrlNotContainsSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        // Get all the medicineList where medicImageUrl does not contain DEFAULT_MEDIC_IMAGE_URL
        defaultMedicineShouldNotBeFound("medicImageUrl.doesNotContain=" + DEFAULT_MEDIC_IMAGE_URL);

        // Get all the medicineList where medicImageUrl does not contain UPDATED_MEDIC_IMAGE_URL
        defaultMedicineShouldBeFound("medicImageUrl.doesNotContain=" + UPDATED_MEDIC_IMAGE_URL);
    }

    @Test
    @Transactional
    void getAllMedicinesBySideEffectIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);
        SideEffect sideEffect = SideEffectResourceIT.createEntity(em);
        em.persist(sideEffect);
        em.flush();
        medicine.addSideEffect(sideEffect);
        medicineRepository.saveAndFlush(medicine);
        Long sideEffectId = sideEffect.getId();

        // Get all the medicineList where sideEffect equals to sideEffectId
        defaultMedicineShouldBeFound("sideEffectId.equals=" + sideEffectId);

        // Get all the medicineList where sideEffect equals to (sideEffectId + 1)
        defaultMedicineShouldNotBeFound("sideEffectId.equals=" + (sideEffectId + 1));
    }

    @Test
    @Transactional
    void getAllMedicinesByPrescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);
        Prescription prescription = PrescriptionResourceIT.createEntity(em);
        em.persist(prescription);
        em.flush();
        medicine.addPrescription(prescription);
        medicineRepository.saveAndFlush(medicine);
        Long prescriptionId = prescription.getId();

        // Get all the medicineList where prescription equals to prescriptionId
        defaultMedicineShouldBeFound("prescriptionId.equals=" + prescriptionId);

        // Get all the medicineList where prescription equals to (prescriptionId + 1)
        defaultMedicineShouldNotBeFound("prescriptionId.equals=" + (prescriptionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicineShouldBeFound(String filter) throws Exception {
        restMedicineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicine.getId().intValue())))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].genericName").value(hasItem(DEFAULT_GENERIC_NAME)))
            .andExpect(jsonPath("$.[*].substanceName").value(hasItem(DEFAULT_SUBSTANCE_NAME)))
            .andExpect(jsonPath("$.[*].manufacturerName").value(hasItem(DEFAULT_MANUFACTURER_NAME)))
            .andExpect(jsonPath("$.[*].productNDC").value(hasItem(DEFAULT_PRODUCT_NDC)))
            .andExpect(jsonPath("$.[*].packageNDC").value(hasItem(DEFAULT_PACKAGE_NDC)))
            .andExpect(jsonPath("$.[*].rx").value(hasItem(DEFAULT_RX)))
            .andExpect(jsonPath("$.[*].medicRout").value(hasItem(DEFAULT_MEDIC_ROUT.toString())))
            .andExpect(jsonPath("$.[*].medicImageUrl").value(hasItem(DEFAULT_MEDIC_IMAGE_URL)));

        // Check, that the count call also returns 1
        restMedicineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicineShouldNotBeFound(String filter) throws Exception {
        restMedicineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicineMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedicine() throws Exception {
        // Get the medicine
        restMedicineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMedicine() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();

        // Update the medicine
        Medicine updatedMedicine = medicineRepository.findById(medicine.getId()).get();
        // Disconnect from session so that the updates on updatedMedicine are not directly saved in db
        em.detach(updatedMedicine);
        updatedMedicine
            .brandName(UPDATED_BRAND_NAME)
            .genericName(UPDATED_GENERIC_NAME)
            .substanceName(UPDATED_SUBSTANCE_NAME)
            .manufacturerName(UPDATED_MANUFACTURER_NAME)
            .productNDC(UPDATED_PRODUCT_NDC)
            .packageNDC(UPDATED_PACKAGE_NDC)
            .rx(UPDATED_RX)
            .medicRout(UPDATED_MEDIC_ROUT)
            .medicImageUrl(UPDATED_MEDIC_IMAGE_URL);
        MedicineDTO medicineDTO = medicineMapper.toDto(updatedMedicine);

        restMedicineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicineDTO))
            )
            .andExpect(status().isOk());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);
        Medicine testMedicine = medicineList.get(medicineList.size() - 1);
        assertThat(testMedicine.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testMedicine.getGenericName()).isEqualTo(UPDATED_GENERIC_NAME);
        assertThat(testMedicine.getSubstanceName()).isEqualTo(UPDATED_SUBSTANCE_NAME);
        assertThat(testMedicine.getManufacturerName()).isEqualTo(UPDATED_MANUFACTURER_NAME);
        assertThat(testMedicine.getProductNDC()).isEqualTo(UPDATED_PRODUCT_NDC);
        assertThat(testMedicine.getPackageNDC()).isEqualTo(UPDATED_PACKAGE_NDC);
        assertThat(testMedicine.getRx()).isEqualTo(UPDATED_RX);
        assertThat(testMedicine.getMedicRout()).isEqualTo(UPDATED_MEDIC_ROUT);
        assertThat(testMedicine.getMedicImageUrl()).isEqualTo(UPDATED_MEDIC_IMAGE_URL);
    }

    @Test
    @Transactional
    void putNonExistingMedicine() throws Exception {
        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();
        medicine.setId(count.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, medicineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedicine() throws Exception {
        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();
        medicine.setId(count.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(medicineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedicine() throws Exception {
        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();
        medicine.setId(count.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(medicineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMedicineWithPatch() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();

        // Update the medicine using partial update
        Medicine partialUpdatedMedicine = new Medicine();
        partialUpdatedMedicine.setId(medicine.getId());

        partialUpdatedMedicine
            .brandName(UPDATED_BRAND_NAME)
            .genericName(UPDATED_GENERIC_NAME)
            .substanceName(UPDATED_SUBSTANCE_NAME)
            .packageNDC(UPDATED_PACKAGE_NDC)
            .rx(UPDATED_RX)
            .medicRout(UPDATED_MEDIC_ROUT);

        restMedicineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicine))
            )
            .andExpect(status().isOk());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);
        Medicine testMedicine = medicineList.get(medicineList.size() - 1);
        assertThat(testMedicine.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testMedicine.getGenericName()).isEqualTo(UPDATED_GENERIC_NAME);
        assertThat(testMedicine.getSubstanceName()).isEqualTo(UPDATED_SUBSTANCE_NAME);
        assertThat(testMedicine.getManufacturerName()).isEqualTo(DEFAULT_MANUFACTURER_NAME);
        assertThat(testMedicine.getProductNDC()).isEqualTo(DEFAULT_PRODUCT_NDC);
        assertThat(testMedicine.getPackageNDC()).isEqualTo(UPDATED_PACKAGE_NDC);
        assertThat(testMedicine.getRx()).isEqualTo(UPDATED_RX);
        assertThat(testMedicine.getMedicRout()).isEqualTo(UPDATED_MEDIC_ROUT);
        assertThat(testMedicine.getMedicImageUrl()).isEqualTo(DEFAULT_MEDIC_IMAGE_URL);
    }

    @Test
    @Transactional
    void fullUpdateMedicineWithPatch() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();

        // Update the medicine using partial update
        Medicine partialUpdatedMedicine = new Medicine();
        partialUpdatedMedicine.setId(medicine.getId());

        partialUpdatedMedicine
            .brandName(UPDATED_BRAND_NAME)
            .genericName(UPDATED_GENERIC_NAME)
            .substanceName(UPDATED_SUBSTANCE_NAME)
            .manufacturerName(UPDATED_MANUFACTURER_NAME)
            .productNDC(UPDATED_PRODUCT_NDC)
            .packageNDC(UPDATED_PACKAGE_NDC)
            .rx(UPDATED_RX)
            .medicRout(UPDATED_MEDIC_ROUT)
            .medicImageUrl(UPDATED_MEDIC_IMAGE_URL);

        restMedicineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedicine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedicine))
            )
            .andExpect(status().isOk());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);
        Medicine testMedicine = medicineList.get(medicineList.size() - 1);
        assertThat(testMedicine.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testMedicine.getGenericName()).isEqualTo(UPDATED_GENERIC_NAME);
        assertThat(testMedicine.getSubstanceName()).isEqualTo(UPDATED_SUBSTANCE_NAME);
        assertThat(testMedicine.getManufacturerName()).isEqualTo(UPDATED_MANUFACTURER_NAME);
        assertThat(testMedicine.getProductNDC()).isEqualTo(UPDATED_PRODUCT_NDC);
        assertThat(testMedicine.getPackageNDC()).isEqualTo(UPDATED_PACKAGE_NDC);
        assertThat(testMedicine.getRx()).isEqualTo(UPDATED_RX);
        assertThat(testMedicine.getMedicRout()).isEqualTo(UPDATED_MEDIC_ROUT);
        assertThat(testMedicine.getMedicImageUrl()).isEqualTo(UPDATED_MEDIC_IMAGE_URL);
    }

    @Test
    @Transactional
    void patchNonExistingMedicine() throws Exception {
        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();
        medicine.setId(count.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, medicineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedicine() throws Exception {
        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();
        medicine.setId(count.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(medicineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedicine() throws Exception {
        int databaseSizeBeforeUpdate = medicineRepository.findAll().size();
        medicine.setId(count.incrementAndGet());

        // Create the Medicine
        MedicineDTO medicineDTO = medicineMapper.toDto(medicine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMedicineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(medicineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Medicine in the database
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedicine() throws Exception {
        // Initialize the database
        medicineRepository.saveAndFlush(medicine);

        int databaseSizeBeforeDelete = medicineRepository.findAll().size();

        // Delete the medicine
        restMedicineMockMvc
            .perform(delete(ENTITY_API_URL_ID, medicine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medicine> medicineList = medicineRepository.findAll();
        assertThat(medicineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
