package de.informatikwerk.gatewayeventhub.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.informatikwerk.gatewayeventhub.config.ApplicationProperties;
import de.informatikwerk.gatewayeventhub.domain.Action;
import de.informatikwerk.gatewayeventhub.domain.Gateways;
import de.informatikwerk.gatewayeventhub.domain.Realmkeys;
import de.informatikwerk.gatewayeventhub.repository.GatewaysRepository;
import de.informatikwerk.gatewayeventhub.repository.RealmkeysRepository;
import de.informatikwerk.gatewayeventhub.service.SyncHttpsRequestThreadRegistry;
import de.informatikwerk.gatewayeventhub.service.SyncResponseObserver;
import de.informatikwerk.gatewayeventhub.web.rest.util.HeaderUtil;
import de.informatikwerk.gatewayeventhub.web.websocket.server.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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

    private final GatewaysRepository gatewaysRepository;

    private final RealmkeysRepository realmkeysRepository;

    private  final ApplicationProperties applicationProperties;



    public ActionsReciverResource(GatewaysRepository gatewaysRepository, RealmkeysRepository realmkeysRepository, ApplicationProperties applicationProperties) {
        this.gatewaysRepository = gatewaysRepository;
        this.realmkeysRepository = realmkeysRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * POST  /action : Passing action to langateway.
     *
     * @param action the action to pass.
     * @return the ResponseEntity TODO description
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/action")
    @Timed
    public ResponseEntity<Action> passActionToLanGateway(@Valid @RequestBody Action action) throws URISyntaxException {
        log.debug("REST pass action to correct LanGateway : {}", action);
        SyncResponseObserver syncResponseObserver = new SyncResponseObserver(3000);
        
        Message msg = getMessageTemplateForAction(action);
        String uniqId = getWebsocketUniqIdForAction(action);
        if(uniqId == null){
            return ResponseEntity.notFound().build();
        }
        this.template.convertAndSend("/doors/actions/" + uniqId, msg);
        SyncHttpsRequestThreadRegistry syncHttpsRequestThreadRegistry = SyncHttpsRequestThreadRegistry.instance();
        syncHttpsRequestThreadRegistry.put(msg.getMessageId(), syncResponseObserver);
        syncResponseObserver.waitForResponse();
        Message message = syncResponseObserver.getMessage();
        if(message == null){
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, msg.getMessageId()))
                .body(null);
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, message.getAction().getRealmKey()))
            .body(message.getAction());
    }

    public String getWebsocketUniqIdForAction(Action action){
        Realmkeys realmkeys = new Realmkeys();
        String uniqId = null;
        realmkeys.setRealmkey(action.getRealmKey());
        Realmkeys matchingRealmkey = realmkeysRepository.findOne(Example.of(realmkeys));
        if(matchingRealmkey != null){
            Gateways gateways = gatewaysRepository.findOne(matchingRealmkey.getGateways().getId());
            if(gateways != null){
                uniqId = gateways.getWebsocketId();
                return uniqId;
            }
        }
        log.error("No matching realmkeys for Realmkey =[ " + action.getRealmKey() + " ] in our database.");
        return null;
    }

    public Message getMessageTemplateForAction(Action action){
        Message msg = new Message();
        msg.setAuthor("GatewayEventHub");
        msg.setAction(action);
        return msg;
    }

    /**
     * Get  /value : Passing value request to langateway.
     *
     * @param action the action to pass.
     * @return the ResponseEntity TODO description
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @GetMapping("/value")
    @Timed
    public ResponseEntity<Action> passValueRequestToLanGateway(@Valid @RequestBody Action action) throws URISyntaxException {
        log.debug("REST pass value request to correct LanGateway : {}", action);
        SyncResponseObserver syncResponseObserver = new SyncResponseObserver(3000);

        Message msg = getMessageTemplateForAction(action);
        String uniqId = getWebsocketUniqIdForAction(action);
        if(uniqId == null){
            return ResponseEntity.notFound().build();
        }
        this.template.convertAndSend("/doors/actions/" + uniqId, msg);
        SyncHttpsRequestThreadRegistry syncHttpsRequestThreadRegistry = SyncHttpsRequestThreadRegistry.instance();
        syncHttpsRequestThreadRegistry.put(msg.getMessageId(), syncResponseObserver);
        syncResponseObserver.waitForResponse();
        Message message = syncResponseObserver.getMessage();
        if(message == null){
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, msg.getMessageId()))
                .body(null);
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, message.getAction().getRealmKey()))
            .body(message.getAction());
    }


}
