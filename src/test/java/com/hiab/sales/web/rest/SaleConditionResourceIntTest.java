package com.hiab.sales.web.rest;

import com.hiab.sales.CotizacionesApp;

import com.hiab.sales.domain.SaleCondition;
import com.hiab.sales.domain.Sales;
import com.hiab.sales.repository.SaleConditionRepository;
import com.hiab.sales.service.SaleConditionService;
import com.hiab.sales.service.dto.SaleConditionDTO;
import com.hiab.sales.service.mapper.SaleConditionMapper;
import com.hiab.sales.web.rest.errors.ExceptionTranslator;
import com.hiab.sales.service.dto.SaleConditionCriteria;
import com.hiab.sales.service.SaleConditionQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.hiab.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SaleConditionResource REST controller.
 *
 * @see SaleConditionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CotizacionesApp.class)
public class SaleConditionResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private SaleConditionRepository saleConditionRepository;

    @Autowired
    private SaleConditionMapper saleConditionMapper;

    @Autowired
    private SaleConditionService saleConditionService;

    @Autowired
    private SaleConditionQueryService saleConditionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSaleConditionMockMvc;

    private SaleCondition saleCondition;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SaleConditionResource saleConditionResource = new SaleConditionResource(saleConditionService, saleConditionQueryService);
        this.restSaleConditionMockMvc = MockMvcBuilders.standaloneSetup(saleConditionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaleCondition createEntity(EntityManager em) {
        SaleCondition saleCondition = new SaleCondition()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE);
        return saleCondition;
    }

    @Before
    public void initTest() {
        saleCondition = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaleCondition() throws Exception {
        int databaseSizeBeforeCreate = saleConditionRepository.findAll().size();

        // Create the SaleCondition
        SaleConditionDTO saleConditionDTO = saleConditionMapper.toDto(saleCondition);
        restSaleConditionMockMvc.perform(post("/api/sale-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleConditionDTO)))
            .andExpect(status().isCreated());

        // Validate the SaleCondition in the database
        List<SaleCondition> saleConditionList = saleConditionRepository.findAll();
        assertThat(saleConditionList).hasSize(databaseSizeBeforeCreate + 1);
        SaleCondition testSaleCondition = saleConditionList.get(saleConditionList.size() - 1);
        assertThat(testSaleCondition.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testSaleCondition.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createSaleConditionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saleConditionRepository.findAll().size();

        // Create the SaleCondition with an existing ID
        saleCondition.setId(1L);
        SaleConditionDTO saleConditionDTO = saleConditionMapper.toDto(saleCondition);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleConditionMockMvc.perform(post("/api/sale-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleConditionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SaleCondition in the database
        List<SaleCondition> saleConditionList = saleConditionRepository.findAll();
        assertThat(saleConditionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSaleConditions() throws Exception {
        // Initialize the database
        saleConditionRepository.saveAndFlush(saleCondition);

        // Get all the saleConditionList
        restSaleConditionMockMvc.perform(get("/api/sale-conditions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleCondition.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getSaleCondition() throws Exception {
        // Initialize the database
        saleConditionRepository.saveAndFlush(saleCondition);

        // Get the saleCondition
        restSaleConditionMockMvc.perform(get("/api/sale-conditions/{id}", saleCondition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saleCondition.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        saleConditionRepository.saveAndFlush(saleCondition);

        // Get all the saleConditionList where key equals to DEFAULT_KEY
        defaultSaleConditionShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the saleConditionList where key equals to UPDATED_KEY
        defaultSaleConditionShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        saleConditionRepository.saveAndFlush(saleCondition);

        // Get all the saleConditionList where key in DEFAULT_KEY or UPDATED_KEY
        defaultSaleConditionShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the saleConditionList where key equals to UPDATED_KEY
        defaultSaleConditionShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleConditionRepository.saveAndFlush(saleCondition);

        // Get all the saleConditionList where key is not null
        defaultSaleConditionShouldBeFound("key.specified=true");

        // Get all the saleConditionList where key is null
        defaultSaleConditionShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        saleConditionRepository.saveAndFlush(saleCondition);

        // Get all the saleConditionList where value equals to DEFAULT_VALUE
        defaultSaleConditionShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the saleConditionList where value equals to UPDATED_VALUE
        defaultSaleConditionShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        saleConditionRepository.saveAndFlush(saleCondition);

        // Get all the saleConditionList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultSaleConditionShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the saleConditionList where value equals to UPDATED_VALUE
        defaultSaleConditionShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleConditionRepository.saveAndFlush(saleCondition);

        // Get all the saleConditionList where value is not null
        defaultSaleConditionShouldBeFound("value.specified=true");

        // Get all the saleConditionList where value is null
        defaultSaleConditionShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleConditionsBySalesIsEqualToSomething() throws Exception {
        // Initialize the database
        Sales sales = SalesResourceIntTest.createEntity(em);
        em.persist(sales);
        em.flush();
        saleCondition.setSales(sales);
        saleConditionRepository.saveAndFlush(saleCondition);
        Long salesId = sales.getId();

        // Get all the saleConditionList where sales equals to salesId
        defaultSaleConditionShouldBeFound("salesId.equals=" + salesId);

        // Get all the saleConditionList where sales equals to salesId + 1
        defaultSaleConditionShouldNotBeFound("salesId.equals=" + (salesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSaleConditionShouldBeFound(String filter) throws Exception {
        restSaleConditionMockMvc.perform(get("/api/sale-conditions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleCondition.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSaleConditionShouldNotBeFound(String filter) throws Exception {
        restSaleConditionMockMvc.perform(get("/api/sale-conditions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSaleCondition() throws Exception {
        // Get the saleCondition
        restSaleConditionMockMvc.perform(get("/api/sale-conditions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaleCondition() throws Exception {
        // Initialize the database
        saleConditionRepository.saveAndFlush(saleCondition);
        int databaseSizeBeforeUpdate = saleConditionRepository.findAll().size();

        // Update the saleCondition
        SaleCondition updatedSaleCondition = saleConditionRepository.findOne(saleCondition.getId());
        // Disconnect from session so that the updates on updatedSaleCondition are not directly saved in db
        em.detach(updatedSaleCondition);
        updatedSaleCondition
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);
        SaleConditionDTO saleConditionDTO = saleConditionMapper.toDto(updatedSaleCondition);

        restSaleConditionMockMvc.perform(put("/api/sale-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleConditionDTO)))
            .andExpect(status().isOk());

        // Validate the SaleCondition in the database
        List<SaleCondition> saleConditionList = saleConditionRepository.findAll();
        assertThat(saleConditionList).hasSize(databaseSizeBeforeUpdate);
        SaleCondition testSaleCondition = saleConditionList.get(saleConditionList.size() - 1);
        assertThat(testSaleCondition.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSaleCondition.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingSaleCondition() throws Exception {
        int databaseSizeBeforeUpdate = saleConditionRepository.findAll().size();

        // Create the SaleCondition
        SaleConditionDTO saleConditionDTO = saleConditionMapper.toDto(saleCondition);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSaleConditionMockMvc.perform(put("/api/sale-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleConditionDTO)))
            .andExpect(status().isCreated());

        // Validate the SaleCondition in the database
        List<SaleCondition> saleConditionList = saleConditionRepository.findAll();
        assertThat(saleConditionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSaleCondition() throws Exception {
        // Initialize the database
        saleConditionRepository.saveAndFlush(saleCondition);
        int databaseSizeBeforeDelete = saleConditionRepository.findAll().size();

        // Get the saleCondition
        restSaleConditionMockMvc.perform(delete("/api/sale-conditions/{id}", saleCondition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SaleCondition> saleConditionList = saleConditionRepository.findAll();
        assertThat(saleConditionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleCondition.class);
        SaleCondition saleCondition1 = new SaleCondition();
        saleCondition1.setId(1L);
        SaleCondition saleCondition2 = new SaleCondition();
        saleCondition2.setId(saleCondition1.getId());
        assertThat(saleCondition1).isEqualTo(saleCondition2);
        saleCondition2.setId(2L);
        assertThat(saleCondition1).isNotEqualTo(saleCondition2);
        saleCondition1.setId(null);
        assertThat(saleCondition1).isNotEqualTo(saleCondition2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleConditionDTO.class);
        SaleConditionDTO saleConditionDTO1 = new SaleConditionDTO();
        saleConditionDTO1.setId(1L);
        SaleConditionDTO saleConditionDTO2 = new SaleConditionDTO();
        assertThat(saleConditionDTO1).isNotEqualTo(saleConditionDTO2);
        saleConditionDTO2.setId(saleConditionDTO1.getId());
        assertThat(saleConditionDTO1).isEqualTo(saleConditionDTO2);
        saleConditionDTO2.setId(2L);
        assertThat(saleConditionDTO1).isNotEqualTo(saleConditionDTO2);
        saleConditionDTO1.setId(null);
        assertThat(saleConditionDTO1).isNotEqualTo(saleConditionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(saleConditionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(saleConditionMapper.fromId(null)).isNull();
    }
}
