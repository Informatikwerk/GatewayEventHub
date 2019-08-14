package de.informatikwerk.gatewayeventhub.web.rest;

import de.informatikwerk.gatewayeventhub.GatewayeventhubApp;

import de.informatikwerk.gatewayeventhub.domain.ConfigElement;
import de.informatikwerk.gatewayeventhub.repository.ConfigElementRepository;
import de.informatikwerk.gatewayeventhub.service.ConfigElementService;
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
 * Test class for the ConfigElementResource REST controller.
 *
 * @see ConfigElementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewayeventhubApp.class)
public class ConfigElementResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private ConfigElementRepository configElementRepository;

    @Autowired
    private ConfigElementService configElementService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restConfigElementMockMvc;

    private ConfigElement configElement;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigElementResource configElementResource = new ConfigElementResource(configElementService);
        this.restConfigElementMockMvc = MockMvcBuilders.standaloneSetup(configElementResource)
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
    public static ConfigElement createEntity(EntityManager em) {
        ConfigElement configElement = new ConfigElement()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE);
        return configElement;
    }

    @Before
    public void initTest() {
        configElement = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfigElement() throws Exception {
        int databaseSizeBeforeCreate = configElementRepository.findAll().size();

        // Create the ConfigElement
        restConfigElementMockMvc.perform(post("/api/config-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configElement)))
            .andExpect(status().isCreated());

        // Validate the ConfigElement in the database
        List<ConfigElement> configElementList = configElementRepository.findAll();
        assertThat(configElementList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigElement testConfigElement = configElementList.get(configElementList.size() - 1);
        assertThat(testConfigElement.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testConfigElement.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createConfigElementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configElementRepository.findAll().size();

        // Create the ConfigElement with an existing ID
        configElement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigElementMockMvc.perform(post("/api/config-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configElement)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigElement in the database
        List<ConfigElement> configElementList = configElementRepository.findAll();
        assertThat(configElementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = configElementRepository.findAll().size();
        // set the field null
        configElement.setKey(null);

        // Create the ConfigElement, which fails.

        restConfigElementMockMvc.perform(post("/api/config-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configElement)))
            .andExpect(status().isBadRequest());

        List<ConfigElement> configElementList = configElementRepository.findAll();
        assertThat(configElementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = configElementRepository.findAll().size();
        // set the field null
        configElement.setValue(null);

        // Create the ConfigElement, which fails.

        restConfigElementMockMvc.perform(post("/api/config-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configElement)))
            .andExpect(status().isBadRequest());

        List<ConfigElement> configElementList = configElementRepository.findAll();
        assertThat(configElementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfigElements() throws Exception {
        // Initialize the database
        configElementRepository.saveAndFlush(configElement);

        // Get all the configElementList
        restConfigElementMockMvc.perform(get("/api/config-elements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configElement.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getConfigElement() throws Exception {
        // Initialize the database
        configElementRepository.saveAndFlush(configElement);

        // Get the configElement
        restConfigElementMockMvc.perform(get("/api/config-elements/{id}", configElement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configElement.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfigElement() throws Exception {
        // Get the configElement
        restConfigElementMockMvc.perform(get("/api/config-elements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfigElement() throws Exception {
        // Initialize the database
        configElementService.save(configElement);

        int databaseSizeBeforeUpdate = configElementRepository.findAll().size();

        // Update the configElement
        ConfigElement updatedConfigElement = configElementRepository.findOne(configElement.getId());
        // Disconnect from session so that the updates on updatedConfigElement are not directly saved in db
        em.detach(updatedConfigElement);
        updatedConfigElement
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);

        restConfigElementMockMvc.perform(put("/api/config-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfigElement)))
            .andExpect(status().isOk());

        // Validate the ConfigElement in the database
        List<ConfigElement> configElementList = configElementRepository.findAll();
        assertThat(configElementList).hasSize(databaseSizeBeforeUpdate);
        ConfigElement testConfigElement = configElementList.get(configElementList.size() - 1);
        assertThat(testConfigElement.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConfigElement.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingConfigElement() throws Exception {
        int databaseSizeBeforeUpdate = configElementRepository.findAll().size();

        // Create the ConfigElement

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restConfigElementMockMvc.perform(put("/api/config-elements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configElement)))
            .andExpect(status().isCreated());

        // Validate the ConfigElement in the database
        List<ConfigElement> configElementList = configElementRepository.findAll();
        assertThat(configElementList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteConfigElement() throws Exception {
        // Initialize the database
        configElementService.save(configElement);

        int databaseSizeBeforeDelete = configElementRepository.findAll().size();

        // Get the configElement
        restConfigElementMockMvc.perform(delete("/api/config-elements/{id}", configElement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ConfigElement> configElementList = configElementRepository.findAll();
        assertThat(configElementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigElement.class);
        ConfigElement configElement1 = new ConfigElement();
        configElement1.setId(1L);
        ConfigElement configElement2 = new ConfigElement();
        configElement2.setId(configElement1.getId());
        assertThat(configElement1).isEqualTo(configElement2);
        configElement2.setId(2L);
        assertThat(configElement1).isNotEqualTo(configElement2);
        configElement1.setId(null);
        assertThat(configElement1).isNotEqualTo(configElement2);
    }
}
