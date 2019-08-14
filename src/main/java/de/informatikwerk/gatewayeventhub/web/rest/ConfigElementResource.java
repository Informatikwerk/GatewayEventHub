package de.informatikwerk.gatewayeventhub.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.informatikwerk.gatewayeventhub.domain.ConfigElement;
import de.informatikwerk.gatewayeventhub.service.ConfigElementService;
import de.informatikwerk.gatewayeventhub.web.rest.errors.BadRequestAlertException;
import de.informatikwerk.gatewayeventhub.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ConfigElement.
 */
@RestController
@RequestMapping("/api")
public class ConfigElementResource {

    private final Logger log = LoggerFactory.getLogger(ConfigElementResource.class);

    private static final String ENTITY_NAME = "configElement";

    private final ConfigElementService configElementService;

    public ConfigElementResource(ConfigElementService configElementService) {
        this.configElementService = configElementService;
    }

    /**
     * POST  /config-elements : Create a new configElement.
     *
     * @param configElement the configElement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new configElement, or with status 400 (Bad Request) if the configElement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/config-elements")
    @Timed
    public ResponseEntity<ConfigElement> createConfigElement(@Valid @RequestBody ConfigElement configElement) throws URISyntaxException {
        log.debug("REST request to save ConfigElement : {}", configElement);
        if (configElement.getId() != null) {
            throw new BadRequestAlertException("A new configElement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigElement result = configElementService.save(configElement);
        return ResponseEntity.created(new URI("/api/config-elements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /config-elements : Updates an existing configElement.
     *
     * @param configElement the configElement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated configElement,
     * or with status 400 (Bad Request) if the configElement is not valid,
     * or with status 500 (Internal Server Error) if the configElement couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/config-elements")
    @Timed
    public ResponseEntity<ConfigElement> updateConfigElement(@Valid @RequestBody ConfigElement configElement) throws URISyntaxException {
        log.debug("REST request to update ConfigElement : {}", configElement);
        if (configElement.getId() == null) {
            return createConfigElement(configElement);
        }
        ConfigElement result = configElementService.save(configElement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, configElement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /config-elements : get all the configElements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of configElements in body
     */
    @GetMapping("/config-elements")
    @Timed
    public List<ConfigElement> getAllConfigElements() {
        log.debug("REST request to get all ConfigElements");
        return configElementService.findAll();
        }

    /**
     * GET  /config-elements/:id : get the "id" configElement.
     *
     * @param id the id of the configElement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the configElement, or with status 404 (Not Found)
     */
    @GetMapping("/config-elements/{id}")
    @Timed
    public ResponseEntity<ConfigElement> getConfigElement(@PathVariable Long id) {
        log.debug("REST request to get ConfigElement : {}", id);
        ConfigElement configElement = configElementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(configElement));
    }

    /**
     * DELETE  /config-elements/:id : delete the "id" configElement.
     *
     * @param id the id of the configElement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/config-elements/{id}")
    @Timed
    public ResponseEntity<Void> deleteConfigElement(@PathVariable Long id) {
        log.debug("REST request to delete ConfigElement : {}", id);
        configElementService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
