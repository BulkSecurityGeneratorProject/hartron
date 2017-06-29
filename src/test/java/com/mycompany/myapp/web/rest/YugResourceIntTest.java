package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AbstractCassandraTest;
import com.mycompany.myapp.StudentApp;

import com.mycompany.myapp.domain.Yug;
import com.mycompany.myapp.repository.YugRepository;
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
 * Test class for the YugResource REST controller.
 *
 * @see YugResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudentApp.class)
public class YugResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SEX = "AAAAAAAAAA";
    private static final String UPDATED_SEX = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 20;
    private static final Integer UPDATED_AGE = 21;

    @Autowired
    private YugRepository yugRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restYugMockMvc;

    private Yug yug;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        YugResource yugResource = new YugResource(yugRepository);
        this.restYugMockMvc = MockMvcBuilders.standaloneSetup(yugResource)
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
    public static Yug createEntity() {
        Yug yug = new Yug()
            .name(DEFAULT_NAME)
            .sex(DEFAULT_SEX)
            .email(DEFAULT_EMAIL)
            .age(DEFAULT_AGE);
        return yug;
    }

    @Before
    public void initTest() {
        yugRepository.deleteAll();
        yug = createEntity();
    }

    @Test
    public void createYug() throws Exception {
        int databaseSizeBeforeCreate = yugRepository.findAll().size();

        // Create the Yug
        restYugMockMvc.perform(post("/api/yugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(yug)))
            .andExpect(status().isCreated());

        // Validate the Yug in the database
        List<Yug> yugList = yugRepository.findAll();
        assertThat(yugList).hasSize(databaseSizeBeforeCreate + 1);
        Yug testYug = yugList.get(yugList.size() - 1);
        assertThat(testYug.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testYug.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testYug.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testYug.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    public void createYugWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = yugRepository.findAll().size();

        // Create the Yug with an existing ID
        yug.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restYugMockMvc.perform(post("/api/yugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(yug)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Yug> yugList = yugRepository.findAll();
        assertThat(yugList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = yugRepository.findAll().size();
        // set the field null
        yug.setName(null);

        // Create the Yug, which fails.

        restYugMockMvc.perform(post("/api/yugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(yug)))
            .andExpect(status().isBadRequest());

        List<Yug> yugList = yugRepository.findAll();
        assertThat(yugList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllYugs() throws Exception {
        // Initialize the database
        yugRepository.save(yug);

        // Get all the yugList
        restYugMockMvc.perform(get("/api/yugs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(yug.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)));
    }

    @Test
    public void getYug() throws Exception {
        // Initialize the database
        yugRepository.save(yug);

        // Get the yug
        restYugMockMvc.perform(get("/api/yugs/{id}", yug.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(yug.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE));
    }

    @Test
    public void getNonExistingYug() throws Exception {
        // Get the yug
        restYugMockMvc.perform(get("/api/yugs/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateYug() throws Exception {
        // Initialize the database
        yugRepository.save(yug);
        int databaseSizeBeforeUpdate = yugRepository.findAll().size();

        // Update the yug
        Yug updatedYug = yugRepository.findOne(yug.getId());
        updatedYug
            .name(UPDATED_NAME)
            .sex(UPDATED_SEX)
            .email(UPDATED_EMAIL)
            .age(UPDATED_AGE);

        restYugMockMvc.perform(put("/api/yugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedYug)))
            .andExpect(status().isOk());

        // Validate the Yug in the database
        List<Yug> yugList = yugRepository.findAll();
        assertThat(yugList).hasSize(databaseSizeBeforeUpdate);
        Yug testYug = yugList.get(yugList.size() - 1);
        assertThat(testYug.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testYug.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testYug.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testYug.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    public void updateNonExistingYug() throws Exception {
        int databaseSizeBeforeUpdate = yugRepository.findAll().size();

        // Create the Yug

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restYugMockMvc.perform(put("/api/yugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(yug)))
            .andExpect(status().isCreated());

        // Validate the Yug in the database
        List<Yug> yugList = yugRepository.findAll();
        assertThat(yugList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteYug() throws Exception {
        // Initialize the database
        yugRepository.save(yug);
        int databaseSizeBeforeDelete = yugRepository.findAll().size();

        // Get the yug
        restYugMockMvc.perform(delete("/api/yugs/{id}", yug.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Yug> yugList = yugRepository.findAll();
        assertThat(yugList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Yug.class);
        Yug yug1 = new Yug();
        yug1.setId(UUID.randomUUID());
        Yug yug2 = new Yug();
        yug2.setId(yug1.getId());
        assertThat(yug1).isEqualTo(yug2);
        yug2.setId(UUID.randomUUID());
        assertThat(yug1).isNotEqualTo(yug2);
        yug1.setId(null);
        assertThat(yug1).isNotEqualTo(yug2);
    }
}
