package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AbstractCassandraTest;
import com.mycompany.myapp.StudentApp;

import com.mycompany.myapp.domain.Hartron;
import com.mycompany.myapp.repository.HartronRepository;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HartronResource REST controller.
 *
 * @see HartronResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudentApp.class)
public class HartronResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_EMPLOYEE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE_NO = 1;
    private static final Integer UPDATED_PHONE_NO = 2;

    @Autowired
    private HartronRepository hartronRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restHartronMockMvc;

    private Hartron hartron;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HartronResource hartronResource = new HartronResource(hartronRepository);
        this.restHartronMockMvc = MockMvcBuilders.standaloneSetup(hartronResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hartron createEntity() {
        Hartron hartron = new Hartron()
            .employee_name(DEFAULT_EMPLOYEE_NAME)
            .designation(DEFAULT_DESIGNATION)
            .phone_no(DEFAULT_PHONE_NO);
        return hartron;
    }

    @Before
    public void initTest() {
        hartronRepository.deleteAll();
        hartron = createEntity();
    }

    @Test
    public void createHartron() throws Exception {
        int databaseSizeBeforeCreate = hartronRepository.findAll().size();

        // Create the Hartron
        restHartronMockMvc.perform(post("/api/hartrons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hartron)))
            .andExpect(status().isCreated());

        // Validate the Hartron in the database
        List<Hartron> hartronList = hartronRepository.findAll();
        assertThat(hartronList).hasSize(databaseSizeBeforeCreate + 1);
        Hartron testHartron = hartronList.get(hartronList.size() - 1);
        assertThat(testHartron.getEmployee_name()).isEqualTo(DEFAULT_EMPLOYEE_NAME);
        assertThat(testHartron.getDesignation()).isEqualTo(DEFAULT_DESIGNATION);
        assertThat(testHartron.getPhone_no()).isEqualTo(DEFAULT_PHONE_NO);
    }

    @Test
    public void createHartronWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hartronRepository.findAll().size();

        // Create the Hartron with an existing ID
        hartron.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restHartronMockMvc.perform(post("/api/hartrons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hartron)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Hartron> hartronList = hartronRepository.findAll();
        assertThat(hartronList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllHartrons() throws Exception {
        // Initialize the database
        hartronRepository.save(hartron);

        // Get all the hartronList
        restHartronMockMvc.perform(get("/api/hartrons"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hartron.getId().toString())))
            .andExpect(jsonPath("$.[*].employee_name").value(hasItem(DEFAULT_EMPLOYEE_NAME.toString())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].phone_no").value(hasItem(DEFAULT_PHONE_NO)));
    }

    @Test
    public void getHartron() throws Exception {
        // Initialize the database
        hartronRepository.save(hartron);

        // Get the hartron
        restHartronMockMvc.perform(get("/api/hartrons/{id}", hartron.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hartron.getId().toString()))
            .andExpect(jsonPath("$.employee_name").value(DEFAULT_EMPLOYEE_NAME.toString()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION.toString()))
            .andExpect(jsonPath("$.phone_no").value(DEFAULT_PHONE_NO));
    }

    @Test
    public void getNonExistingHartron() throws Exception {
        // Get the hartron
        restHartronMockMvc.perform(get("/api/hartrons/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateHartron() throws Exception {
        // Initialize the database
        hartronRepository.save(hartron);
        int databaseSizeBeforeUpdate = hartronRepository.findAll().size();

        // Update the hartron
        Hartron updatedHartron = hartronRepository.findOne(hartron.getId());
        updatedHartron
            .employee_name(UPDATED_EMPLOYEE_NAME)
            .designation(UPDATED_DESIGNATION)
            .phone_no(UPDATED_PHONE_NO);

        restHartronMockMvc.perform(put("/api/hartrons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHartron)))
            .andExpect(status().isOk());

        // Validate the Hartron in the database
        List<Hartron> hartronList = hartronRepository.findAll();
        assertThat(hartronList).hasSize(databaseSizeBeforeUpdate);
        Hartron testHartron = hartronList.get(hartronList.size() - 1);
        assertThat(testHartron.getEmployee_name()).isEqualTo(UPDATED_EMPLOYEE_NAME);
        assertThat(testHartron.getDesignation()).isEqualTo(UPDATED_DESIGNATION);
        assertThat(testHartron.getPhone_no()).isEqualTo(UPDATED_PHONE_NO);
    }

    @Test
    public void updateNonExistingHartron() throws Exception {
        int databaseSizeBeforeUpdate = hartronRepository.findAll().size();

        // Create the Hartron

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restHartronMockMvc.perform(put("/api/hartrons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hartron)))
            .andExpect(status().isCreated());

        // Validate the Hartron in the database
        List<Hartron> hartronList = hartronRepository.findAll();
        assertThat(hartronList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteHartron() throws Exception {
        // Initialize the database
        hartronRepository.save(hartron);
        int databaseSizeBeforeDelete = hartronRepository.findAll().size();

        // Get the hartron
        restHartronMockMvc.perform(delete("/api/hartrons/{id}", hartron.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Hartron> hartronList = hartronRepository.findAll();
        assertThat(hartronList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hartron.class);
        Hartron hartron1 = new Hartron();
        hartron1.setId(UUID.randomUUID());
        Hartron hartron2 = new Hartron();
        hartron2.setId(hartron1.getId());
        assertThat(hartron1).isEqualTo(hartron2);
        hartron2.setId(UUID.randomUUID());
        assertThat(hartron1).isNotEqualTo(hartron2);
        hartron1.setId(null);
        assertThat(hartron1).isNotEqualTo(hartron2);
    }
}
