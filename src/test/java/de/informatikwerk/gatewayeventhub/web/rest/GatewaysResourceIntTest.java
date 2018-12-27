package de.informatikwerk.gatewayeventhub.web.rest;

import de.informatikwerk.gatewayeventhub.GatewayeventhubApp;

import de.informatikwerk.gatewayeventhub.domain.Gateways;
import de.informatikwerk.gatewayeventhub.repository.GatewaysRepository;
import de.informatikwerk.gatewayeventhub.repository.RealmkeysRepository;
import de.informatikwerk.gatewayeventhub.web.rest.errors.ExceptionTranslator;

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

import static de.informatikwerk.gatewayeventhub.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the GatewaysResource REST controller.
 *
 * @see GatewaysResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewayeventhubApp.class)
public class GatewaysResourceIntTest {

    private static final String DEFAULT_EXTERNAL_IP = "AAAAAAAAAA";
    private static final String UPDATED_EXTERNAL_IP = "BBBBBBBBBB";

    private static final String DEFAULT_INTERNAL_IP = "AAAAAAAAAA";
    private static final String UPDATED_INTERNAL_IP = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSOCKET_ID = "AAAAAAAAAA";
    private static final String UPDATED_WEBSOCKET_ID = "BBBBBBBBBB";

    @Autowired
    private GatewaysRepository gatewaysRepository;

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

    private MockMvc restGatewaysMockMvc;

    private Gateways gateways;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GatewaysResource gatewaysResource = new GatewaysResource(gatewaysRepository,realmkeysRepository);
        this.restGatewaysMockMvc = MockMvcBuilders.standaloneSetup(gatewaysResource)
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
    public static Gateways createEntity(EntityManager em) {
        Gateways gateways = new Gateways()
            .externalIp(DEFAULT_EXTERNAL_IP)
            .internalIp(DEFAULT_INTERNAL_IP)
            .userId(DEFAULT_USER_ID)
            .websocketId(DEFAULT_WEBSOCKET_ID);
        return gateways;
    }

    @Before
    public void initTest() {
        gateways = createEntity(em);
    }

