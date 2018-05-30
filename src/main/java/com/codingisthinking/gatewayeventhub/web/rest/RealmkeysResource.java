package com.codingisthinking.gatewayeventhub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.codingisthinking.gatewayeventhub.domain.Realmkeys;

import com.codingisthinking.gatewayeventhub.repository.RealmkeysRepository;
import com.codingisthinking.gatewayeventhub.web.rest.errors.BadRequestAlertException;
import com.codingisthinking.gatewayeventhub.web.rest.util.HeaderUtil;
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
 * REST controller for managing Realmkeys.
 */
@RestController
@RequestMapping("/api")
public class RealmkeysResource {

    private final Logger log = LoggerFactory.getLogger(RealmkeysResource.class);

    private static final String ENTITY_NAME = "realmkeys";

    private final RealmkeysRepository realmkeysRepository;

    public RealmkeysResource(RealmkeysRepository realmkeysRepository) {
        this.realmkeysRepository = realmkeysRepository;
    }

    /**
     * POST  /realmkeys : Create a new realmkeys.
     *
     * @param realmkeys the realmkeys to create
     * @return the ResponseEntity with status 201 (Created) and with body the new realmkeys, or with status 400 (Bad Request) if the realmkeys has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/realmkeys")
    @Timed
    public ResponseEntity<Realmkeys> createRealmkeys(@Valid @RequestBody Realmkeys realmkeys) throws URISyntaxException {
        log.debug("REST request to save Realmkeys : {}", realmkeys);
        if (realmkeys.getId() != null) {
            throw new BadRequestAlertException("A new realmkeys cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Realmkeys result = realmkeysRepository.save(realmkeys);
        return ResponseEntity.created(new URI("/api/realmkeys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /realmkeys : Updates an existing realmkeys.
     *
     * @param realmkeys the realmkeys to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated realmkeys,
     * or with status 400 (Bad Request) if the realmkeys is not valid,
     * or with status 500 (Internal Server Error) if the realmkeys couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/realmkeys")
    @Timed
    public ResponseEntity<Realmkeys> updateRealmkeys(@Valid @RequestBody Realmkeys realmkeys) throws URISyntaxException {
        log.debug("REST request to update Realmkeys : {}", realmkeys);
        if (realmkeys.getId() == null) {
            return createRealmkeys(realmkeys);
        }
        Realmkeys result = realmkeysRepository.save(realmkeys);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, realmkeys.getId().toString()))
            .body(result);
    }

    /**
     * GET  /realmkeys : get all the realmkeys.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of realmkeys in body
     */
    @GetMapping("/realmkeys")
    @Timed
    public List<Realmkeys> getAllRealmkeys() {
        log.debug("REST request to get all Realmkeys");
        return realmkeysRepository.findAll();
        }

    /**
     * GET  /realmkeys/:id : get the "id" realmkeys.
     *
     * @param id the id of the realmkeys to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the realmkeys, or with status 404 (Not Found)
     */
    @GetMapping("/realmkeys/{id}")
    @Timed
    public ResponseEntity<Realmkeys> getRealmkeys(@PathVariable Long id) {
        log.debug("REST request to get Realmkeys : {}", id);
        Realmkeys realmkeys = realmkeysRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(realmkeys));
    }

    /**
     * DELETE  /realmkeys/:id : delete the "id" realmkeys.
     *
     * @param id the id of the realmkeys to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/realmkeys/{id}")
    @Timed
    public ResponseEntity<Void> deleteRealmkeys(@PathVariable Long id) {
        log.debug("REST request to delete Realmkeys : {}", id);
        realmkeysRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
