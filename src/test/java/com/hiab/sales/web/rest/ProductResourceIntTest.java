package com.hiab.sales.web.rest;

import com.hiab.sales.CotizacionesApp;

import com.hiab.sales.domain.Product;
import com.hiab.sales.domain.Sales;
import com.hiab.sales.repository.ProductRepository;
import com.hiab.sales.service.ProductService;
import com.hiab.sales.service.dto.ProductDTO;
import com.hiab.sales.service.mapper.ProductMapper;
import com.hiab.sales.web.rest.errors.ExceptionTranslator;
import com.hiab.sales.service.dto.ProductCriteria;
import com.hiab.sales.service.ProductQueryService;

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
import org.springframework.util.Base64Utils;

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
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CotizacionesApp.class)
public class ProductResourceIntTest {

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final Long DEFAULT_PRICE_LIST = 0L;
    private static final Long UPDATED_PRICE_LIST = 1L;

    private static final Integer DEFAULT_STOCK = 0;
    private static final Integer UPDATED_STOCK = 1;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE_REF = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_REF = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_REF_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_REF_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_LOAD_DIAGRAM = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOAD_DIAGRAM = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_LOAD_DIAGRAM_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOAD_DIAGRAM_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ACTIVE = 1;
    private static final Integer UPDATED_ACTIVE = 2;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProductMockMvc;

