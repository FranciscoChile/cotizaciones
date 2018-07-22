package com.hiab.sales.web.rest;

import com.hiab.sales.CotizacionesApp;

import com.hiab.sales.domain.Sales;
import com.hiab.sales.domain.Client;
import com.hiab.sales.domain.Contact;
import com.hiab.sales.domain.Location;
import com.hiab.sales.domain.Product;
import com.hiab.sales.repository.SalesRepository;
import com.hiab.sales.service.SalesService;
import com.hiab.sales.service.dto.SalesDTO;
import com.hiab.sales.service.mapper.SalesMapper;
import com.hiab.sales.web.rest.errors.ExceptionTranslator;
import com.hiab.sales.service.dto.SalesCriteria;
import com.hiab.sales.service.SalesQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.hiab.sales.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SalesResource REST controller.
 *
 * @see SalesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CotizacionesApp.class)
public class SalesResourceIntTest {

    private static final Integer DEFAULT_FINAL_PRICE = 0;
    private static final Integer UPDATED_FINAL_PRICE = 1;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ACTIVE = 1;
    private static final Integer UPDATED_ACTIVE = 2;

    private static final String DEFAULT_CONDITIONS = "AAAAAAAAAA";
    private static final String UPDATED_CONDITIONS = "BBBBBBBBBB";

    @Autowired
    private SalesRepository salesRepository;

    @Autowired
    private SalesMapper salesMapper;

    @Autowired
    private SalesService salesService;

    @Autowired
    private SalesQueryService salesQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSalesMockMvc;

    private Sales sales;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SalesResource salesResource = new SalesResource(salesService, salesQueryService);
        this.restSalesMockMvc = MockMvcBuilders.standaloneSetup(salesResource)
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
    public static Sales createEntity(EntityManager em) {
        Sales sales = new Sales()
            .finalPrice(DEFAULT_FINAL_PRICE)
            .createDate(DEFAULT_CREATE_DATE)
            .active(DEFAULT_ACTIVE)
            .conditions(DEFAULT_CONDITIONS);
        return sales;
    }

    @Before
    public void initTest() {
        sales = createEntity(em);
    }

