package com.hiab.sales.web.rest;

import com.hiab.sales.CotizacionesApp;

import com.hiab.sales.domain.SaleConditions;
import com.hiab.sales.domain.Sales;
import com.hiab.sales.repository.SaleConditionsRepository;
import com.hiab.sales.service.SaleConditionsService;
import com.hiab.sales.service.dto.SaleConditionsDTO;
import com.hiab.sales.service.mapper.SaleConditionsMapper;
import com.hiab.sales.web.rest.errors.ExceptionTranslator;
import com.hiab.sales.service.dto.SaleConditionsCriteria;
import com.hiab.sales.service.SaleConditionsQueryService;

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
 * Test class for the SaleConditionsResource REST controller.
 *
 * @see SaleConditionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CotizacionesApp.class)
public class SaleConditionsResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private SaleConditionsRepository saleConditionsRepository;

    @Autowired
    private SaleConditionsMapper saleConditionsMapper;

    @Autowired
    private SaleConditionsService saleConditionsService;

    @Autowired
    private SaleConditionsQueryService saleConditionsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSaleConditionsMockMvc;

    private SaleConditions saleConditions;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SaleConditionsResource saleConditionsResource = new SaleConditionsResource(saleConditionsService, saleConditionsQueryService);
        this.restSaleConditionsMockMvc = MockMvcBuilders.standaloneSetup(saleConditionsResource)
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
    public static SaleConditions createEntity(EntityManager em) {
        SaleConditions saleConditions = new SaleConditions()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE);
        return saleConditions;
    }

    @Before
    public void initTest() {
        saleConditions = createEntity(em);
    }

    @Test
    @Transactional
    public void createSaleConditions() throws Exception {
        int databaseSizeBeforeCreate = saleConditionsRepository.findAll().size();

        // Create the SaleConditions
        SaleConditionsDTO saleConditionsDTO = saleConditionsMapper.toDto(saleConditions);
        restSaleConditionsMockMvc.perform(post("/api/sale-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleConditionsDTO)))
            .andExpect(status().isCreated());

        // Validate the SaleConditions in the database
        List<SaleConditions> saleConditionsList = saleConditionsRepository.findAll();
        assertThat(saleConditionsList).hasSize(databaseSizeBeforeCreate + 1);
        SaleConditions testSaleConditions = saleConditionsList.get(saleConditionsList.size() - 1);
        assertThat(testSaleConditions.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testSaleConditions.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createSaleConditionsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = saleConditionsRepository.findAll().size();

        // Create the SaleConditions with an existing ID
        saleConditions.setId(1L);
        SaleConditionsDTO saleConditionsDTO = saleConditionsMapper.toDto(saleConditions);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaleConditionsMockMvc.perform(post("/api/sale-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleConditionsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SaleConditions in the database
        List<SaleConditions> saleConditionsList = saleConditionsRepository.findAll();
        assertThat(saleConditionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSaleConditions() throws Exception {
        // Initialize the database
        saleConditionsRepository.saveAndFlush(saleConditions);

        // Get all the saleConditionsList
        restSaleConditionsMockMvc.perform(get("/api/sale-conditions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleConditions.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getSaleConditions() throws Exception {
        // Initialize the database
        saleConditionsRepository.saveAndFlush(saleConditions);

        // Get the saleConditions
        restSaleConditionsMockMvc.perform(get("/api/sale-conditions/{id}", saleConditions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saleConditions.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        saleConditionsRepository.saveAndFlush(saleConditions);

        // Get all the saleConditionsList where key equals to DEFAULT_KEY
        defaultSaleConditionsShouldBeFound("key.equals=" + DEFAULT_KEY);

        // Get all the saleConditionsList where key equals to UPDATED_KEY
        defaultSaleConditionsShouldNotBeFound("key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        saleConditionsRepository.saveAndFlush(saleConditions);

        // Get all the saleConditionsList where key in DEFAULT_KEY or UPDATED_KEY
        defaultSaleConditionsShouldBeFound("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY);

        // Get all the saleConditionsList where key equals to UPDATED_KEY
        defaultSaleConditionsShouldNotBeFound("key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleConditionsRepository.saveAndFlush(saleConditions);

        // Get all the saleConditionsList where key is not null
        defaultSaleConditionsShouldBeFound("key.specified=true");

        // Get all the saleConditionsList where key is null
        defaultSaleConditionsShouldNotBeFound("key.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        saleConditionsRepository.saveAndFlush(saleConditions);

        // Get all the saleConditionsList where value equals to DEFAULT_VALUE
        defaultSaleConditionsShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the saleConditionsList where value equals to UPDATED_VALUE
        defaultSaleConditionsShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        saleConditionsRepository.saveAndFlush(saleConditions);

        // Get all the saleConditionsList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultSaleConditionsShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the saleConditionsList where value equals to UPDATED_VALUE
        defaultSaleConditionsShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllSaleConditionsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        saleConditionsRepository.saveAndFlush(saleConditions);

        // Get all the saleConditionsList where value is not null
        defaultSaleConditionsShouldBeFound("value.specified=true");

        // Get all the saleConditionsList where value is null
        defaultSaleConditionsShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllSaleConditionsBySalesIsEqualToSomething() throws Exception {
        // Initialize the database
        Sales sales = SalesResourceIntTest.createEntity(em);
        em.persist(sales);
        em.flush();
        saleConditions.addSales(sales);
        saleConditionsRepository.saveAndFlush(saleConditions);
        Long salesId = sales.getId();

        // Get all the saleConditionsList where sales equals to salesId
        defaultSaleConditionsShouldBeFound("salesId.equals=" + salesId);

        // Get all the saleConditionsList where sales equals to salesId + 1
        defaultSaleConditionsShouldNotBeFound("salesId.equals=" + (salesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSaleConditionsShouldBeFound(String filter) throws Exception {
        restSaleConditionsMockMvc.perform(get("/api/sale-conditions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saleConditions.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSaleConditionsShouldNotBeFound(String filter) throws Exception {
        restSaleConditionsMockMvc.perform(get("/api/sale-conditions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSaleConditions() throws Exception {
        // Get the saleConditions
        restSaleConditionsMockMvc.perform(get("/api/sale-conditions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSaleConditions() throws Exception {
        // Initialize the database
        saleConditionsRepository.saveAndFlush(saleConditions);
        int databaseSizeBeforeUpdate = saleConditionsRepository.findAll().size();

        // Update the saleConditions
        SaleConditions updatedSaleConditions = saleConditionsRepository.findOne(saleConditions.getId());
        // Disconnect from session so that the updates on updatedSaleConditions are not directly saved in db
        em.detach(updatedSaleConditions);
        updatedSaleConditions
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);
        SaleConditionsDTO saleConditionsDTO = saleConditionsMapper.toDto(updatedSaleConditions);

        restSaleConditionsMockMvc.perform(put("/api/sale-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleConditionsDTO)))
            .andExpect(status().isOk());

        // Validate the SaleConditions in the database
        List<SaleConditions> saleConditionsList = saleConditionsRepository.findAll();
        assertThat(saleConditionsList).hasSize(databaseSizeBeforeUpdate);
        SaleConditions testSaleConditions = saleConditionsList.get(saleConditionsList.size() - 1);
        assertThat(testSaleConditions.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testSaleConditions.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingSaleConditions() throws Exception {
        int databaseSizeBeforeUpdate = saleConditionsRepository.findAll().size();

        // Create the SaleConditions
        SaleConditionsDTO saleConditionsDTO = saleConditionsMapper.toDto(saleConditions);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSaleConditionsMockMvc.perform(put("/api/sale-conditions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(saleConditionsDTO)))
            .andExpect(status().isCreated());

        // Validate the SaleConditions in the database
        List<SaleConditions> saleConditionsList = saleConditionsRepository.findAll();
        assertThat(saleConditionsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSaleConditions() throws Exception {
        // Initialize the database
        saleConditionsRepository.saveAndFlush(saleConditions);
        int databaseSizeBeforeDelete = saleConditionsRepository.findAll().size();

        // Get the saleConditions
        restSaleConditionsMockMvc.perform(delete("/api/sale-conditions/{id}", saleConditions.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SaleConditions> saleConditionsList = saleConditionsRepository.findAll();
        assertThat(saleConditionsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleConditions.class);
        SaleConditions saleConditions1 = new SaleConditions();
        saleConditions1.setId(1L);
        SaleConditions saleConditions2 = new SaleConditions();
        saleConditions2.setId(saleConditions1.getId());
        assertThat(saleConditions1).isEqualTo(saleConditions2);
        saleConditions2.setId(2L);
        assertThat(saleConditions1).isNotEqualTo(saleConditions2);
        saleConditions1.setId(null);
        assertThat(saleConditions1).isNotEqualTo(saleConditions2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SaleConditionsDTO.class);
        SaleConditionsDTO saleConditionsDTO1 = new SaleConditionsDTO();
        saleConditionsDTO1.setId(1L);
        SaleConditionsDTO saleConditionsDTO2 = new SaleConditionsDTO();
        assertThat(saleConditionsDTO1).isNotEqualTo(saleConditionsDTO2);
        saleConditionsDTO2.setId(saleConditionsDTO1.getId());
        assertThat(saleConditionsDTO1).isEqualTo(saleConditionsDTO2);
        saleConditionsDTO2.setId(2L);
        assertThat(saleConditionsDTO1).isNotEqualTo(saleConditionsDTO2);
        saleConditionsDTO1.setId(null);
        assertThat(saleConditionsDTO1).isNotEqualTo(saleConditionsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(saleConditionsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(saleConditionsMapper.fromId(null)).isNull();
    }
}
