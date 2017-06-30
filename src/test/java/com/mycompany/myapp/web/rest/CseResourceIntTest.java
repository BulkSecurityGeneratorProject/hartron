package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.AbstractCassandraTest;
import com.mycompany.myapp.StudentApp;

import com.mycompany.myapp.domain.Cse;
import com.mycompany.myapp.repository.CseRepository;
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
 * Test class for the CseResource REST controller.
 *
 * @see CseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudentApp.class)
public class CseResourceIntTest extends AbstractCassandraTest {

    private static final String DEFAULT_DEPT = "AAAAAAAAAA";
    private static final String UPDATED_DEPT = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH = "BBBBBBBBBB";

    @Autowired
    private CseRepository cseRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restCseMockMvc;

    private Cse cse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CseResource cseResource = new CseResource(cseRepository);
        this.restCseMockMvc = MockMvcBuilders.standaloneSetup(cseResource)
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
    public static Cse createEntity() {
        Cse cse = new Cse()
            .dept(DEFAULT_DEPT)
            .branch(DEFAULT_BRANCH);
        return cse;
    }

    @Before
    public void initTest() {
        cseRepository.deleteAll();
        cse = createEntity();
    }

    @Test
    public void createCse() throws Exception {
        int databaseSizeBeforeCreate = cseRepository.findAll().size();

        // Create the Cse
        restCseMockMvc.perform(post("/api/cses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cse)))
            .andExpect(status().isCreated());

        // Validate the Cse in the database
        List<Cse> cseList = cseRepository.findAll();
        assertThat(cseList).hasSize(databaseSizeBeforeCreate + 1);
        Cse testCse = cseList.get(cseList.size() - 1);
        assertThat(testCse.getDept()).isEqualTo(DEFAULT_DEPT);
        assertThat(testCse.getBranch()).isEqualTo(DEFAULT_BRANCH);
    }

    @Test
    public void createCseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cseRepository.findAll().size();

        // Create the Cse with an existing ID
        cse.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCseMockMvc.perform(post("/api/cses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cse)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Cse> cseList = cseRepository.findAll();
        assertThat(cseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllCses() throws Exception {
        // Initialize the database
        cseRepository.save(cse);

        // Get all the cseList
        restCseMockMvc.perform(get("/api/cses"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cse.getId().toString())))
            .andExpect(jsonPath("$.[*].dept").value(hasItem(DEFAULT_DEPT.toString())))
            .andExpect(jsonPath("$.[*].branch").value(hasItem(DEFAULT_BRANCH.toString())));
    }

    @Test
    public void getCse() throws Exception {
        // Initialize the database
        cseRepository.save(cse);

        // Get the cse
        restCseMockMvc.perform(get("/api/cses/{id}", cse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cse.getId().toString()))
            .andExpect(jsonPath("$.dept").value(DEFAULT_DEPT.toString()))
            .andExpect(jsonPath("$.branch").value(DEFAULT_BRANCH.toString()));
    }

    @Test
    public void getNonExistingCse() throws Exception {
        // Get the cse
        restCseMockMvc.perform(get("/api/cses/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCse() throws Exception {
        // Initialize the database
        cseRepository.save(cse);
        int databaseSizeBeforeUpdate = cseRepository.findAll().size();

        // Update the cse
        Cse updatedCse = cseRepository.findOne(cse.getId());
        updatedCse
            .dept(UPDATED_DEPT)
            .branch(UPDATED_BRANCH);

        restCseMockMvc.perform(put("/api/cses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCse)))
            .andExpect(status().isOk());

        // Validate the Cse in the database
        List<Cse> cseList = cseRepository.findAll();
        assertThat(cseList).hasSize(databaseSizeBeforeUpdate);
        Cse testCse = cseList.get(cseList.size() - 1);
        assertThat(testCse.getDept()).isEqualTo(UPDATED_DEPT);
        assertThat(testCse.getBranch()).isEqualTo(UPDATED_BRANCH);
    }

    @Test
    public void updateNonExistingCse() throws Exception {
        int databaseSizeBeforeUpdate = cseRepository.findAll().size();

        // Create the Cse

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCseMockMvc.perform(put("/api/cses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cse)))
            .andExpect(status().isCreated());

        // Validate the Cse in the database
        List<Cse> cseList = cseRepository.findAll();
        assertThat(cseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    public void deleteCse() throws Exception {
        // Initialize the database
        cseRepository.save(cse);
        int databaseSizeBeforeDelete = cseRepository.findAll().size();

        // Get the cse
        restCseMockMvc.perform(delete("/api/cses/{id}", cse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Cse> cseList = cseRepository.findAll();
        assertThat(cseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cse.class);
        Cse cse1 = new Cse();
        cse1.setId(UUID.randomUUID());
        Cse cse2 = new Cse();
        cse2.setId(cse1.getId());
        assertThat(cse1).isEqualTo(cse2);
        cse2.setId(UUID.randomUUID());
        assertThat(cse1).isNotEqualTo(cse2);
        cse1.setId(null);
        assertThat(cse1).isNotEqualTo(cse2);
    }
}
