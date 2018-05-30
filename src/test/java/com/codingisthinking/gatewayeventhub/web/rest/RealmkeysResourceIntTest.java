package com.codingisthinking.gatewayeventhub.web.rest;

import com.codingisthinking.gatewayeventhub.GatewayeventhubApp;

import com.codingisthinking.gatewayeventhub.domain.Realmkeys;
import com.codingisthinking.gatewayeventhub.domain.Gateways;
import com.codingisthinking.gatewayeventhub.repository.RealmkeysRepository;
import com.codingisthinking.gatewayeventhub.web.rest.errors.ExceptionTranslator;

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

import static com.codingisthinking.gatewayeventhub.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RealmkeysResource REST controller.
 *
 * @see RealmkeysResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewayeventhubApp.class)
public class RealmkeysResourceIntTest {

    private static final String DEFAULT_REALMKEY = "AAAAAAAAAA";
    private static final String UPDATED_REALMKEY = "BBBBBBBBBB";

    @Autowired
    private RealmkeysRepository realmkeysRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRealmkeysMockMvc;

    private Realmkeys realmkeys;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RealmkeysResource realmkeysResource = new RealmkeysResource(realmkeysRepository);
        this.restRealmkeysMockMvc = MockMvcBuilders.standaloneSetup(realmkeysResource)
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
    public static Realmkeys createEntity(EntityManager em) {
        Realmkeys realmkeys = new Realmkeys()
            .realmkey(DEFAULT_REALMKEY);
        // Add required entity
        Gateways gateways = GatewaysResourceIntTest.createEntity(em);
        em.persist(gateways);
        em.flush();
        realmkeys.setGateways(gateways);
        return realmkeys;
    }

    @Before
    public void initTest() {
        realmkeys = createEntity(em);
    }

    @Test
    @Transactional
    public void createRealmkeys() throws Exception {
        int databaseSizeBeforeCreate = realmkeysRepository.findAll().size();

        // Create the Realmkeys
        restRealmkeysMockMvc.perform(post("/api/realmkeys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(realmkeys)))
            .andExpect(status().isCreated());

        // Validate the Realmkeys in the database
        List<Realmkeys> realmkeysList = realmkeysRepository.findAll();
        assertThat(realmkeysList).hasSize(databaseSizeBeforeCreate + 1);
        Realmkeys testRealmkeys = realmkeysList.get(realmkeysList.size() - 1);
        assertThat(testRealmkeys.getRealmkey()).isEqualTo(DEFAULT_REALMKEY);
    }

    @Test
    @Transactional
    public void createRealmkeysWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = realmkeysRepository.findAll().size();

        // Create the Realmkeys with an existing ID
        realmkeys.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRealmkeysMockMvc.perform(post("/api/realmkeys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(realmkeys)))
            .andExpect(status().isBadRequest());

        // Validate the Realmkeys in the database
        List<Realmkeys> realmkeysList = realmkeysRepository.findAll();
        assertThat(realmkeysList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRealmkeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = realmkeysRepository.findAll().size();
        // set the field null
        realmkeys.setRealmkey(null);

        // Create the Realmkeys, which fails.

        restRealmkeysMockMvc.perform(post("/api/realmkeys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(realmkeys)))
            .andExpect(status().isBadRequest());

        List<Realmkeys> realmkeysList = realmkeysRepository.findAll();
        assertThat(realmkeysList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRealmkeys() throws Exception {
        // Initialize the database
        realmkeysRepository.saveAndFlush(realmkeys);

        // Get all the realmkeysList
        restRealmkeysMockMvc.perform(get("/api/realmkeys?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(realmkeys.getId().intValue())))
            .andExpect(jsonPath("$.[*].realmkey").value(hasItem(DEFAULT_REALMKEY.toString())));
    }

    @Test
    @Transactional
    public void getRealmkeys() throws Exception {
        // Initialize the database
        realmkeysRepository.saveAndFlush(realmkeys);

        // Get the realmkeys
        restRealmkeysMockMvc.perform(get("/api/realmkeys/{id}", realmkeys.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(realmkeys.getId().intValue()))
            .andExpect(jsonPath("$.realmkey").value(DEFAULT_REALMKEY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRealmkeys() throws Exception {
        // Get the realmkeys
        restRealmkeysMockMvc.perform(get("/api/realmkeys/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRealmkeys() throws Exception {
        // Initialize the database
        realmkeysRepository.saveAndFlush(realmkeys);
        int databaseSizeBeforeUpdate = realmkeysRepository.findAll().size();

        // Update the realmkeys
        Realmkeys updatedRealmkeys = realmkeysRepository.findOne(realmkeys.getId());
        // Disconnect from session so that the updates on updatedRealmkeys are not directly saved in db
        em.detach(updatedRealmkeys);
        updatedRealmkeys
            .realmkey(UPDATED_REALMKEY);

        restRealmkeysMockMvc.perform(put("/api/realmkeys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRealmkeys)))
            .andExpect(status().isOk());

        // Validate the Realmkeys in the database
        List<Realmkeys> realmkeysList = realmkeysRepository.findAll();
        assertThat(realmkeysList).hasSize(databaseSizeBeforeUpdate);
        Realmkeys testRealmkeys = realmkeysList.get(realmkeysList.size() - 1);
        assertThat(testRealmkeys.getRealmkey()).isEqualTo(UPDATED_REALMKEY);
    }

    @Test
    @Transactional
    public void updateNonExistingRealmkeys() throws Exception {
        int databaseSizeBeforeUpdate = realmkeysRepository.findAll().size();

        // Create the Realmkeys

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRealmkeysMockMvc.perform(put("/api/realmkeys")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(realmkeys)))
            .andExpect(status().isCreated());

        // Validate the Realmkeys in the database
        List<Realmkeys> realmkeysList = realmkeysRepository.findAll();
        assertThat(realmkeysList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRealmkeys() throws Exception {
        // Initialize the database
        realmkeysRepository.saveAndFlush(realmkeys);
        int databaseSizeBeforeDelete = realmkeysRepository.findAll().size();

        // Get the realmkeys
        restRealmkeysMockMvc.perform(delete("/api/realmkeys/{id}", realmkeys.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Realmkeys> realmkeysList = realmkeysRepository.findAll();
        assertThat(realmkeysList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Realmkeys.class);
        Realmkeys realmkeys1 = new Realmkeys();
        realmkeys1.setId(1L);
        Realmkeys realmkeys2 = new Realmkeys();
        realmkeys2.setId(realmkeys1.getId());
        assertThat(realmkeys1).isEqualTo(realmkeys2);
        realmkeys2.setId(2L);
        assertThat(realmkeys1).isNotEqualTo(realmkeys2);
        realmkeys1.setId(null);
        assertThat(realmkeys1).isNotEqualTo(realmkeys2);
    }
}