    @Test
    @Transactional
    public void createSales() throws Exception {
        int databaseSizeBeforeCreate = salesRepository.findAll().size();

        // Create the Sales
        SalesDTO salesDTO = salesMapper.toDto(sales);
        restSalesMockMvc.perform(post("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesDTO)))
            .andExpect(status().isCreated());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeCreate + 1);
        Sales testSales = salesList.get(salesList.size() - 1);
        assertThat(testSales.getFinalPrice()).isEqualTo(DEFAULT_FINAL_PRICE);
        assertThat(testSales.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testSales.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testSales.getConditions()).isEqualTo(DEFAULT_CONDITIONS);
    }

    @Test
    @Transactional
    public void createSalesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = salesRepository.findAll().size();

        // Create the Sales with an existing ID
        sales.setId(1L);
        SalesDTO salesDTO = salesMapper.toDto(sales);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalesMockMvc.perform(post("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSales() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList
        restSalesMockMvc.perform(get("/api/sales?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sales.getId().intValue())))
            .andExpect(jsonPath("$.[*].finalPrice").value(hasItem(DEFAULT_FINAL_PRICE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS.toString())));
    }

    @Test
    @Transactional
    public void getSales() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get the sales
        restSalesMockMvc.perform(get("/api/sales/{id}", sales.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sales.getId().intValue()))
            .andExpect(jsonPath("$.finalPrice").value(DEFAULT_FINAL_PRICE))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE))
            .andExpect(jsonPath("$.conditions").value(DEFAULT_CONDITIONS.toString()));
    }

    @Test
    @Transactional
    public void getAllSalesByFinalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where finalPrice equals to DEFAULT_FINAL_PRICE
        defaultSalesShouldBeFound("finalPrice.equals=" + DEFAULT_FINAL_PRICE);

        // Get all the salesList where finalPrice equals to UPDATED_FINAL_PRICE
        defaultSalesShouldNotBeFound("finalPrice.equals=" + UPDATED_FINAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllSalesByFinalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where finalPrice in DEFAULT_FINAL_PRICE or UPDATED_FINAL_PRICE
        defaultSalesShouldBeFound("finalPrice.in=" + DEFAULT_FINAL_PRICE + "," + UPDATED_FINAL_PRICE);

        // Get all the salesList where finalPrice equals to UPDATED_FINAL_PRICE
        defaultSalesShouldNotBeFound("finalPrice.in=" + UPDATED_FINAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllSalesByFinalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where finalPrice is not null
        defaultSalesShouldBeFound("finalPrice.specified=true");

        // Get all the salesList where finalPrice is null
        defaultSalesShouldNotBeFound("finalPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalesByFinalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where finalPrice greater than or equals to DEFAULT_FINAL_PRICE
        defaultSalesShouldBeFound("finalPrice.greaterOrEqualThan=" + DEFAULT_FINAL_PRICE);

        // Get all the salesList where finalPrice greater than or equals to UPDATED_FINAL_PRICE
        defaultSalesShouldNotBeFound("finalPrice.greaterOrEqualThan=" + UPDATED_FINAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllSalesByFinalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where finalPrice less than or equals to DEFAULT_FINAL_PRICE
        defaultSalesShouldNotBeFound("finalPrice.lessThan=" + DEFAULT_FINAL_PRICE);

        // Get all the salesList where finalPrice less than or equals to UPDATED_FINAL_PRICE
        defaultSalesShouldBeFound("finalPrice.lessThan=" + UPDATED_FINAL_PRICE);
    }


    @Test
    @Transactional
    public void getAllSalesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where createDate equals to DEFAULT_CREATE_DATE
        defaultSalesShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the salesList where createDate equals to UPDATED_CREATE_DATE
        defaultSalesShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllSalesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultSalesShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the salesList where createDate equals to UPDATED_CREATE_DATE
        defaultSalesShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllSalesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where createDate is not null
        defaultSalesShouldBeFound("createDate.specified=true");

        // Get all the salesList where createDate is null
        defaultSalesShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalesByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where active equals to DEFAULT_ACTIVE
        defaultSalesShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the salesList where active equals to UPDATED_ACTIVE
        defaultSalesShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllSalesByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultSalesShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the salesList where active equals to UPDATED_ACTIVE
        defaultSalesShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllSalesByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where active is not null
        defaultSalesShouldBeFound("active.specified=true");

        // Get all the salesList where active is null
        defaultSalesShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalesByActiveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where active greater than or equals to DEFAULT_ACTIVE
        defaultSalesShouldBeFound("active.greaterOrEqualThan=" + DEFAULT_ACTIVE);

        // Get all the salesList where active greater than or equals to UPDATED_ACTIVE
        defaultSalesShouldNotBeFound("active.greaterOrEqualThan=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllSalesByActiveIsLessThanSomething() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where active less than or equals to DEFAULT_ACTIVE
        defaultSalesShouldNotBeFound("active.lessThan=" + DEFAULT_ACTIVE);

        // Get all the salesList where active less than or equals to UPDATED_ACTIVE
        defaultSalesShouldBeFound("active.lessThan=" + UPDATED_ACTIVE);
    }


    @Test
    @Transactional
    public void getAllSalesByConditionsIsEqualToSomething() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where conditions equals to DEFAULT_CONDITIONS
        defaultSalesShouldBeFound("conditions.equals=" + DEFAULT_CONDITIONS);

        // Get all the salesList where conditions equals to UPDATED_CONDITIONS
        defaultSalesShouldNotBeFound("conditions.equals=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    public void getAllSalesByConditionsIsInShouldWork() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where conditions in DEFAULT_CONDITIONS or UPDATED_CONDITIONS
        defaultSalesShouldBeFound("conditions.in=" + DEFAULT_CONDITIONS + "," + UPDATED_CONDITIONS);

        // Get all the salesList where conditions equals to UPDATED_CONDITIONS
        defaultSalesShouldNotBeFound("conditions.in=" + UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    public void getAllSalesByConditionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);

        // Get all the salesList where conditions is not null
        defaultSalesShouldBeFound("conditions.specified=true");

        // Get all the salesList where conditions is null
        defaultSalesShouldNotBeFound("conditions.specified=false");
    }

    @Test
    @Transactional
    public void getAllSalesByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        sales.setClient(client);
        salesRepository.saveAndFlush(sales);
        Long clientId = client.getId();

        // Get all the salesList where client equals to clientId
        defaultSalesShouldBeFound("clientId.equals=" + clientId);

        // Get all the salesList where client equals to clientId + 1
        defaultSalesShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }


    @Test
    @Transactional
    public void getAllSalesByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        Contact contact = ContactResourceIntTest.createEntity(em);
        em.persist(contact);
        em.flush();
        sales.setContact(contact);
        salesRepository.saveAndFlush(sales);
        Long contactId = contact.getId();

        // Get all the salesList where contact equals to contactId
        defaultSalesShouldBeFound("contactId.equals=" + contactId);

        // Get all the salesList where contact equals to contactId + 1
        defaultSalesShouldNotBeFound("contactId.equals=" + (contactId + 1));
    }


    @Test
    @Transactional
    public void getAllSalesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        Location location = LocationResourceIntTest.createEntity(em);
        em.persist(location);
        em.flush();
        sales.setLocation(location);
        salesRepository.saveAndFlush(sales);
        Long locationId = location.getId();

        // Get all the salesList where location equals to locationId
        defaultSalesShouldBeFound("locationId.equals=" + locationId);

        // Get all the salesList where location equals to locationId + 1
        defaultSalesShouldNotBeFound("locationId.equals=" + (locationId + 1));
    }


    @Test
    @Transactional
    public void getAllSalesByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        Product product = ProductResourceIntTest.createEntity(em);
        em.persist(product);
        em.flush();
        sales.addProduct(product);
        salesRepository.saveAndFlush(sales);
        Long productId = product.getId();

        // Get all the salesList where product equals to productId
        defaultSalesShouldBeFound("productId.equals=" + productId);

        // Get all the salesList where product equals to productId + 1
        defaultSalesShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultSalesShouldBeFound(String filter) throws Exception {
        restSalesMockMvc.perform(get("/api/sales?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sales.getId().intValue())))
            .andExpect(jsonPath("$.[*].finalPrice").value(hasItem(DEFAULT_FINAL_PRICE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultSalesShouldNotBeFound(String filter) throws Exception {
        restSalesMockMvc.perform(get("/api/sales?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSales() throws Exception {
        // Get the sales
        restSalesMockMvc.perform(get("/api/sales/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSales() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);
        int databaseSizeBeforeUpdate = salesRepository.findAll().size();

        // Update the sales
        Sales updatedSales = salesRepository.findOne(sales.getId());
        // Disconnect from session so that the updates on updatedSales are not directly saved in db
        em.detach(updatedSales);
        updatedSales
            .finalPrice(UPDATED_FINAL_PRICE)
            .createDate(UPDATED_CREATE_DATE)
            .active(UPDATED_ACTIVE)
            .conditions(UPDATED_CONDITIONS);
        SalesDTO salesDTO = salesMapper.toDto(updatedSales);

        restSalesMockMvc.perform(put("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesDTO)))
            .andExpect(status().isOk());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeUpdate);
        Sales testSales = salesList.get(salesList.size() - 1);
        assertThat(testSales.getFinalPrice()).isEqualTo(UPDATED_FINAL_PRICE);
        assertThat(testSales.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testSales.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testSales.getConditions()).isEqualTo(UPDATED_CONDITIONS);
    }

    @Test
    @Transactional
    public void updateNonExistingSales() throws Exception {
        int databaseSizeBeforeUpdate = salesRepository.findAll().size();

        // Create the Sales
        SalesDTO salesDTO = salesMapper.toDto(sales);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSalesMockMvc.perform(put("/api/sales")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(salesDTO)))
            .andExpect(status().isCreated());

        // Validate the Sales in the database
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSales() throws Exception {
        // Initialize the database
        salesRepository.saveAndFlush(sales);
        int databaseSizeBeforeDelete = salesRepository.findAll().size();

        // Get the sales
        restSalesMockMvc.perform(delete("/api/sales/{id}", sales.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sales> salesList = salesRepository.findAll();
        assertThat(salesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sales.class);
        Sales sales1 = new Sales();
        sales1.setId(1L);
        Sales sales2 = new Sales();
        sales2.setId(sales1.getId());
        assertThat(sales1).isEqualTo(sales2);
        sales2.setId(2L);
        assertThat(sales1).isNotEqualTo(sales2);
        sales1.setId(null);
        assertThat(sales1).isNotEqualTo(sales2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SalesDTO.class);
        SalesDTO salesDTO1 = new SalesDTO();
        salesDTO1.setId(1L);
        SalesDTO salesDTO2 = new SalesDTO();
        assertThat(salesDTO1).isNotEqualTo(salesDTO2);
        salesDTO2.setId(salesDTO1.getId());
        assertThat(salesDTO1).isEqualTo(salesDTO2);
        salesDTO2.setId(2L);
        assertThat(salesDTO1).isNotEqualTo(salesDTO2);
        salesDTO1.setId(null);
        assertThat(salesDTO1).isNotEqualTo(salesDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(salesMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(salesMapper.fromId(null)).isNull();
    }
}
