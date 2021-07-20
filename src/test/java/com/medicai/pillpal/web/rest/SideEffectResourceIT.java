package com.medicai.pillpal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.medicai.pillpal.IntegrationTest;
import com.medicai.pillpal.domain.SideEffect;
import com.medicai.pillpal.repository.SideEffectRepository;
import com.medicai.pillpal.service.dto.SideEffectDTO;
import com.medicai.pillpal.service.mapper.SideEffectMapper;
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
 * Integration tests for the {@link SideEffectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SideEffectResourceIT {

    private static final String DEFAULT_SIDE_EFFECT = "AAAAAAAAAA";
    private static final String UPDATED_SIDE_EFFECT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/side-effects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SideEffectRepository sideEffectRepository;

    @Autowired
    private SideEffectMapper sideEffectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSideEffectMockMvc;

    private SideEffect sideEffect;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SideEffect createEntity(EntityManager em) {
        SideEffect sideEffect = new SideEffect().sideEffect(DEFAULT_SIDE_EFFECT);
        return sideEffect;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SideEffect createUpdatedEntity(EntityManager em) {
        SideEffect sideEffect = new SideEffect().sideEffect(UPDATED_SIDE_EFFECT);
        return sideEffect;
    }

    @BeforeEach
    public void initTest() {
        sideEffect = createEntity(em);
    }

    @Test
    @Transactional
    void createSideEffect() throws Exception {
        int databaseSizeBeforeCreate = sideEffectRepository.findAll().size();
        // Create the SideEffect
        SideEffectDTO sideEffectDTO = sideEffectMapper.toDto(sideEffect);
        restSideEffectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sideEffectDTO)))
            .andExpect(status().isCreated());

        // Validate the SideEffect in the database
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeCreate + 1);
        SideEffect testSideEffect = sideEffectList.get(sideEffectList.size() - 1);
        assertThat(testSideEffect.getSideEffect()).isEqualTo(DEFAULT_SIDE_EFFECT);
    }

    @Test
    @Transactional
    void createSideEffectWithExistingId() throws Exception {
        // Create the SideEffect with an existing ID
        sideEffect.setId(1L);
        SideEffectDTO sideEffectDTO = sideEffectMapper.toDto(sideEffect);

        int databaseSizeBeforeCreate = sideEffectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSideEffectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sideEffectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SideEffect in the database
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSideEffectIsRequired() throws Exception {
        int databaseSizeBeforeTest = sideEffectRepository.findAll().size();
        // set the field null
        sideEffect.setSideEffect(null);

        // Create the SideEffect, which fails.
        SideEffectDTO sideEffectDTO = sideEffectMapper.toDto(sideEffect);

        restSideEffectMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sideEffectDTO)))
            .andExpect(status().isBadRequest());

        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSideEffects() throws Exception {
        // Initialize the database
        sideEffectRepository.saveAndFlush(sideEffect);

        // Get all the sideEffectList
        restSideEffectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sideEffect.getId().intValue())))
            .andExpect(jsonPath("$.[*].sideEffect").value(hasItem(DEFAULT_SIDE_EFFECT)));
    }

    @Test
    @Transactional
    void getSideEffect() throws Exception {
        // Initialize the database
        sideEffectRepository.saveAndFlush(sideEffect);

        // Get the sideEffect
        restSideEffectMockMvc
            .perform(get(ENTITY_API_URL_ID, sideEffect.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sideEffect.getId().intValue()))
            .andExpect(jsonPath("$.sideEffect").value(DEFAULT_SIDE_EFFECT));
    }

    @Test
    @Transactional
    void getNonExistingSideEffect() throws Exception {
        // Get the sideEffect
        restSideEffectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSideEffect() throws Exception {
        // Initialize the database
        sideEffectRepository.saveAndFlush(sideEffect);

        int databaseSizeBeforeUpdate = sideEffectRepository.findAll().size();

        // Update the sideEffect
        SideEffect updatedSideEffect = sideEffectRepository.findById(sideEffect.getId()).get();
        // Disconnect from session so that the updates on updatedSideEffect are not directly saved in db
        em.detach(updatedSideEffect);
        updatedSideEffect.sideEffect(UPDATED_SIDE_EFFECT);
        SideEffectDTO sideEffectDTO = sideEffectMapper.toDto(updatedSideEffect);

        restSideEffectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sideEffectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sideEffectDTO))
            )
            .andExpect(status().isOk());

        // Validate the SideEffect in the database
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeUpdate);
        SideEffect testSideEffect = sideEffectList.get(sideEffectList.size() - 1);
        assertThat(testSideEffect.getSideEffect()).isEqualTo(UPDATED_SIDE_EFFECT);
    }

    @Test
    @Transactional
    void putNonExistingSideEffect() throws Exception {
        int databaseSizeBeforeUpdate = sideEffectRepository.findAll().size();
        sideEffect.setId(count.incrementAndGet());

        // Create the SideEffect
        SideEffectDTO sideEffectDTO = sideEffectMapper.toDto(sideEffect);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSideEffectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sideEffectDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sideEffectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SideEffect in the database
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSideEffect() throws Exception {
        int databaseSizeBeforeUpdate = sideEffectRepository.findAll().size();
        sideEffect.setId(count.incrementAndGet());

        // Create the SideEffect
        SideEffectDTO sideEffectDTO = sideEffectMapper.toDto(sideEffect);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSideEffectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sideEffectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SideEffect in the database
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSideEffect() throws Exception {
        int databaseSizeBeforeUpdate = sideEffectRepository.findAll().size();
        sideEffect.setId(count.incrementAndGet());

        // Create the SideEffect
        SideEffectDTO sideEffectDTO = sideEffectMapper.toDto(sideEffect);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSideEffectMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sideEffectDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SideEffect in the database
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSideEffectWithPatch() throws Exception {
        // Initialize the database
        sideEffectRepository.saveAndFlush(sideEffect);

        int databaseSizeBeforeUpdate = sideEffectRepository.findAll().size();

        // Update the sideEffect using partial update
        SideEffect partialUpdatedSideEffect = new SideEffect();
        partialUpdatedSideEffect.setId(sideEffect.getId());

        partialUpdatedSideEffect.sideEffect(UPDATED_SIDE_EFFECT);

        restSideEffectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSideEffect.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSideEffect))
            )
            .andExpect(status().isOk());

        // Validate the SideEffect in the database
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeUpdate);
        SideEffect testSideEffect = sideEffectList.get(sideEffectList.size() - 1);
        assertThat(testSideEffect.getSideEffect()).isEqualTo(UPDATED_SIDE_EFFECT);
    }

    @Test
    @Transactional
    void fullUpdateSideEffectWithPatch() throws Exception {
        // Initialize the database
        sideEffectRepository.saveAndFlush(sideEffect);

        int databaseSizeBeforeUpdate = sideEffectRepository.findAll().size();

        // Update the sideEffect using partial update
        SideEffect partialUpdatedSideEffect = new SideEffect();
        partialUpdatedSideEffect.setId(sideEffect.getId());

        partialUpdatedSideEffect.sideEffect(UPDATED_SIDE_EFFECT);

        restSideEffectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSideEffect.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSideEffect))
            )
            .andExpect(status().isOk());

        // Validate the SideEffect in the database
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeUpdate);
        SideEffect testSideEffect = sideEffectList.get(sideEffectList.size() - 1);
        assertThat(testSideEffect.getSideEffect()).isEqualTo(UPDATED_SIDE_EFFECT);
    }

    @Test
    @Transactional
    void patchNonExistingSideEffect() throws Exception {
        int databaseSizeBeforeUpdate = sideEffectRepository.findAll().size();
        sideEffect.setId(count.incrementAndGet());

        // Create the SideEffect
        SideEffectDTO sideEffectDTO = sideEffectMapper.toDto(sideEffect);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSideEffectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sideEffectDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sideEffectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SideEffect in the database
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSideEffect() throws Exception {
        int databaseSizeBeforeUpdate = sideEffectRepository.findAll().size();
        sideEffect.setId(count.incrementAndGet());

        // Create the SideEffect
        SideEffectDTO sideEffectDTO = sideEffectMapper.toDto(sideEffect);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSideEffectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sideEffectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SideEffect in the database
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSideEffect() throws Exception {
        int databaseSizeBeforeUpdate = sideEffectRepository.findAll().size();
        sideEffect.setId(count.incrementAndGet());

        // Create the SideEffect
        SideEffectDTO sideEffectDTO = sideEffectMapper.toDto(sideEffect);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSideEffectMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sideEffectDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SideEffect in the database
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSideEffect() throws Exception {
        // Initialize the database
        sideEffectRepository.saveAndFlush(sideEffect);

        int databaseSizeBeforeDelete = sideEffectRepository.findAll().size();

        // Delete the sideEffect
        restSideEffectMockMvc
            .perform(delete(ENTITY_API_URL_ID, sideEffect.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SideEffect> sideEffectList = sideEffectRepository.findAll();
        assertThat(sideEffectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
