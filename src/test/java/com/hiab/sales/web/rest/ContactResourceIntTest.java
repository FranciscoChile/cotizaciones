package com.hiab.sales.web.rest;

import com.hiab.sales.CotizacionesApp;

import com.hiab.sales.domain.Contact;
import com.hiab.sales.domain.Client;
import com.hiab.sales.domain.Sales;
import com.hiab.sales.repository.ContactRepository;
import com.hiab.sales.service.ContactService;
import com.hiab.sales.service.dto.ContactDTO;
import com.hiab.sales.service.mapper.ContactMapper;
import com.hiab.sales.web.rest.errors.ExceptionTranslator;
import com.hiab.sales.service.dto.ContactCriteria;
import com.hiab.sales.service.ContactQueryService;

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
 * Test class for the ContactResource REST controller.
 *
 * @see ContactResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CotizacionesApp.class)
public class ContactResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_AREA = "AAAAAAAAAA";
    private static final String UPDATED_AREA = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CELLPHONE = "AAAAAAAAAA";
    private static final String UPDATED_CELLPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_LINE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_LINE_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ACTIVE = 1;
    private static final Integer UPDATED_ACTIVE = 2;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactMapper contactMapper;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactQueryService contactQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContactMockMvc;

    private Contact contact;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContactResource contactResource = new ContactResource(contactService, contactQueryService);
        this.restContactMockMvc = MockMvcBuilders.standaloneSetup(contactResource)
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
    public static Contact createEntity(EntityManager em) {
        Contact contact = new Contact()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .position(DEFAULT_POSITION)
            .area(DEFAULT_AREA)
            .address(DEFAULT_ADDRESS)
            .cellphone(DEFAULT_CELLPHONE)
            .linePhone(DEFAULT_LINE_PHONE)
            .email(DEFAULT_EMAIL)
            .comments(DEFAULT_COMMENTS)
            .createDate(DEFAULT_CREATE_DATE)
            .active(DEFAULT_ACTIVE);
        return contact;
    }

    @Before
    public void initTest() {
        contact = createEntity(em);
    }

    @Test
    @Transactional
    public void createContact() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);
        restContactMockMvc.perform(post("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isCreated());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate + 1);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContact.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testContact.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testContact.getArea()).isEqualTo(DEFAULT_AREA);
        assertThat(testContact.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testContact.getCellphone()).isEqualTo(DEFAULT_CELLPHONE);
        assertThat(testContact.getLinePhone()).isEqualTo(DEFAULT_LINE_PHONE);
        assertThat(testContact.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContact.getComments()).isEqualTo(DEFAULT_COMMENTS);
        assertThat(testContact.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testContact.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createContactWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contactRepository.findAll().size();

        // Create the Contact with an existing ID
        contact.setId(1L);
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactMockMvc.perform(post("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContacts() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].cellphone").value(hasItem(DEFAULT_CELLPHONE.toString())))
            .andExpect(jsonPath("$.[*].linePhone").value(hasItem(DEFAULT_LINE_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    @Test
    @Transactional
    public void getContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get the contact
        restContactMockMvc.perform(get("/api/contacts/{id}", contact.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contact.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()))
            .andExpect(jsonPath("$.area").value(DEFAULT_AREA.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.cellphone").value(DEFAULT_CELLPHONE.toString()))
            .andExpect(jsonPath("$.linePhone").value(DEFAULT_LINE_PHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS.toString()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE));
    }

    @Test
    @Transactional
    public void getAllContactsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name equals to DEFAULT_NAME
        defaultContactShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the contactList where name equals to UPDATED_NAME
        defaultContactShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name in DEFAULT_NAME or UPDATED_NAME
        defaultContactShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the contactList where name equals to UPDATED_NAME
        defaultContactShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllContactsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where name is not null
        defaultContactShouldBeFound("name.specified=true");

        // Get all the contactList where name is null
        defaultContactShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsBySurnameIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where surname equals to DEFAULT_SURNAME
        defaultContactShouldBeFound("surname.equals=" + DEFAULT_SURNAME);

        // Get all the contactList where surname equals to UPDATED_SURNAME
        defaultContactShouldNotBeFound("surname.equals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllContactsBySurnameIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where surname in DEFAULT_SURNAME or UPDATED_SURNAME
        defaultContactShouldBeFound("surname.in=" + DEFAULT_SURNAME + "," + UPDATED_SURNAME);

        // Get all the contactList where surname equals to UPDATED_SURNAME
        defaultContactShouldNotBeFound("surname.in=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllContactsBySurnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where surname is not null
        defaultContactShouldBeFound("surname.specified=true");

        // Get all the contactList where surname is null
        defaultContactShouldNotBeFound("surname.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where position equals to DEFAULT_POSITION
        defaultContactShouldBeFound("position.equals=" + DEFAULT_POSITION);

        // Get all the contactList where position equals to UPDATED_POSITION
        defaultContactShouldNotBeFound("position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllContactsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where position in DEFAULT_POSITION or UPDATED_POSITION
        defaultContactShouldBeFound("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION);

        // Get all the contactList where position equals to UPDATED_POSITION
        defaultContactShouldNotBeFound("position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    public void getAllContactsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where position is not null
        defaultContactShouldBeFound("position.specified=true");

        // Get all the contactList where position is null
        defaultContactShouldNotBeFound("position.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByAreaIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where area equals to DEFAULT_AREA
        defaultContactShouldBeFound("area.equals=" + DEFAULT_AREA);

        // Get all the contactList where area equals to UPDATED_AREA
        defaultContactShouldNotBeFound("area.equals=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllContactsByAreaIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where area in DEFAULT_AREA or UPDATED_AREA
        defaultContactShouldBeFound("area.in=" + DEFAULT_AREA + "," + UPDATED_AREA);

        // Get all the contactList where area equals to UPDATED_AREA
        defaultContactShouldNotBeFound("area.in=" + UPDATED_AREA);
    }

    @Test
    @Transactional
    public void getAllContactsByAreaIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where area is not null
        defaultContactShouldBeFound("area.specified=true");

        // Get all the contactList where area is null
        defaultContactShouldNotBeFound("area.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where address equals to DEFAULT_ADDRESS
        defaultContactShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the contactList where address equals to UPDATED_ADDRESS
        defaultContactShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllContactsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultContactShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the contactList where address equals to UPDATED_ADDRESS
        defaultContactShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllContactsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where address is not null
        defaultContactShouldBeFound("address.specified=true");

        // Get all the contactList where address is null
        defaultContactShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByCellphoneIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where cellphone equals to DEFAULT_CELLPHONE
        defaultContactShouldBeFound("cellphone.equals=" + DEFAULT_CELLPHONE);

        // Get all the contactList where cellphone equals to UPDATED_CELLPHONE
        defaultContactShouldNotBeFound("cellphone.equals=" + UPDATED_CELLPHONE);
    }

    @Test
    @Transactional
    public void getAllContactsByCellphoneIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where cellphone in DEFAULT_CELLPHONE or UPDATED_CELLPHONE
        defaultContactShouldBeFound("cellphone.in=" + DEFAULT_CELLPHONE + "," + UPDATED_CELLPHONE);

        // Get all the contactList where cellphone equals to UPDATED_CELLPHONE
        defaultContactShouldNotBeFound("cellphone.in=" + UPDATED_CELLPHONE);
    }

    @Test
    @Transactional
    public void getAllContactsByCellphoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where cellphone is not null
        defaultContactShouldBeFound("cellphone.specified=true");

        // Get all the contactList where cellphone is null
        defaultContactShouldNotBeFound("cellphone.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByLinePhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where linePhone equals to DEFAULT_LINE_PHONE
        defaultContactShouldBeFound("linePhone.equals=" + DEFAULT_LINE_PHONE);

        // Get all the contactList where linePhone equals to UPDATED_LINE_PHONE
        defaultContactShouldNotBeFound("linePhone.equals=" + UPDATED_LINE_PHONE);
    }

    @Test
    @Transactional
    public void getAllContactsByLinePhoneIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where linePhone in DEFAULT_LINE_PHONE or UPDATED_LINE_PHONE
        defaultContactShouldBeFound("linePhone.in=" + DEFAULT_LINE_PHONE + "," + UPDATED_LINE_PHONE);

        // Get all the contactList where linePhone equals to UPDATED_LINE_PHONE
        defaultContactShouldNotBeFound("linePhone.in=" + UPDATED_LINE_PHONE);
    }

    @Test
    @Transactional
    public void getAllContactsByLinePhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where linePhone is not null
        defaultContactShouldBeFound("linePhone.specified=true");

        // Get all the contactList where linePhone is null
        defaultContactShouldNotBeFound("linePhone.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email equals to DEFAULT_EMAIL
        defaultContactShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the contactList where email equals to UPDATED_EMAIL
        defaultContactShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllContactsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultContactShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the contactList where email equals to UPDATED_EMAIL
        defaultContactShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllContactsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where email is not null
        defaultContactShouldBeFound("email.specified=true");

        // Get all the contactList where email is null
        defaultContactShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where comments equals to DEFAULT_COMMENTS
        defaultContactShouldBeFound("comments.equals=" + DEFAULT_COMMENTS);

        // Get all the contactList where comments equals to UPDATED_COMMENTS
        defaultContactShouldNotBeFound("comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllContactsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where comments in DEFAULT_COMMENTS or UPDATED_COMMENTS
        defaultContactShouldBeFound("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS);

        // Get all the contactList where comments equals to UPDATED_COMMENTS
        defaultContactShouldNotBeFound("comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllContactsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where comments is not null
        defaultContactShouldBeFound("comments.specified=true");

        // Get all the contactList where comments is null
        defaultContactShouldNotBeFound("comments.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where createDate equals to DEFAULT_CREATE_DATE
        defaultContactShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the contactList where createDate equals to UPDATED_CREATE_DATE
        defaultContactShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllContactsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultContactShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the contactList where createDate equals to UPDATED_CREATE_DATE
        defaultContactShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllContactsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where createDate is not null
        defaultContactShouldBeFound("createDate.specified=true");

        // Get all the contactList where createDate is null
        defaultContactShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where active equals to DEFAULT_ACTIVE
        defaultContactShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the contactList where active equals to UPDATED_ACTIVE
        defaultContactShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllContactsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultContactShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the contactList where active equals to UPDATED_ACTIVE
        defaultContactShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllContactsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where active is not null
        defaultContactShouldBeFound("active.specified=true");

        // Get all the contactList where active is null
        defaultContactShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllContactsByActiveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where active greater than or equals to DEFAULT_ACTIVE
        defaultContactShouldBeFound("active.greaterOrEqualThan=" + DEFAULT_ACTIVE);

        // Get all the contactList where active greater than or equals to UPDATED_ACTIVE
        defaultContactShouldNotBeFound("active.greaterOrEqualThan=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllContactsByActiveIsLessThanSomething() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);

        // Get all the contactList where active less than or equals to DEFAULT_ACTIVE
        defaultContactShouldNotBeFound("active.lessThan=" + DEFAULT_ACTIVE);

        // Get all the contactList where active less than or equals to UPDATED_ACTIVE
        defaultContactShouldBeFound("active.lessThan=" + UPDATED_ACTIVE);
    }


    @Test
    @Transactional
    public void getAllContactsByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        Client client = ClientResourceIntTest.createEntity(em);
        em.persist(client);
        em.flush();
        contact.setClient(client);
        contactRepository.saveAndFlush(contact);
        Long clientId = client.getId();

        // Get all the contactList where client equals to clientId
        defaultContactShouldBeFound("clientId.equals=" + clientId);

        // Get all the contactList where client equals to clientId + 1
        defaultContactShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }


    @Test
    @Transactional
    public void getAllContactsBySalesIsEqualToSomething() throws Exception {
        // Initialize the database
        Sales sales = SalesResourceIntTest.createEntity(em);
        em.persist(sales);
        em.flush();
        contact.addSales(sales);
        contactRepository.saveAndFlush(contact);
        Long salesId = sales.getId();

        // Get all the contactList where sales equals to salesId
        defaultContactShouldBeFound("salesId.equals=" + salesId);

        // Get all the contactList where sales equals to salesId + 1
        defaultContactShouldNotBeFound("salesId.equals=" + (salesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultContactShouldBeFound(String filter) throws Exception {
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contact.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].area").value(hasItem(DEFAULT_AREA.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].cellphone").value(hasItem(DEFAULT_CELLPHONE.toString())))
            .andExpect(jsonPath("$.[*].linePhone").value(hasItem(DEFAULT_LINE_PHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultContactShouldNotBeFound(String filter) throws Exception {
        restContactMockMvc.perform(get("/api/contacts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingContact() throws Exception {
        // Get the contact
        restContactMockMvc.perform(get("/api/contacts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Update the contact
        Contact updatedContact = contactRepository.findOne(contact.getId());
        // Disconnect from session so that the updates on updatedContact are not directly saved in db
        em.detach(updatedContact);
        updatedContact
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .position(UPDATED_POSITION)
            .area(UPDATED_AREA)
            .address(UPDATED_ADDRESS)
            .cellphone(UPDATED_CELLPHONE)
            .linePhone(UPDATED_LINE_PHONE)
            .email(UPDATED_EMAIL)
            .comments(UPDATED_COMMENTS)
            .createDate(UPDATED_CREATE_DATE)
            .active(UPDATED_ACTIVE);
        ContactDTO contactDTO = contactMapper.toDto(updatedContact);

        restContactMockMvc.perform(put("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isOk());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate);
        Contact testContact = contactList.get(contactList.size() - 1);
        assertThat(testContact.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContact.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testContact.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testContact.getArea()).isEqualTo(UPDATED_AREA);
        assertThat(testContact.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testContact.getCellphone()).isEqualTo(UPDATED_CELLPHONE);
        assertThat(testContact.getLinePhone()).isEqualTo(UPDATED_LINE_PHONE);
        assertThat(testContact.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContact.getComments()).isEqualTo(UPDATED_COMMENTS);
        assertThat(testContact.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testContact.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingContact() throws Exception {
        int databaseSizeBeforeUpdate = contactRepository.findAll().size();

        // Create the Contact
        ContactDTO contactDTO = contactMapper.toDto(contact);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContactMockMvc.perform(put("/api/contacts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contactDTO)))
            .andExpect(status().isCreated());

        // Validate the Contact in the database
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContact() throws Exception {
        // Initialize the database
        contactRepository.saveAndFlush(contact);
        int databaseSizeBeforeDelete = contactRepository.findAll().size();

        // Get the contact
        restContactMockMvc.perform(delete("/api/contacts/{id}", contact.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Contact> contactList = contactRepository.findAll();
        assertThat(contactList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contact.class);
        Contact contact1 = new Contact();
        contact1.setId(1L);
        Contact contact2 = new Contact();
        contact2.setId(contact1.getId());
        assertThat(contact1).isEqualTo(contact2);
        contact2.setId(2L);
        assertThat(contact1).isNotEqualTo(contact2);
        contact1.setId(null);
        assertThat(contact1).isNotEqualTo(contact2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactDTO.class);
        ContactDTO contactDTO1 = new ContactDTO();
        contactDTO1.setId(1L);
        ContactDTO contactDTO2 = new ContactDTO();
        assertThat(contactDTO1).isNotEqualTo(contactDTO2);
        contactDTO2.setId(contactDTO1.getId());
        assertThat(contactDTO1).isEqualTo(contactDTO2);
        contactDTO2.setId(2L);
        assertThat(contactDTO1).isNotEqualTo(contactDTO2);
        contactDTO1.setId(null);
        assertThat(contactDTO1).isNotEqualTo(contactDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contactMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contactMapper.fromId(null)).isNull();
    }
}