    private Product product;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductResource productResource = new ProductResource(productService, productQueryService);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource)
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
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .model(DEFAULT_MODEL)
            .priceList(DEFAULT_PRICE_LIST)
            .stock(DEFAULT_STOCK)
            .description(DEFAULT_DESCRIPTION)
            .imageRef(DEFAULT_IMAGE_REF)
            .imageRefContentType(DEFAULT_IMAGE_REF_CONTENT_TYPE)
            .loadDiagram(DEFAULT_LOAD_DIAGRAM)
            .loadDiagramContentType(DEFAULT_LOAD_DIAGRAM_CONTENT_TYPE)
            .createDate(DEFAULT_CREATE_DATE)
            .active(DEFAULT_ACTIVE);
        return product;
    }

    @Before
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testProduct.getPriceList()).isEqualTo(DEFAULT_PRICE_LIST);
        assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getImageRef()).isEqualTo(DEFAULT_IMAGE_REF);
        assertThat(testProduct.getImageRefContentType()).isEqualTo(DEFAULT_IMAGE_REF_CONTENT_TYPE);
        assertThat(testProduct.getLoadDiagram()).isEqualTo(DEFAULT_LOAD_DIAGRAM);
        assertThat(testProduct.getLoadDiagramContentType()).isEqualTo(DEFAULT_LOAD_DIAGRAM_CONTENT_TYPE);
        assertThat(testProduct.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testProduct.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].priceList").value(hasItem(DEFAULT_PRICE_LIST.intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageRefContentType").value(hasItem(DEFAULT_IMAGE_REF_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageRef").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_REF))))
            .andExpect(jsonPath("$.[*].loadDiagramContentType").value(hasItem(DEFAULT_LOAD_DIAGRAM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].loadDiagram").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOAD_DIAGRAM))))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL.toString()))
            .andExpect(jsonPath("$.priceList").value(DEFAULT_PRICE_LIST.intValue()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.imageRefContentType").value(DEFAULT_IMAGE_REF_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageRef").value(Base64Utils.encodeToString(DEFAULT_IMAGE_REF)))
            .andExpect(jsonPath("$.loadDiagramContentType").value(DEFAULT_LOAD_DIAGRAM_CONTENT_TYPE))
            .andExpect(jsonPath("$.loadDiagram").value(Base64Utils.encodeToString(DEFAULT_LOAD_DIAGRAM)))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    public void getAllProductsByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where model equals to DEFAULT_MODEL
        defaultProductShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the productList where model equals to UPDATED_MODEL
        defaultProductShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void getAllProductsByModelIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultProductShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the productList where model equals to UPDATED_MODEL
        defaultProductShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    public void getAllProductsByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where model is not null
        defaultProductShouldBeFound("model.specified=true");

        // Get all the productList where model is null
        defaultProductShouldNotBeFound("model.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByPriceListIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where priceList equals to DEFAULT_PRICE_LIST
        defaultProductShouldBeFound("priceList.equals=" + DEFAULT_PRICE_LIST);

        // Get all the productList where priceList equals to UPDATED_PRICE_LIST
        defaultProductShouldNotBeFound("priceList.equals=" + UPDATED_PRICE_LIST);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceListIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where priceList in DEFAULT_PRICE_LIST or UPDATED_PRICE_LIST
        defaultProductShouldBeFound("priceList.in=" + DEFAULT_PRICE_LIST + "," + UPDATED_PRICE_LIST);

        // Get all the productList where priceList equals to UPDATED_PRICE_LIST
        defaultProductShouldNotBeFound("priceList.in=" + UPDATED_PRICE_LIST);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceListIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where priceList is not null
        defaultProductShouldBeFound("priceList.specified=true");

        // Get all the productList where priceList is null
        defaultProductShouldNotBeFound("priceList.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByPriceListIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where priceList greater than or equals to DEFAULT_PRICE_LIST
        defaultProductShouldBeFound("priceList.greaterOrEqualThan=" + DEFAULT_PRICE_LIST);

        // Get all the productList where priceList greater than or equals to UPDATED_PRICE_LIST
        defaultProductShouldNotBeFound("priceList.greaterOrEqualThan=" + UPDATED_PRICE_LIST);
    }

    @Test
    @Transactional
    public void getAllProductsByPriceListIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where priceList less than or equals to DEFAULT_PRICE_LIST
        defaultProductShouldNotBeFound("priceList.lessThan=" + DEFAULT_PRICE_LIST);

        // Get all the productList where priceList less than or equals to UPDATED_PRICE_LIST
        defaultProductShouldBeFound("priceList.lessThan=" + UPDATED_PRICE_LIST);
    }


    @Test
    @Transactional
    public void getAllProductsByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock equals to DEFAULT_STOCK
        defaultProductShouldBeFound("stock.equals=" + DEFAULT_STOCK);

        // Get all the productList where stock equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock in DEFAULT_STOCK or UPDATED_STOCK
        defaultProductShouldBeFound("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK);

        // Get all the productList where stock equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is not null
        defaultProductShouldBeFound("stock.specified=true");

        // Get all the productList where stock is null
        defaultProductShouldNotBeFound("stock.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock greater than or equals to DEFAULT_STOCK
        defaultProductShouldBeFound("stock.greaterOrEqualThan=" + DEFAULT_STOCK);

        // Get all the productList where stock greater than or equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.greaterOrEqualThan=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock less than or equals to DEFAULT_STOCK
        defaultProductShouldNotBeFound("stock.lessThan=" + DEFAULT_STOCK);

        // Get all the productList where stock less than or equals to UPDATED_STOCK
        defaultProductShouldBeFound("stock.lessThan=" + UPDATED_STOCK);
    }


    @Test
    @Transactional
    public void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description equals to DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductShouldBeFound("description.specified=true");

        // Get all the productList where description is null
        defaultProductShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createDate equals to DEFAULT_CREATE_DATE
        defaultProductShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the productList where createDate equals to UPDATED_CREATE_DATE
        defaultProductShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllProductsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultProductShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the productList where createDate equals to UPDATED_CREATE_DATE
        defaultProductShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllProductsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where createDate is not null
        defaultProductShouldBeFound("createDate.specified=true");

        // Get all the productList where createDate is null
        defaultProductShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active equals to DEFAULT_ACTIVE
        defaultProductShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the productList where active equals to UPDATED_ACTIVE
        defaultProductShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultProductShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the productList where active equals to UPDATED_ACTIVE
        defaultProductShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active is not null
        defaultProductShouldBeFound("active.specified=true");

        // Get all the productList where active is null
        defaultProductShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active greater than or equals to DEFAULT_ACTIVE
        defaultProductShouldBeFound("active.greaterOrEqualThan=" + DEFAULT_ACTIVE);

        // Get all the productList where active greater than or equals to UPDATED_ACTIVE
        defaultProductShouldNotBeFound("active.greaterOrEqualThan=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllProductsByActiveIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where active less than or equals to DEFAULT_ACTIVE
        defaultProductShouldNotBeFound("active.lessThan=" + DEFAULT_ACTIVE);

        // Get all the productList where active less than or equals to UPDATED_ACTIVE
        defaultProductShouldBeFound("active.lessThan=" + UPDATED_ACTIVE);
    }


    @Test
    @Transactional
    public void getAllProductsBySalesIsEqualToSomething() throws Exception {
        // Initialize the database
        Sales sales = SalesResourceIntTest.createEntity(em);
        em.persist(sales);
        em.flush();
        product.addSales(sales);
        productRepository.saveAndFlush(product);
        Long salesId = sales.getId();

        // Get all the productList where sales equals to salesId
        defaultProductShouldBeFound("salesId.equals=" + salesId);

        // Get all the productList where sales equals to salesId + 1
        defaultProductShouldNotBeFound("salesId.equals=" + (salesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL.toString())))
            .andExpect(jsonPath("$.[*].priceList").value(hasItem(DEFAULT_PRICE_LIST.intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].imageRefContentType").value(hasItem(DEFAULT_IMAGE_REF_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageRef").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_REF))))
            .andExpect(jsonPath("$.[*].loadDiagramContentType").value(hasItem(DEFAULT_LOAD_DIAGRAM_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].loadDiagram").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOAD_DIAGRAM))))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findOne(product.getId());
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .model(UPDATED_MODEL)
            .priceList(UPDATED_PRICE_LIST)
            .stock(UPDATED_STOCK)
            .description(UPDATED_DESCRIPTION)
            .imageRef(UPDATED_IMAGE_REF)
            .imageRefContentType(UPDATED_IMAGE_REF_CONTENT_TYPE)
            .loadDiagram(UPDATED_LOAD_DIAGRAM)
            .loadDiagramContentType(UPDATED_LOAD_DIAGRAM_CONTENT_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .active(UPDATED_ACTIVE);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testProduct.getPriceList()).isEqualTo(UPDATED_PRICE_LIST);
        assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getImageRef()).isEqualTo(UPDATED_IMAGE_REF);
        assertThat(testProduct.getImageRefContentType()).isEqualTo(UPDATED_IMAGE_REF_CONTENT_TYPE);
        assertThat(testProduct.getLoadDiagram()).isEqualTo(UPDATED_LOAD_DIAGRAM);
        assertThat(testProduct.getLoadDiagramContentType()).isEqualTo(UPDATED_LOAD_DIAGRAM_CONTENT_TYPE);
        assertThat(testProduct.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testProduct.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProductMockMvc.perform(put("/api/products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Get the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);
        product2.setId(2L);
        assertThat(product1).isNotEqualTo(product2);
        product1.setId(null);
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductDTO.class);
        ProductDTO productDTO1 = new ProductDTO();
        productDTO1.setId(1L);
        ProductDTO productDTO2 = new ProductDTO();
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO2.setId(productDTO1.getId());
        assertThat(productDTO1).isEqualTo(productDTO2);
        productDTO2.setId(2L);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
        productDTO1.setId(null);
        assertThat(productDTO1).isNotEqualTo(productDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productMapper.fromId(null)).isNull();
    }
}
