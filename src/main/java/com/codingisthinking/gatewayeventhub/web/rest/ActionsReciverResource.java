package com.codingisthinking.gatewayeventhub.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.codingisthinking.gatewayeventhub.domain.Action;
import com.codingisthinking.gatewayeventhub.web.rest.util.HeaderUtil;
import com.codingisthinking.gatewayeventhub.web.websocket.server.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

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
    public ResponseEntity<String> passActionToLanGateway(@Valid @RequestBody Action action) throws URISyntaxException {
        log.debug("REST request to save Realmkeys : {}", action);
        Message msg = new Message();
        msg.setAuthor("Example LAN ID ");
        msg.setAction(action);
        this.template.convertAndSend("/topic/messages", msg);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(ENTITY_NAME, "Header"))
            .body("Message recived and passed to LanGateway.");
    }


}
