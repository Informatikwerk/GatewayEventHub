package com.codingisthinking.gatewayeventhub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.codingisthinking.gatewayeventhub.domain.Action;
import com.codingisthinking.gatewayeventhub.domain.Gateways;
import com.codingisthinking.gatewayeventhub.domain.Realmkeys;
import com.codingisthinking.gatewayeventhub.repository.RealmkeysRepository;
import com.codingisthinking.gatewayeventhub.web.rest.errors.BadRequestAlertException;
import com.codingisthinking.gatewayeventhub.web.rest.util.HeaderUtil;
import com.codingisthinking.gatewayeventhub.web.websocket.server.model.ResponseMessage;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Recived messages.
 */
@RestController
@RequestMapping("/api")
public class ActionsReciverResource {

    private final Logger log = LoggerFactory.getLogger(ActionsReciverResource.class);

    private static final String ENTITY_NAME = "RecivedMessage";

    @Autowired
    private SimpMessagingTemplate template;

    /**
     * POST  /action : Passing action to langateway.
     *
     * @param action the action to pass.
     * @return the ResponseEntity TODO description
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/action")
    @Timed
    public ResponseEntity<String> createRealmkeys(@Valid @RequestBody Action action) throws URISyntaxException {
        log.debug("REST request to save Realmkeys : {}", action);
        this.template.convertAndSend("/topic/messages", new ResponseMessage(action.toString()));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(ENTITY_NAME, "Header"))
            .body("TODO response msg");
    }


}
