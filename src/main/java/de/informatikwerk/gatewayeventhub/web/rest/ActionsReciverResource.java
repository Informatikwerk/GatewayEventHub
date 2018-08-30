package de.informatikwerk.gatewayeventhub.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.informatikwerk.gatewayeventhub.config.ApplicationProperties;
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
import redis.clients.jedis.JedisPoolConfig;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.time.Duration;

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

    private JedisPool jedisPool;


    public ActionsReciverResource(GatewaysRepository gatewaysRepository, RealmkeysRepository realmkeysRepository, ApplicationProperties applicationProperties) {
        this.gatewaysRepository = gatewaysRepository;
        this.realmkeysRepository = realmkeysRepository;
        this.applicationProperties = applicationProperties;
        if(this.jedisPool == null){
            this.jedisPool = new JedisPool(applicationProperties.buildPoolConfig(), applicationProperties.getJedisIp(), applicationProperties.getJedisPort());
        }
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
        String uniqueTestValue = null;
        Jedis jedis = jedisPool.getResource();
        try {
            Thread.sleep(8000);
            uniqueTestValue = jedis.get(msg.getMessageId());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
        }
        jedis.close();
        Action responseAction = action;
        if(uniqueTestValue == null){
            responseAction.setData("Timeout");
        } else {
            responseAction.setData(uniqueTestValue);
        }

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, action.getRealmKey()))
            .body(responseAction);
    }


}
