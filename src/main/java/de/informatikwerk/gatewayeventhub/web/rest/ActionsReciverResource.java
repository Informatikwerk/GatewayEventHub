package de.informatikwerk.gatewayeventhub.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.informatikwerk.gatewayeventhub.domain.Action;
import de.informatikwerk.gatewayeventhub.domain.Gateways;
import de.informatikwerk.gatewayeventhub.domain.Realmkeys;
import de.informatikwerk.gatewayeventhub.repository.GatewaysRepository;
import de.informatikwerk.gatewayeventhub.repository.RealmkeysRepository;
import de.informatikwerk.gatewayeventhub.web.rest.util.HeaderUtil;
import de.informatikwerk.gatewayeventhub.web.websocket.server.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

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


    public ActionsReciverResource(GatewaysRepository gatewaysRepository, RealmkeysRepository realmkeysRepository) {
        this.gatewaysRepository = gatewaysRepository;
        this.realmkeysRepository = realmkeysRepository;
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
        log.debug("REST request to save Realmkeys : {}", action);
        Message msg = new Message();
        msg.setAuthor("GatewayEventHub");
        Realmkeys realmkeys = new Realmkeys();
        String uniqId = "unique";
        realmkeys.setRealmkey(action.getRealmKey());
        Realmkeys matchingRealmkey = realmkeysRepository.findOne(Example.of(realmkeys));
        if(matchingRealmkey != null){
            Gateways gateways = gatewaysRepository.findOne(matchingRealmkey.getGateways().getId());
            if(gateways != null){
                uniqId = gateways.getWebsocketId();
            } else {
                log.error("No matching gateway for Realmkey =[ " + action.getRealmKey() + " ] in our database.");
            }
        } else {
            log.error("No matching realmkeys for Realmkey =[ " + action.getRealmKey() + " ] in our database.");
        }
        msg.setAction(action);
        this.template.convertAndSend("/doors/actions/" + uniqId, msg);
        JedisPool jedisPool = new JedisPool("127.0.0.1", 6379);
        Jedis jedis = jedisPool.getResource();
        String uniqueTestValue = null;
        //TODO replace with more elegant solution
        while(uniqueTestValue == null){
            try {
                uniqueTestValue = jedis.get(msg.getMessageId());
                System.out.println("Checked value and it's " + uniqueTestValue);
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Action responseAction = action;
        responseAction.setData(uniqueTestValue);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, action.getRealmKey()))
            .body(responseAction);
    }

}
