package com.codingisthinking.gatewayeventhub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.codingisthinking.gatewayeventhub.domain.ConfigWrapper;
import com.codingisthinking.gatewayeventhub.domain.Gateways;

import com.codingisthinking.gatewayeventhub.domain.Realmkeys;
import com.codingisthinking.gatewayeventhub.repository.GatewaysRepository;
import com.codingisthinking.gatewayeventhub.repository.RealmkeysRepository;
import com.codingisthinking.gatewayeventhub.web.rest.errors.BadRequestAlertException;
import com.codingisthinking.gatewayeventhub.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Gateways.
 */
@RestController
@RequestMapping("/api")
public class GatewaysResource {

    private final Logger log = LoggerFactory.getLogger(GatewaysResource.class);

    private static final String ENTITY_NAME = "gateways";

    private final GatewaysRepository gatewaysRepository;

    private final RealmkeysRepository realmkeysRepository;

    public GatewaysResource(GatewaysRepository gatewaysRepository, RealmkeysRepository realmkeysRepository) {
        this.gatewaysRepository = gatewaysRepository;
        this.realmkeysRepository = realmkeysRepository;
    }

    /**
     * POST  /gateways : Create a new gateways.
     *
     * @param gateways the gateways to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gateways, or with status 400 (Bad Request) if the gateways has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gateways")
    @Timed
    public ResponseEntity<Gateways> createGateways(@Valid @RequestBody Gateways gateways) throws URISyntaxException {
        log.debug("REST request to save Gateways : {}", gateways);
        if (gateways.getId() != null) {
            throw new BadRequestAlertException("A new gateways cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gateways result = gatewaysRepository.save(gateways);
        return ResponseEntity.created(new URI("/api/gateways/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /gateways/config : Create a new config.
     *
     * @param configWrapper the config to create
     * @return the ResponseEntity with status 201 (Created) and with body the new gateways, or with status 400 (Bad Request) if the gateways has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/gateways/config")
    @Timed
    public ResponseEntity<Gateways> createGatewaysAndKeys(@Valid @RequestBody ConfigWrapper configWrapper) throws URISyntaxException {
        log.debug("REST request to save WrapperObject : {}");
        log.debug("REST request to save WrapperObject gates : {}", configWrapper.getGateways().toString());
        log.debug("REST request to save WrapperObject keys : {}", configWrapper.getRealmkeys().toString());
        Gateways gateways = configWrapper.getGateways();
        if (gateways.getId() != null) {
            throw new BadRequestAlertException("A new gateways cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gateways result = gatewaysRepository.save(gateways);
        List<Realmkeys> realmkeys = configWrapper.getRealmkeys();
        for(Realmkeys key : realmkeys){
            key.setGateways(result);
        }
        realmkeysRepository.save(realmkeys);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gateways/config : Updates an existing config.
     *
     * @param configWrapper the gateways to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gateways,
     * or with status 400 (Bad Request) if the gateways is not valid,
     * or with status 500 (Internal Server Error) if the gateways couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gateways/config")
    @Timed
    public ResponseEntity<Gateways> updateGatewaysAndKeys(@Valid @RequestBody ConfigWrapper configWrapper) throws URISyntaxException {
        log.debug("REST request to update Gateways : {}", configWrapper);
        Gateways gateways = configWrapper.getGateways();
        if (gateways.getId() == null) {
            return createGatewaysAndKeys(configWrapper);
        }
        Gateways result = gatewaysRepository.save(gateways);
        List<Realmkeys> realmkeys = configWrapper.getRealmkeys();
        Realmkeys example = new Realmkeys();
        example.setGateways(result);
        realmkeysRepository.delete(realmkeysRepository.findAll(Example.of(example)));
        for(Realmkeys key : realmkeys){
            key.setGateways(result);
        }
        realmkeysRepository.save(realmkeys);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gateways.getId().toString()))
            .body(result);
    }
    /**
     * PUT  /gateways : Updates an existing gateways.
     *
     * @param gateways the gateways to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated gateways,
     * or with status 400 (Bad Request) if the gateways is not valid,
     * or with status 500 (Internal Server Error) if the gateways couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/gateways")
    @Timed
    public ResponseEntity<Gateways> updateGateways(@Valid @RequestBody Gateways gateways) throws URISyntaxException {
        log.debug("REST request to update Gateways : {}", gateways);
        if (gateways.getId() == null) {
            return createGateways(gateways);
        }
        Gateways result = gatewaysRepository.save(gateways);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, gateways.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gateways : get all the gateways.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of gateways in body
     */
    @GetMapping("/gateways")
    @Timed
    public List<Gateways> getAllGateways() {
        log.debug("REST request to get all Gateways");
        return gatewaysRepository.findAll();
        }

    /**
     * GET  /gateways/:id : get the "id" gateways.
     *
     * @param id the id of the gateways to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the gateways, or with status 404 (Not Found)
     */
    @GetMapping("/gateways/{id}")
    @Timed
    public ResponseEntity<Gateways> getGateways(@PathVariable Long id) {
        log.debug("REST request to get Gateways : {}", id);
        Gateways gateways = gatewaysRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(gateways));
    }

    /**
     * DELETE  /gateways/:id : delete the "id" gateways.
     *
     * @param id the id of the gateways to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/gateways/{id}")
    @Timed
    public ResponseEntity<Void> deleteGateways(@PathVariable Long id) {
        log.debug("REST request to delete Gateways : {}", id);
        gatewaysRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