    @Test
    @Transactional
    public void createGateways() throws Exception {
        int databaseSizeBeforeCreate = gatewaysRepository.findAll().size();

        // Create the Gateways
        restGatewaysMockMvc.perform(post("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gateways)))
            .andExpect(status().isCreated());

        // Validate the Gateways in the database
        List<Gateways> gatewaysList = gatewaysRepository.findAll();
        assertThat(gatewaysList).hasSize(databaseSizeBeforeCreate + 1);
        Gateways testGateways = gatewaysList.get(gatewaysList.size() - 1);
        assertThat(testGateways.getExternalIp()).isEqualTo(DEFAULT_EXTERNAL_IP);
        assertThat(testGateways.getInternalIp()).isEqualTo(DEFAULT_INTERNAL_IP);
        assertThat(testGateways.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testGateways.getWebsocketId()).isEqualTo(DEFAULT_WEBSOCKET_ID);
    }

    @Test
    @Transactional
    public void createGatewaysWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gatewaysRepository.findAll().size();

        // Create the Gateways with an existing ID
        gateways.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGatewaysMockMvc.perform(post("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gateways)))
            .andExpect(status().isBadRequest());

        // Validate the Gateways in the database
        List<Gateways> gatewaysList = gatewaysRepository.findAll();
        assertThat(gatewaysList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkExternalIpIsRequired() throws Exception {
        int databaseSizeBeforeTest = gatewaysRepository.findAll().size();
        // set the field null
        gateways.setExternalIp(null);

        // Create the Gateways, which fails.

        restGatewaysMockMvc.perform(post("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gateways)))
            .andExpect(status().isBadRequest());

        List<Gateways> gatewaysList = gatewaysRepository.findAll();
        assertThat(gatewaysList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkInternalIpIsRequired() throws Exception {
        int databaseSizeBeforeTest = gatewaysRepository.findAll().size();
        // set the field null
        gateways.setInternalIp(null);

        // Create the Gateways, which fails.

        restGatewaysMockMvc.perform(post("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gateways)))
            .andExpect(status().isBadRequest());

        List<Gateways> gatewaysList = gatewaysRepository.findAll();
        assertThat(gatewaysList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = gatewaysRepository.findAll().size();
        // set the field null
        gateways.setUserId(null);

        // Create the Gateways, which fails.

        restGatewaysMockMvc.perform(post("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gateways)))
            .andExpect(status().isBadRequest());

        List<Gateways> gatewaysList = gatewaysRepository.findAll();
        assertThat(gatewaysList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWebsocketIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = gatewaysRepository.findAll().size();
        // set the field null
        gateways.setWebsocketId(null);

        // Create the Gateways, which fails.

        restGatewaysMockMvc.perform(post("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gateways)))
            .andExpect(status().isBadRequest());

        List<Gateways> gatewaysList = gatewaysRepository.findAll();
        assertThat(gatewaysList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGateways() throws Exception {
        // Initialize the database
        gatewaysRepository.saveAndFlush(gateways);

        // Get all the gatewaysList
        restGatewaysMockMvc.perform(get("/api/gateways?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gateways.getId().intValue())))
            .andExpect(jsonPath("$.[*].externalIp").value(hasItem(DEFAULT_EXTERNAL_IP.toString())))
            .andExpect(jsonPath("$.[*].internalIp").value(hasItem(DEFAULT_INTERNAL_IP.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].websocketId").value(hasItem(DEFAULT_WEBSOCKET_ID.toString())));
    }

    @Test
    @Transactional
    public void getGateways() throws Exception {
        // Initialize the database
        gatewaysRepository.saveAndFlush(gateways);

        // Get the gateways
        restGatewaysMockMvc.perform(get("/api/gateways/{id}", gateways.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gateways.getId().intValue()))
            .andExpect(jsonPath("$.externalIp").value(DEFAULT_EXTERNAL_IP.toString()))
            .andExpect(jsonPath("$.internalIp").value(DEFAULT_INTERNAL_IP.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.websocketId").value(DEFAULT_WEBSOCKET_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGateways() throws Exception {
        // Get the gateways
        restGatewaysMockMvc.perform(get("/api/gateways/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGateways() throws Exception {
        // Initialize the database
        gatewaysRepository.saveAndFlush(gateways);
        int databaseSizeBeforeUpdate = gatewaysRepository.findAll().size();

        // Update the gateways
        Gateways updatedGateways = gatewaysRepository.findOne(gateways.getId());
        // Disconnect from session so that the updates on updatedGateways are not directly saved in db
        em.detach(updatedGateways);
        updatedGateways
            .externalIp(UPDATED_EXTERNAL_IP)
            .internalIp(UPDATED_INTERNAL_IP)
            .userId(UPDATED_USER_ID)
            .websocketId(UPDATED_WEBSOCKET_ID);

        restGatewaysMockMvc.perform(put("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGateways)))
            .andExpect(status().isOk());

        // Validate the Gateways in the database
        List<Gateways> gatewaysList = gatewaysRepository.findAll();
        assertThat(gatewaysList).hasSize(databaseSizeBeforeUpdate);
        Gateways testGateways = gatewaysList.get(gatewaysList.size() - 1);
        assertThat(testGateways.getExternalIp()).isEqualTo(UPDATED_EXTERNAL_IP);
        assertThat(testGateways.getInternalIp()).isEqualTo(UPDATED_INTERNAL_IP);
        assertThat(testGateways.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testGateways.getWebsocketId()).isEqualTo(UPDATED_WEBSOCKET_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingGateways() throws Exception {
        int databaseSizeBeforeUpdate = gatewaysRepository.findAll().size();

        // Create the Gateways

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGatewaysMockMvc.perform(put("/api/gateways")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gateways)))
            .andExpect(status().isCreated());

        // Validate the Gateways in the database
        List<Gateways> gatewaysList = gatewaysRepository.findAll();
        assertThat(gatewaysList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGateways() throws Exception {
        // Initialize the database
        gatewaysRepository.saveAndFlush(gateways);
        int databaseSizeBeforeDelete = gatewaysRepository.findAll().size();

        // Get the gateways
        restGatewaysMockMvc.perform(delete("/api/gateways/{id}", gateways.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Gateways> gatewaysList = gatewaysRepository.findAll();
        assertThat(gatewaysList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gateways.class);
        Gateways gateways1 = new Gateways();
        gateways1.setId(1L);
        Gateways gateways2 = new Gateways();
        gateways2.setId(gateways1.getId());
        assertThat(gateways1).isEqualTo(gateways2);
        gateways2.setId(2L);
        assertThat(gateways1).isNotEqualTo(gateways2);
        gateways1.setId(null);
        assertThat(gateways1).isNotEqualTo(gateways2);
    }
}
